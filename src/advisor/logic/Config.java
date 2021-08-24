package advisor.logic;

public class Config {
    private static String accessServer = "https://accounts.spotify.com/api/token";
    private static String accessToken;
    private static String resourceServer = "https://api.spotify.com";
    private static int pageSize = 5;
    private static final String CLIENT_ID = "PASTE YOUR ID HERE";
    private static final String CLIENT_SECRET = "PASTE YOUR SECRET HERE";

    public static String getClientId() {
        return CLIENT_ID;
    }

    public static String getClientSecret() {
        return CLIENT_SECRET;
    }

    public static void setResourceServer(String resourceServer) {
        Config.resourceServer = resourceServer;
    }

    public static String getResourceServer() {
        return resourceServer;
    }

    public static void setAccessServer(String accessServer) {
        Config.accessServer = accessServer + "/api/token";
    }

    public static String getAccessServer() {
        return accessServer;
    }

    public static void setAccessToken(String accessToken) {
        Config.accessToken = accessToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getLink() {
        return "https://accounts.spotify.com/authorize?client_id=" + CLIENT_ID + "&redirect_uri=http://localhost:8080&response_type=code";
    }

    public static void setPageSize(int pageSize) {
        Config.pageSize = pageSize;
    }

    public static int getPageSize() {
        return Config.pageSize;
    }
}
