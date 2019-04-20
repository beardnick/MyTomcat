package simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                WebServer context = this;
//                multiThread(serverSocket.accept(), context);
                singleThread(serverSocket.accept(), context);
//                    threadPool(serverSocket.accept(), context);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
    }

    private  final ExecutorService executor = Executors.newFixedThreadPool(100);

    private void threadPool(Socket socket, WebServer context) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream input = socket.getInputStream();
                    OutputStream output = socket.getOutputStream();
                    Request request = new Request(input);
                    request.parse();
                    Response response = new Response(context , output, request);
                    response.sendResponse();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void multiThread(Socket socket, WebServer context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream input = socket.getInputStream();
                    OutputStream output = socket.getOutputStream();
                    Request request = new Request(input);
                    request.parse();
                    Response response = new Response(context , output, request);
                    response.sendResponse();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void singleThread(Socket socket, WebServer context) throws IOException {
        InputStream input = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        Request request = new Request(input);
        request.parse();
        Response response = new Response(context , output, request);
        response.sendResponse();
        socket.close();
    }

    public void shutdown() {
        shutdown = true;
    }


    public static void main(String[] args) {
        WebServer webServer = WebServer.builder().port(8080);
        webServer.start();
    }
}
