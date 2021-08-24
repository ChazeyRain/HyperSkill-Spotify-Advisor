package advisor.gui;

import advisor.logic.web.Authentification.Code;
import advisor.logic.web.requests.Request;

public interface UI {
    String getInput();
    Request getRequest();

    void outputMessage(String message);
    boolean outputPage(String[] pages, int pageID);
    void receiveCode(Code code);
    void receiveAuthLink(String message);
}
