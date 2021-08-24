package advisor.logic.web.requests;

import advisor.logic.Command;

public class Request {

    Command command;
    String parameter;

    public Request(Command command) {
        this(command, null);
    }

    public Request(Command command, String parameter) {
        this.command = command;
        this.parameter = parameter;
    }

    public Command getRequestType() {
        return command;
    }

    public String getParameter() {
        return parameter;
    }
}
