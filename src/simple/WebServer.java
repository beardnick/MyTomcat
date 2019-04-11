package simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private boolean shutdown = false;
    private String WEB_ROOT = "/webroot";

    private   int port = 80;

    private WebServer(){
    }

    public  String getWebRoot() {
        return WEB_ROOT;
    }

    public  int getPort() {
        return port;
    }

    public WebServer port(int port) {
        this.port = port;
        return this;
    }

    public WebServer webRoot(String path) {
        this.WEB_ROOT = path;
        return this;
    }



    public  static WebServer builder() {
        return new WebServer();
    }

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port,1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!shutdown) {
                try {
                Socket socket = serverSocket.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                Response response = new Response(this, output, request);
                response.sendResponse();
                socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
    }

    public void shutdown() {
        shutdown = true;
    }


    public static void main(String[] args) {
        WebServer webServer = WebServer.builder().port(8080);
        webServer.start();
    }
}
