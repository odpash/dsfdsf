package work.lab8FX.server.clientcommands;

import work.lab8FX.common.exceptions.DatabaseException;
import work.lab8FX.common.util.requests.CommandRequest;
import work.lab8FX.common.util.responses.CommandResponse;
import work.lab8FX.server.abstractions.AbstractClientCommand;
import work.lab8FX.server.db.DBManager;
import work.lab8FX.server.util.CollectionManager;

import java.util.List;

public class ShowCommand extends AbstractClientCommand {

    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public ShowCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("show",
                0,
                "display all the elements of the collection and information about them");
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse executeClientCommand(CommandRequest request) {
        try {
            if (!dbManager.validateUser(request.getUsername(), request.getPassword())) {
                return new CommandResponse(false, "Login and password mismatch");
            }
            if (collectionManager.getMusicBands().isEmpty()) {
                return new CommandResponse(false, "Collection is empty");
            } else {
                List<Long> ids = dbManager.getIdsOfUsersElements(request.getUsername());
                return new CommandResponse(true, "Elements of collection:",
                        collectionManager.getUsersElements(ids),
                        collectionManager.getAlienElements(ids));
            }
        } catch (DatabaseException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }
}
