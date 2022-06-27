package work.lab8FX.client.controllers;

import work.lab8FX.client.exceptions.FieldsValidationException;
import work.lab8FX.client.models.CountModel;
import work.lab8FX.client.models.MainModel;
import work.lab8FX.client.util.ClientSocketWorker;
import work.lab8FX.client.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CountController extends AbstractController implements Initializable {

    private final CountModel countModel;
    private final MainModel mainModel;
    @FXML
    public TextField numberField;

    public CountController(ClientSocketWorker clientSocketWorker, Session session, MainModel mainModel) {
        countModel = new CountModel(clientSocketWorker, getCurrentStage(), session, this);
        this.mainModel = mainModel;
    }

    public MainModel getMainModel() {
        return mainModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setResourceBundle(resources);
        addRegex(numberField);
    }

    public void setField(Long number) {
        numberField.setText(number.toString());
    }

    @FXML
    public void countAction() {
        List<TextField> textFields = List.of(numberField);
        removeFieldsColoring(textFields);
        try {
            countModel.processCount(numberField.getText());
        } catch (FieldsValidationException e) {
            showFieldsErrors(e.getErrorList(), textFields);
        }
    }
}
