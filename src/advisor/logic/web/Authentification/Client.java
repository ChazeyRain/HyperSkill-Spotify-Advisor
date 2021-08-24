package advisor.logic.web.Authentification;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Client {

    private static HttpClient client = HttpClient.newBuilder().build();

    public static HttpResponse<String> sendRequest(HttpRequest request) {

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
