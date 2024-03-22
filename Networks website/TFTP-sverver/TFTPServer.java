import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class TFTPServer {
  public static final int ERR_FILE_NOT_FOUND = 1;
  public static final int ERR_ACCESS_VIOLATION = 2;
  public static final int ERR_FILE_ALREADY_EXISTS = 6;
  public static final int TFTPPORT = 4970;
  public static final int BUFSIZE = 516;
  public static final String READDIR = "test_a3/read/"; //custom address at your PC
  public static final String WRITEDIR = "test_a3/write/"; //custom address at your PC
  // OP codes
  public static final int OP_RRQ = 1;
  public static final int OP_WRQ = 2;
  public static final int OP_DAT = 3;
  public static final int OP_ACK = 4;
  public static final int OP_ERR = 5;

  public static void main(String[] args) {
    if (args.length > 0) {
      System.err.printf("usage: java %s\n", TFTPServer.class.getCanonicalName());
      System.exit(1);
    }
    //Starting the server
    try {
      TFTPServer server = new TFTPServer();
      server.start();
    } catch (SocketException e) {
      e.printStackTrace();
    }
  }

  private void start() throws SocketException {
    byte[] buf = new byte[BUFSIZE];

    // Create socket
    DatagramSocket socket = new DatagramSocket(null);

    // Create local bind point
    SocketAddress localBindPoint = new InetSocketAddress(TFTPPORT);
    socket.bind(localBindPoint);

    System.out.printf("Listening at port %d for new requests\n", TFTPPORT);

    // Loop to handle client requests
    while (true) {

      final InetSocketAddress clientAddress = receiveFrom(socket, buf);

      // If clientAddress is null, an error occurred in receiveFrom()
      if (clientAddress == null)
        continue;

      final StringBuffer requestedFile = new StringBuffer();
      final int reqtype = ParseRQ(buf, requestedFile);

      new Thread() {
        public void run() {
          try {
            DatagramSocket sendSocket = new DatagramSocket(0);

            // Connect to client
            sendSocket.connect(clientAddress);

            System.out.printf("%s request for %s from %s using port %d\n",
                    (reqtype == OP_RRQ) ? "Read" : "Write",
                    clientAddress.getHostName(), clientAddress.getHostString(), clientAddress.getPort());

            System.out.println("opcode = " + reqtype);

            // Read request
            if (reqtype == OP_RRQ) {
              requestedFile.insert(0, READDIR);
              HandleRQ(sendSocket, requestedFile.toString(), OP_RRQ);
            }
            // Write request
            else if (reqtype == OP_WRQ) {
              requestedFile.insert(0, WRITEDIR);
              HandleRQ(sendSocket, requestedFile.toString(), OP_WRQ);
            } else {
              send_ERR(sendSocket, "bad op code", 4);
            }
            sendSocket.close();
          } catch (SocketException e) {
            e.printStackTrace();
          }
        }
      }.start();
    }
  }

  /**
   * Reads the first block of data, i.e., the request for an action (read or write).
   *
   * @param socket (socket to read from)
   * @param buf    (where to store the read data)
   * @return socketAddress (the socket address of the client)
   */
  private InetSocketAddress receiveFrom(DatagramSocket socket, byte[] buf) {
    try {
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);
      return new InetSocketAddress(packet.getAddress(), packet.getPort());
    } catch (IOException e) {
      System.err.println("Error receiving packet: " + e.getMessage());
      return null;
    }
  }

  /**
   * Parses the request in buf to retrieve the type of request and requestedFile
   *
   * @param buf           (received request)
   * @param requestedFile (name of file to read/write)
   * @return opcode (request type: RRQ or WRQ)
   */
  private int ParseRQ(byte[] buf, StringBuffer requestedFile) {
    int len = 2; // Skip the opcode
    while (buf[len] != 0) requestedFile.append((char) buf[len++]);

    // Assuming the mode is always "octet"
    return (buf[0] << 8) | buf[1]; // This returns the opcode (RRQ or WRQ)
  }

  /**
   * Handles RRQ and WRQ requests
   *
   * @param sendSocket    (socket used to send/receive packets)
   * @param requestedFile (name of file to read/write)
   * @param opcode        (RRQ or WRQ)
   */
  private void HandleRQ(DatagramSocket sendSocket, String requestedFile, int opcode) {
    System.out.println("opcode " + opcode);
    if (opcode == OP_RRQ) {
      File file = new File(requestedFile);
      if (!file.exists()) {
        send_ERR(sendSocket, "File not found", ERR_FILE_NOT_FOUND);
        return;
      }
      send_DATA_receive_ACK(sendSocket, requestedFile);
    } else if (opcode == OP_WRQ) {
      File file = new File(requestedFile);
      if (file.exists()) {
        send_ERR(sendSocket, "File already exists", ERR_FILE_ALREADY_EXISTS);
        return;
      }
      receive_DATA_send_ACK(sendSocket, requestedFile);
    } else {
      System.err.println("Invalid request. Sending an error packet.");
      send_ERR(sendSocket, "Invalid request", 4);
    }
  }

  /**
   * To be implemented
   */
  private boolean send_DATA_receive_ACK(DatagramSocket sendSocket, String requestedFile) {
    try {
      File file = new File(requestedFile);
      if (!file.exists()) {
        send_ERR(sendSocket, "File not found", 1);
        return false;
      }
      InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
      byte[] dataBuffer = new byte[512];
      int blockNumber = 1;
      int bytesRead;

      while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
        byte[] dataPacket = new byte[4 + bytesRead];
        dataPacket[0] = 0;
        dataPacket[1] = OP_DAT;
        dataPacket[2] = (byte) (blockNumber >> 8);
        dataPacket[3] = (byte) (blockNumber & 0xFF);
        System.arraycopy(dataBuffer, 0, dataPacket, 4, bytesRead);

        DatagramPacket packetToSend = new DatagramPacket(dataPacket, dataPacket.length, sendSocket.getInetAddress(), sendSocket.getPort());
        sendSocket.send(packetToSend);

        // Now wait for the ACK
        byte[] ackBuf = new byte[4];
        DatagramPacket ackPacket = new DatagramPacket(ackBuf, ackBuf.length);
        boolean ackReceived = false;
        for (int attempts = 0; attempts < 5 && !ackReceived; attempts++) {
          try {
            sendSocket.setSoTimeout(30); // 30 mseconds timeout
            sendSocket.receive(ackPacket);

            // Check if the ACK is for the correct block
            if (ackBuf[1] == OP_ACK) {
              int ackBlockNumber = ((ackBuf[2] & 0xff) << 8) | (ackBuf[3] & 0xff);
              if (ackBlockNumber == blockNumber) {
                ackReceived = true; // Correct ACK received
              }
            }
          } catch (SocketTimeoutException ste) {
            // Resend the data packet if timeout occurred
            System.out.println("Timeout, resending data packet for block " + blockNumber);
            sendSocket.send(packetToSend);
          }
        }

        if (!ackReceived) {
          System.out.println("Failed to receive ACK for block " + blockNumber + ", giving up.");
          inputStream.close();
          return false;
        }

        blockNumber++;
      }

      // Send the last packet if file size is a multiple of 512 bytes
      if (file.length() % 512 == 0) {
        byte[] lastPacket = {0, OP_DAT, (byte) (blockNumber >> 8), (byte) (blockNumber & 0xFF)};
        sendSocket.send(new DatagramPacket(lastPacket, lastPacket.length, sendSocket.getInetAddress(), sendSocket.getPort()));
      }

      inputStream.close();
      return true;
    } catch (IOException e) {
      send_ERR(sendSocket, "Error sending file", 0);
      return false;
    }
  }


  private boolean receive_DATA_send_ACK(DatagramSocket sendSocket, String requestedFile) {
    try {
      System.out.println("creating " + requestedFile);
      FileOutputStream fileOutputStream = new FileOutputStream(requestedFile);
      int blockNumber = 0;
      boolean lastPacketReceived = false;

      // Prepare and send the first ACK
      byte[] ackPacket = new byte[4];
      ackPacket[0] = 0;
      ackPacket[1] = OP_ACK;
      ackPacket[2] = (byte) (blockNumber >> 8);
      ackPacket[3] = (byte) (blockNumber & 0xFF);

      DatagramPacket packetToSend = new DatagramPacket(ackPacket, ackPacket.length, sendSocket.getInetAddress(), sendSocket.getPort());
      sendSocket.send(packetToSend);

      while (!lastPacketReceived) {
        byte[] dataBuf = new byte[BUFSIZE];
        DatagramPacket receivePacket = new DatagramPacket(dataBuf, dataBuf.length);
        sendSocket.receive(receivePacket);

        byte[] packetData = receivePacket.getData();
        int length = receivePacket.getLength();
        int receivedBlockNumber = ((packetData[2] & 0xff) << 8) | (packetData[3] & 0xff);

        if (blockNumber + 1 != receivedBlockNumber) {
          System.err.println("Expected block number " + (blockNumber + 1) + " but received " + receivedBlockNumber + ". Resending ACK for block " + blockNumber);
          continue;
        }

        blockNumber = receivedBlockNumber;

        if (length > 4) {
          fileOutputStream.write(packetData, 4, length - 4);
        }

        if (length < BUFSIZE) {
          lastPacketReceived = true;
        }

        ackPacket[2] = (byte) (blockNumber >> 8);
        ackPacket[3] = (byte) (blockNumber & 0xFF);
        packetToSend.setData(ackPacket);
        sendSocket.send(packetToSend);
      }

      fileOutputStream.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }


  private void send_ERR(DatagramSocket sendSocket, String errorMessage, int errorCode) {
    System.out.println(errorMessage);
    byte[] errorMsgBytes = errorMessage.getBytes();
    byte[] errorPacket = new byte[4 + errorMsgBytes.length + 1];
    errorPacket[0] = 0;
    errorPacket[1] = OP_ERR;
    errorPacket[2] = 0;
    errorPacket[3] = (byte) errorCode;
    System.arraycopy(errorMsgBytes, 0, errorPacket, 4, errorMsgBytes.length);
    errorPacket[errorPacket.length - 1] = 0; // Null terminator

    try {
      sendSocket.send(new DatagramPacket(errorPacket, errorPacket.length, sendSocket.getInetAddress(), sendSocket.getPort()));
    } catch (IOException e) {
      System.err.println("Failed to send error packet: " + e.getMessage());
    }
  }
}



