import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.logging.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * a simple static http server
 */
public class SimpleHttpServer {

    static boolean LED1;
    static boolean LED2;

    public static void main(String[] args) throws Exception {
        LED1 = false;
        LED2 = false;

        HttpServer server = HttpServer.create(new InetSocketAddress(25565), 0);
        server.createContext("/test", new MyHandler());
        server.createContext("/b1", new HandlerB1());
        server.createContext("/b2", new HandlerB2());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestParamValue=null;
            if("GET".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handleGetRequest(httpExchange);
            }
            handleResponse(httpExchange,requestParamValue);
        }
        private String handleGetRequest(HttpExchange httpExchange) {
            return httpExchange.getRequestURI().toString();
        }
        private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            printHTML(httpExchange, requestParamValue, outputStream);
        }
    }

    static class HandlerB1 implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestParamValue=null;
            if("GET".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handleGetRequest(httpExchange);
            }
            handleResponse(httpExchange,requestParamValue);
        }
        private String handleGetRequest(HttpExchange httpExchange) {
            LED1 = !LED1;
            return httpExchange.getRequestURI().toString();
        }
        private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            printHTML(httpExchange, requestParamValue, outputStream);
        }
    }

    static class HandlerB2 implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestParamValue=null;
            if("GET".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handleGetRequest(httpExchange);
            }
            handleResponse(httpExchange,requestParamValue);
        }
        private String handleGetRequest(HttpExchange httpExchange) {
            LED2 = !LED2;
            return httpExchange.getRequestURI().toString();
        }
        private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {

            OutputStream outputStream = httpExchange.getResponseBody();
            printHTML(httpExchange, requestParamValue, outputStream);
        }
    }

    public static void printHTML(HttpExchange httpExchange, String requestParamValue, OutputStream outputStream) throws IOException{
        String L1 = "";
        String bL1 = "";
        if(LED1 == true){
            L1 = "ON";
            bL1 = "Apagar_LED1";
        }else {
            L1 = "OFF";
            bL1 = "Encender_LED1";
        }
        String L2 = "";
        String bL2 = "";
        if(LED2 == true){
            L2 = "ON";
            bL2 = "Apagar_LED2";
        } else {
            L2 = "OFF";
            bL2 = "Encender_LED2";
        }

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>").
                append("<body>").
                append("<h1>\n" +
                        "Estado de los LEDs\n" +
                        "</h1>").
                append("<form action=\"/b1\">\n" +
                        "  <label for=\"fname\">LED1:</label><br>" +
                        "  <label for=\"fname\">" + L1 + "</label><br>\n" +
                        "  <input type=\"submit\" value=" + bL1 + ">\n" +
                        "</form>").
                append("<form action=\"/b2\">\n"+
                        "  \n<label for=\"fname\">LED2:</label><br>" +
                        "  <label for=\"fname\">" + L2 +"</label><br>\n" +
                        "  <input type=\"submit\" value=" + bL2 + ">\n" +
                        "</form>").
                append("</body>").
                append("</html>");
        // encode HTML content
        String htmlResponse = htmlBuilder.toString();
        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
