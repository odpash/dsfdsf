package work.lab8FX.server.servercommands;


import work.lab8FX.common.util.TextColoring;
import work.lab8FX.server.ServerConfig;
import work.lab8FX.server.abstractions.AbstractServerCommand;

public class ServerExitCommand extends AbstractServerCommand {

    public ServerExitCommand() {
        super("exit", "shut downs the server");
    }

    @Override
    public String executeServerCommand() {
        ServerConfig.toggleRun();
        return TextColoring.getGreenText("Server shutdown");
    }
}
