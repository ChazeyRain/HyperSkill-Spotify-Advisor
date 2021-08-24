package advisor.logic.web.Authentification;

import advisor.logic.Config;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Auth {
    private static boolean authorized = false;
    private static String authCode;
    private static boolean codeReceived;

    public static boolean getAuthorizationStatus() {
        return authorized;
    }

    public static Code requestAuthCode() {
        codeReceived = false;


        for (int i = 1; i < 300; i++) {
            pause(200);
            if (codeReceived) {
                break;
            }
        }

        if (!codeReceived) {
            return Code.CONNECTION_TIME_OUT;
        }

        if (authCode == null || !authCode.matches("code=.+")) {
            return Code.CODE_NOT_FOUND;
        }

        return Code.CODE_RECEIVED;
    }

    public static Code requestAccessToken() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.getAccessServer()))
                .header("Authorization", "Basic " + Base64.getEncoder()
                        .encodeToString((Config.getClientId() + ":" + Config.getClientSecret()).getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(
                        "redirect_uri=http://localhost:8080" + "&grant_type=authorization_code&" + authCode
                ))
                .build();

        HttpResponse<String> response = Client.sendRequest(request);

        if (response == null) {
            return Code.AUTH_DENIED;
        }

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Config.setAccessToken(jsonObject.get("access_token").getAsString());

        authorized = true;
        return Code.AUTH_SUCCESS;
    }

    static void receiveCode(String code) {
        authCode = code;
        codeReceived = true;
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
