package work.lab8FX.server.clientcommands;

import work.lab8FX.common.exceptions.CollectionIsEmptyException;
import work.lab8FX.common.exceptions.DatabaseException;
import work.lab8FX.common.util.requests.CommandRequest;
import work.lab8FX.common.util.responses.CommandResponse;
import work.lab8FX.server.abstractions.AbstractClientCommand;
import work.lab8FX.server.db.DBManager;
import work.lab8FX.server.util.CollectionManager;

public class MinByStudioCommand extends AbstractClientCommand {

    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public MinByStudioCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("min_by_studio",
                0,
                "output any object from the collection whose studio field value is minimal");
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse executeClientCommand(CommandRequest request) {
        try {
            if (!dbManager.validateUser(request.getUsername(), request.getPassword())) {
                return new CommandResponse(false, "Login and password mismatch");
            }
            try {
                return new CommandResponse(true, "Minimal element:", collectionManager.returnMinByStudio());
            } catch (CollectionIsEmptyException e) {
                return new CommandResponse(false, e.getMessage());
            }
        } catch (DatabaseException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }
}
