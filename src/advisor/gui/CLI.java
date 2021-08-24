package advisor.gui;

import advisor.logic.Command;
import advisor.logic.web.Authentification.Code;
import advisor.logic.web.requests.Request;

import java.util.Locale;
import java.util.Scanner;

public class CLI implements UI {
    Scanner scanner = new Scanner(System.in);


    @Override
    public String getInput() {
        String input = scanner.nextLine();
        return input;
    }

    @Override
    public Request getRequest() {
        String command = scanner.nextLine().trim();

        command = command.toLowerCase(Locale.ROOT);

        if (!command.contains(" ")) {
            switch (command) {
                case "new" :
                    return new Request(Command.NEW);
                case "featured" :
                    return new Request(Command.FEATURED);
                case "categories" :
                    return new Request(Command.CATEGORIES);
                case "auth" :
                    return new Request(Command.AUTH);
                case "exit" :
                    return new Request(Command.EXIT);
                case "prev" :
                    return new Request(Command.PREV);
                case "next" :
                    return new Request(Command.NEXT);
            }
        } else {
            String[] commands = command.split(" ");
            if ("playlists".equals(commands[0])) {
                return new Request(Command.PLAYLISTS, command.replaceFirst("playlists ", ""));
            }
        }

        return null;
    }

    @Override
    public void outputMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void receiveAuthLink(String message) {
        outputMessage("use this link to request the access code:\n" + message);
    }

    @Override
    public boolean outputPage(String[] pages, int pageID) {

        if (pages == null || pageID >= pages.length || pageID < 0) {
            outputMessage("No more pages.");
            return false;
        }
        System.out.print(pages[pageID]);
        outputMessage("---PAGE " + (pageID + 1) + " OF " + pages.length + "---");
        return true;
    }

    @Override
    public void receiveCode(Code code) {
        switch (code) {
            case CODE_RECEIVED:
                System.out.println("code received");
                break;
            case CODE_NOT_FOUND:
                System.out.println("Authorization code not found. Try again.");
                break;
            case CONNECTION_TIME_OUT:
                System.out.println("time out");
                break;
            case AUTH_SUCCESS:
                System.out.println("---SUCCESS---");
                break;
            case AUTH_DENIED:
                System.out.println("---FAILURE---");
                break;
            case UNAUTHORIZED:
                System.out.println("Please, provide access for application.");
        }
    }
}
