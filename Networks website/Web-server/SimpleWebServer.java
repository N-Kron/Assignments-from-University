import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SimpleWebServer {

  public OutputStream output;
  public PrintWriter writer;
  public String publicDir;
  public BufferedReader reader;
  public byte[] buffer;
  public InputStream inputStream;


  // code 404
  public void httpNotFound() throws IOException {
    String notFoundHtml = """
            <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">\n
            <html><head>\n
            <title>404 Not Found</title>\n
            </head><body>\n
            <h1>Not Found</h1>\n
            <p>The requested URL was not found on this server.</p>\n
            </body></html>\n      
              """;
    writer.println("HTTP/1.1 404 Not Found");
    writer.println("Content-Type: text/html");
    writer.println("Content-Length: " + notFoundHtml.length());
    writer.println();
    writer.println(notFoundHtml);
    output.flush();

    System.out.println("HTTP/1.1 404 Not Found");
  }


  // code 302
  public void httpFound() throws IOException {
    String hardcodedURL = "suckless.html";
    writer.println("HTTP/1.1 302 Found");
    writer.println("Location: " + hardcodedURL);
    output.flush();

    System.out.println("HTTP/1.1 302 Found");
    System.out.println("redirecting to " + hardcodedURL);
  }

  // code 500
  public void httpInternalServerError() {
    try {
      String internalServerErrorHtml = """
            <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">\n
            <html><head>\n
            <title>500 Internal Server Error</title>\n
            </head><body>\n
            <h1>Internal Server Error</h1>\n
            <p>The server encountered an internal error and was unable to complete your request.</p>\n
            </body></html>\n      
              """;
      writer.println("HTTP/1.1 500 Internal Server Error");
      writer.println("Content-Type: text/html");
      writer.println("Content-Length: " + internalServerErrorHtml.length());
      writer.println();
      writer.println(internalServerErrorHtml);
      output.flush();
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

    System.out.println("HTTP/1.1 500 Internal Server Error");
  }


  // code 200
  public void httpAuthorized() throws IOException {
    String authorizedHtml = """
            <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">\n
            <html><head>\n
            <title>200 OK</title>\n
            </head><body>\n
            <h1>Authorized</h1>\n
            <p>The password and username were correct</p>\n
            </body></html>\n      
              """;
    writer.println("HTTP/1.1 200 OK");
    writer.println("Content-Type: text/html");
    writer.println("Content-Length: " + authorizedHtml.length());
    writer.println("Connection: close");
    writer.println();
    writer.println(authorizedHtml);
    output.flush();

    System.out.println("HTTP/1.1 200 OK");
  }


  public void httpUnauthorized() throws IOException {
    String unauthorizedHtml = """
            <!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">\n
            <html><head>\n
            <title>401 Unauthorized</title>\n
            </head><body>\n
            <h1>Unauthorized</h1>\n
            <p>The password and username were incorrect</p>\n
            </body></html>\n      
              """;
    writer.println("HTTP/1.1 401 Unauthorized");
    writer.println("Content-Type: text/html");
    writer.println("Content-Length: " + unauthorizedHtml.length());
    writer.println("Connection: close");
    writer.println();
    writer.println(unauthorizedHtml);
    output.flush();

    System.out.println("HTTP/1.1 401 Unauthorized");
  }


  public int firstIndexOf(byte[] outerArray, byte[] smallerArray) {
    for (int i = 0; i < outerArray.length - smallerArray.length + 1; ++i) {
      boolean found = true;
      for (int j = 0; j < smallerArray.length; ++j) {
        if (outerArray[i + j] != smallerArray[j]) {
          found = false;
          break;
        }
      }
      if (found) return i;
    }
    return -1;
  }

  // program starts here
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.out.println("Usage: java SimpleWebServer <port> <publicDir>");
      System.out.println("<port> - the port to listen on");
      System.out.println("<publicDir> - the directory to serve files from");
      return;
    }

    SimpleWebServer webServer = new SimpleWebServer();
    webServer.start(args);

  }

  void start(String[] args) {
    int port = Integer.parseInt(args[0]);
    publicDir = args[1];

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server is listening on port " + port);

      while (true) {
        try (Socket socket = serverSocket.accept()) {
          inputStream = socket.getInputStream();
          TimeUnit.MILLISECONDS.sleep(25);
          buffer = inputStream.readNBytes(inputStream.available());
          System.out.println(buffer.length);
          reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer)));

          try {
          String line = reader.readLine();
          String[] parts = line.split(" ");
          String method = parts[0];

          output = socket.getOutputStream();
          writer = new PrintWriter(output, true);
          //TODO add catch that leads to errorcode 500 internal error
          if (method.equals("GET")) {
            new Thread() {
              public void run() {
                try {
                  getMethod(parts);
                } catch (IOException ex) {
                  httpInternalServerError();
                }
              }
            }.run();
          }

          if (method.equals("POST")) {
            StringBuilder headers = new StringBuilder();
            // reading headers
            while (!line.isEmpty()) {
              headers.append(line);
              headers.append(System.lineSeparator());
              line = reader.readLine();
            }

            System.out.println(headers.toString().split("\n")[0]);

            int contentLength = 0;
            if (headers.toString().contains("Content-Length")) {
              contentLength = Integer.parseInt(headers.toString().split("Content-Length: ")[1].split("\n")[0].trim());
            }
            // because it has to be final
            final int finalContentLength = contentLength;
            // if its a login request
            if ("/login.html".equals(parts[1])) {
              new Thread() {
                public void run() {
                  try {
                    login(finalContentLength);
                  } catch (IOException ex) {
                    httpInternalServerError();
                  }
                }
              }.run();

            } else if ("/upload.html".equals(parts[1])) {
              new Thread() {
                public void run() {
                  try {
                    upload();
                  } catch (Exception ex) {
                    httpInternalServerError();
                  }
                }
              }.run();
            }
          }
          } catch (Exception ex) {
            System.out.println("Server exception: " + ex.getMessage());
            httpInternalServerError();
          }
          socket.close();
        } catch (IOException ex) {
          System.out.println("Server exception: " + ex.getMessage());
          httpInternalServerError();
        }
      }

    } catch (NumberFormatException ex) {
      System.out.println("Invalid port number. Please provide a valid integer.");
    } catch (IOException ex) {
      httpInternalServerError();
      System.out.println("Server exception: " + ex.getMessage());
      httpInternalServerError();
    } catch (Exception ex) {
      System.out.println("Unexpected error: " + ex.getMessage());
      httpInternalServerError();
    }
  }


  public void getMethod(String[] parts) throws IOException {
    String fileRequested = parts[1].endsWith("/") ? parts[1] + "index.html" : parts[1];
    System.out.println("Request: GET " + fileRequested);
    File file = new File(publicDir, fileRequested);
    file = file.isDirectory() ? new File(publicDir, fileRequested + "/index.html") : file;
    if (file.exists()) {
      String contentType = Files.probeContentType(file.toPath());
      writer.println("HTTP/1.1 200 OK");
      writer.println("Content-Type: " + contentType);
      writer.println("Content-Length: " + file.length());
      writer.println();

      Files.copy(file.toPath(), output);
      output.flush();
      System.out.println("HTTP/1.1 200 OK");
    }
    // hardcoded url to trigger 302
    else if (fileRequested.equals("/systemd")) {
      httpFound();
    } else {
      httpNotFound();
    }
  }


  public void login(int contentLength) throws IOException {
    String requestBody = "";
    // reading body
    char[] body = new char[contentLength];
    reader.read(body, 0, contentLength); // reading body
    requestBody = new String(body);
    System.out.println("  " + requestBody);
    // parsing credentials from body
    String[] credentials = requestBody.split("&");
    String name = credentials[0].split("=")[1];
    String pass = credentials[1].split("=")[1];
    // reading credentials from a file
    BufferedReader fileReader = new BufferedReader(new FileReader("public/credentials.txt"));
    String nameFromFile = fileReader.readLine();
    String passFromFile = fileReader.readLine();
    fileReader.close();
    // checking credentials
    if (name.equals(nameFromFile) && pass.equals(passFromFile)) {
      httpAuthorized();
    } else {
      httpUnauthorized();
    }
  }


  public void upload() throws Exception {
    // finding the boundary
    String boundary = reader.readLine() + "--";
    // creating the name from md5 hash of the request
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hash = md.digest(buffer);
    String name = (new BigInteger(hash).toString(32)).substring(2);

    // creating the file
    File outputFile = new File("public/" + name + ".png");
    FileOutputStream outStream = new FileOutputStream(outputFile);

    // seperating bytes representing the image
    byte[] boundaryBin = ("\r\n" + boundary).getBytes("ASCII");
    byte[] beginningBin = "Content-Type: image/png\r\n\r\n".getBytes("ASCII");
    int startIndex = firstIndexOf(buffer, beginningBin) + beginningBin.length;
    int endIndex = firstIndexOf(buffer, boundaryBin);
    int size = 0;
    while (endIndex < 0) {
      System.out.println("part: " + startIndex + "  " + buffer.length);
      byte[] image = Arrays.copyOfRange(buffer, startIndex, buffer.length);
      size += image.length;
      outStream.write(image);
      buffer = inputStream.readNBytes(inputStream.available());
      endIndex = firstIndexOf(buffer, boundaryBin);
      startIndex = 0;
    }
    byte[] image = Arrays.copyOfRange(buffer, startIndex, endIndex);
    size += image.length;
    System.out.println(startIndex + "  " + endIndex);
    outStream.write(image);
    System.out.println("name: " + name + ".png size: " + size);

    outStream.flush();
    outStream.close();
    writer.println("HTTP/1.1 200 OK");
    writer.println();
    System.out.println("HTTP/1.1 200 OK");
  }

}

