package simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jdk.nashorn.internal.runtime.RewriteException;

public class Response {

    private WebServer context;
    private OutputStream outputStream;
    private Request request;

    public Response(WebServer context, OutputStream out, Request request) {
        this.context = context;
        this.outputStream = out;
        this.request = request;
    }

    public Response(OutputStream outputStream, Request request) {
        this.outputStream = outputStream;
        this.request = request;
    }

    public void sendResponse() throws IOException {
        InputStream in = getClass().getResourceAsStream(context.getWebRoot() + request.getUri());
        if(in == null){
            System.out.println("file not found");
            String html = "<h1>File not Found</h1>";
            String error = "HTTP /1.1 404 File Not Found\n" +
                    "Content-Type: text/html\n" +
                    "Content-Length: " + html.length()  +
                    "\n\n" +
                    html;
            System.out.println(error);
            outputStream.write(error.getBytes());
            outputStream.close();
            return;
        }
        String success = "HTTP /1.1 200 File Not Found\n";
        if (request.getUri().endsWith("jpeg")) {
            success += "Content-Type: image/jpeg\n\n" ;
        } else if (request.getUri().endsWith("html")) {
            success += "Content-Type: text/html\n\n" ;
        }
        outputStream.write(success.getBytes());
        byte[] buffer = new byte[1024];
        int cnt = -1;
        while ((cnt = in.read(buffer)) != -1) {
            outputStream.write(buffer,0, cnt);
        }
        in.close();
    }


    public static void main(String[] args) {
//        Response response = new Response();
    }
}
