# SimpleWebServer

This is a simple web server written in Java! It can handle GET and POST requests, and it can respond with different HTTP status codes based on the request.

## How to Run

Run the following command:
java SimpleWebServer.java <port> <publicDir>

Run the Java program: After successful compilation, run the program using the java command. You need to provide two arguments: the port number on which the server will listen, and the public directory from which the server will serve files:
java SimpleWebServer <port> <publicDir>

Replace <port> with the port number you want the server to listen on (for example, 8080), and replace <publicDir> with the path to the directory containing the files you want to serve (for example, public).

The server runs on Chrome, Edge and Firefox but image uploads only work on edge and firefox. Larger images are only possible on Firefox.

### Credentials for login page

The credentials are stored in /public/credentials.txt
by default they are:
Username = username
Password = password

### URL

The url for the login page is:
http://127.0.0.1:port/login.html

The URL for image uploads is:
http://127.0.0.1:port/upload.html

and the URL for redirection is:
http://127.0.0.1:port/systemd

### Test code 500, 301, 404

1. 302 Found:
    ◦ Trigger: Access /systemd.
    ◦ Expected Result: The server will respond with a "302 Found" status and redirect the client to "suckless.html".
2. 404 Not Found:
    ◦ Trigger: Request a non-existent page or resource from the server.
    ◦ Expected Result: The server will respond with a "404 Not Found" status, displaying a custom error page.
3. 500 Internal Server Error:
    ◦ Trigger: Upload an image from an unsupported browser.
    ◦ Expected Result: The server will respond with a "500 Internal Server Error" status, displaying a custom error message.
