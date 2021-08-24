package advisor.logic;

import advisor.gui.CLI;
import advisor.gui.UI;
import advisor.logic.web.Authentification.Code;
import advisor.logic.web.Authentification.Auth;
import advisor.logic.web.Authentification.Server;
import advisor.logic.web.requests.Request;
import advisor.logic.web.requests.Requests;

public class RunTime {

    private static boolean running = true;

    private static UI ui = new CLI();

    private static String[] currentRequest = null;

    private static int currentPage = 0;

    public static void start() {

        while (running) {
            sendRequest(ui.getRequest());
        }

        System.out.println("---GOODBYE!---");
    }

    private static void sendRequest(Request request) {
        if (request != null) {
            switch (request.getRequestType()) {
                case EXIT:
                    running = false;
                    return;
                case AUTH:
                    Server.start();
                    Code code;
                    ui.receiveAuthLink(Config.getLink());
                    ui.outputMessage("waiting for code...");

                    code = Auth.requestAuthCode();
                    ui.receiveCode(code);

                    if (code == Code.CODE_RECEIVED) {
                        ui.outputMessage("making http request for access_token...");
                        code = Auth.requestAccessToken();

                        ui.receiveCode(code);
                    }
                    Server.stop();
                    return;
            }
            if (Auth.getAuthorizationStatus()) {
                switch (request.getRequestType()) {
                    case NEW:
                        //ui.receiveMessage(Requests.getNewReleases());
                        currentRequest = Requests.getNewReleases().split("%&%");
                        currentPage = 0;
                        ui.outputPage(currentRequest, currentPage);
                        return;
                    case FEATURED:
                        //ui.receiveMessage(Requests.getFeatured());
                        currentRequest = Requests.getFeatured().split("%&%");
                        currentPage = 0;
                        ui.outputPage(currentRequest, currentPage);
                        return;
                    case PLAYLISTS:
                        currentRequest = Requests.getPlaylists(request.getParameter()).split("%&%");
                        currentPage = 0;
                        ui.outputPage(currentRequest, currentPage);
                        //ui.receiveMessage(Requests.getPlaylists(request.getParameter()));
                        return;
                    case CATEGORIES:
                        currentRequest = Requests.getCategories().split("%&%");
                        currentPage = 0;
                        ui.outputPage(currentRequest, currentPage);
                        //ui.receiveMessage(Requests.getCategories());
                        return;
                    case NEXT:
                        if (ui.outputPage(currentRequest, currentPage + 1)) {
                            currentPage++;
                        }
                        return;
                    case PREV:
                        if (ui.outputPage(currentRequest, currentPage - 1)) {
                            currentPage--;
                        }
                        return;

                }
            } else {
                ui.receiveCode(Code.UNAUTHORIZED);
            }
        }
    }
}
