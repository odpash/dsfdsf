package work.lab8FX.client.controllers;

import work.lab8FX.client.exceptions.FieldsValidationException;
import work.lab8FX.client.models.MainModel;
import work.lab8FX.client.models.RemoveByIdModel;
import work.lab8FX.client.util.ClientSocketWorker;
import work.lab8FX.client.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RemoveByIdController extends AbstractController implements Initializable {

    private final RemoveByIdModel removeByIdModel;

    private final MainModel mainModel;
    @FXML
    private TextField idField;

    public RemoveByIdController(ClientSocketWorker clientSocketWorker, Session session, MainModel mainModel) {
        removeByIdModel = new RemoveByIdModel(clientSocketWorker, getCurrentStage(), session, this);
        this.mainModel = mainModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        addRegex(idField);
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    public void setField(Long id) {
        idField.setText(id.toString());
    }

    @FXML
    public void removeAction() {
        List<TextField> textFields = List.of(idField);
        removeFieldsColoring(textFields);
        try {
            removeByIdModel.processRemove(idField.getText());
            getCurrentStage().close();
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }
}
