package work.lab8FX.server.clientcommands;

import work.lab8FX.common.entities.MusicBand;
import work.lab8FX.common.exceptions.DatabaseException;
import work.lab8FX.common.util.requests.CommandRequest;
import work.lab8FX.common.util.responses.CommandResponse;
import work.lab8FX.server.abstractions.AbstractClientCommand;
import work.lab8FX.server.db.DBManager;
import work.lab8FX.server.util.CollectionManager;

public class AddCommand extends AbstractClientCommand {

    private final DBManager dbManager;
    private final CollectionManager collectionManager;

    public AddCommand(DBManager dbManager, CollectionManager collectionManager) {
        super("add",
                0,
                "add a new item to the collection");
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse executeClientCommand(CommandRequest request) {
        try {
            if (!dbManager.validateUser(request.getUsername(), request.getPassword())) {
                return new CommandResponse(false, "Login and password mismatch");
            }
            MusicBand bandToAdd = request.getBandArgument();
            Long id = dbManager.addElement(bandToAdd, request.getUsername());
            bandToAdd.setId(id);
            collectionManager.addMusicBand(bandToAdd);
            return new CommandResponse(true, "Element was successfully added to collection with ID: "
                    + id);
        } catch (DatabaseException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }
}
