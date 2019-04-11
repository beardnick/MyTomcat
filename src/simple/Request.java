package simple;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private InputStream input;

    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 从流中解析出http请求的内容
     */
    public void parse() {
        StringBuilder request = new StringBuilder();
//        byte[] buffer = new byte[1024];
//        要使用大一点的缓冲区
        byte[] buffer = new byte[2048];
        try {
            int j = -1;
            j = input.read(buffer);
//            http请求中不包含结束符号，如果使用while循环的话循环不会退出
//            while (( j = input.read(buffer)) != -1) {
                for (int i = 0; i <j; i++) {
                    request.append((char) buffer[i]);
                }
                System.out.println(request);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(request.toString());
        uri = parseUri(request.toString());
    }

    private String parseUri(String request) {
//        根据http协议，第一个空格和第二个空格之间就是请求的uri
        int begin = request.indexOf(' ');
        if (begin != -1) {
            int end = request.indexOf(' ', begin + 1);
            if (begin < end) {
                return request.substring(begin + 1, end);
            }
        }
        return  null;
    }

    public static void main(String[] args) {
        InputStream inputStream = Request.class.getResourceAsStream("/simple/test.c");
        if (inputStream != null) {
            Request request = new Request(inputStream);
            request.parse();
            String uri = request.parseUri("GET /test/uri HTTP/1.1\n" +
                    "Host: www.baidu.com\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                    "Referer: https://www.baidu.com/\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: en,zh-CN;q=0.9,zh;q=0.8\n");
            System.out.println("uri:" + uri);
        }
    }

    public String getUri() {
        return uri;
    }

}
