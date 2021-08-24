package advisor.logic.web.requests;

import advisor.logic.web.Authentification.Client;
import advisor.logic.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class Requests {

    public static String getNewReleases() {

        HttpRequest request = getNewRequest("/v1/browse/new-releases");

        HttpResponse<String> clientResponse = Client.sendRequest(request);

        if (clientResponse == null || clientResponse.statusCode() != 200) {
            return "Something went wrong";
        }

        JsonArray response = JsonParser.parseString(clientResponse.body()).getAsJsonObject()
                .get("albums").getAsJsonObject()
                .getAsJsonArray("items");

        StringBuilder stringBuilder = new StringBuilder();

        JsonObject songInfo;
        JsonArray artists;

        for (int i = 0; i < response.size(); i++) {
            songInfo = response.get(i).getAsJsonObject();
            stringBuilder.append(songInfo.get("name").getAsString())
                    .append("\n");
            artists = songInfo.getAsJsonArray("artists");

            stringBuilder.append("[");

            for (int j = 0; j < artists.size(); j++) {
                stringBuilder.append(artists.get(j).getAsJsonObject().get("name").getAsString())
                        .append(", ");
            }

            stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(" ") + 1, "]\n");

            stringBuilder.append(songInfo.getAsJsonObject("external_urls").get("spotify").getAsString())
                    .append("\n\n");

            if ((i + 1) % Config.getPageSize() == 0) {
                stringBuilder.append("%&%");
            }
        }

        stringBuilder.delete(stringBuilder.lastIndexOf("\n"), stringBuilder.lastIndexOf("\n") + 1);

        return stringBuilder.toString();
    }

    public static String getFeatured() {
        HttpRequest request = getNewRequest("/v1/browse/featured-playlists");

        HttpResponse<String> clientResponse = Client.sendRequest(request);

        if (clientResponse == null) {
            return "Something went wrong";
        }

        StringBuilder stringBuilder = new StringBuilder();

        JsonArray featured = JsonParser.parseString(clientResponse.body())
                .getAsJsonObject().get("playlists")
                .getAsJsonObject().get("items")
                .getAsJsonArray();

        for (int i = 0; i < featured.size(); i++) {
            stringBuilder.append(featured
                            .get(i)
                            .getAsJsonObject()
                            .get("name")
                            .getAsString()).append("\n");

            stringBuilder.append(featured
                            .get(i)
                            .getAsJsonObject()
                            .get("external_urls")
                            .getAsJsonObject()
                            .get("spotify")
                            .getAsString()).append("\n\n");
            if ((i + 1) % Config.getPageSize() == 0) {
                stringBuilder.append("%&%");
            }
        }
        stringBuilder.delete(stringBuilder.lastIndexOf("\n"), stringBuilder.lastIndexOf("\n") + 1);

        return stringBuilder.toString();
    }

    public static String getCategories() {
        HttpRequest request = getNewRequest("/v1/browse/categories");

        HttpResponse<String> clientResponse = Client.sendRequest(request);

        if (clientResponse == null) {
            return "Something went wrong";
        }

        JsonArray categories = JsonParser.parseString(clientResponse.body())
                .getAsJsonObject()
                .get("categories")
                .getAsJsonObject()
                .get("items")
                .getAsJsonArray();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < categories.size(); i++) {
            stringBuilder.append(categories.get(i)
                    .getAsJsonObject()
                    .get("name")
                    .getAsString()).append("\n");

            if ((i + 1) % Config.getPageSize() == 0) {
                stringBuilder.append("%&%");
            }
        }

        return stringBuilder.toString();
    }

    public static String getPlaylists(String name) {
        HttpRequest request = getNewRequest("/v1/browse/categories");

        HttpResponse<String> clientResponse = Client.sendRequest(request);

        if (clientResponse == null) {
            return "Something went wrong";
        }

        JsonArray categories = JsonParser.parseString(clientResponse.body())
                .getAsJsonObject()
                .get("categories")
                .getAsJsonObject()
                .get("items")
                .getAsJsonArray();

        String id = null;

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i)
                    .getAsJsonObject()
                    .get("name")
                    .getAsString()
                    .toLowerCase(Locale.ROOT)
                    .equals(name)) {
                id = categories.get(i).getAsJsonObject().get("id").getAsString();
            }
        }

        if (id == null) {
            return "Unknown category name.";
        }

        request = getNewRequest("/v1/browse/categories/" + id + "/playlists");

        clientResponse = Client.sendRequest(request);

        if (clientResponse == null) {
            return "something went wrong";
        }

        try {

            JsonArray items = JsonParser.parseString(clientResponse.body())
                    .getAsJsonObject().get("playlists")
                    .getAsJsonObject().get("items")
                    .getAsJsonArray();

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < items.size(); i++) {
                stringBuilder.append(items.get(i)
                        .getAsJsonObject()
                        .get("name")
                        .getAsString())
                        .append("\n");

                stringBuilder.append(items
                        .get(i)
                        .getAsJsonObject()
                        .get("external_urls")
                        .getAsJsonObject()
                        .get("spotify")
                        .getAsString())
                        .append("\n\n");

                if ((i + 1) % Config.getPageSize() == 0) {
                    stringBuilder.append("%&%");
                }
            }

            stringBuilder.delete(stringBuilder.lastIndexOf("\n"), stringBuilder.lastIndexOf("\n") + 1);

            return stringBuilder.toString();
        } catch (Exception e) {
            return "Error: " + clientResponse.body();
        }
    }

    private static HttpRequest getNewRequest(String uri) {

        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer "
                        + Config.getAccessToken())
                .uri(URI.create(Config.getResourceServer() + uri))
                .GET()
                .build();
    }
}
