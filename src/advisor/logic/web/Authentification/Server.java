package advisor.logic.web.Authentification;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    private static HttpServer authServer;


    public static void start() {
        try {
            authServer = HttpServer.create();
            authServer.bind(new InetSocketAddress(8080), 0);
            authServer.createContext("/",
                    new HttpHandler() {
                        @Override
                        public void handle(HttpExchange exchange) throws IOException {
                            String authCode = exchange.getRequestURI().getQuery();
                            Auth.receiveCode(authCode);
                            String message;

                            if (authCode == null || !authCode.matches("code=.+")) {
                                message = "Authorization code not found. Try again.";
                            } else {
                                message = "Got the code. Return back to your program.";
                            }
                            exchange.sendResponseHeaders(200, message.length());
                            exchange.getResponseBody().write(message.getBytes(StandardCharsets.UTF_8));
                            exchange.getResponseBody().close();
                            exchange.close();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        authServer.start();
    }

    public static void stop() {
        authServer.removeContext("/");
        authServer.stop(0);
    }

}
