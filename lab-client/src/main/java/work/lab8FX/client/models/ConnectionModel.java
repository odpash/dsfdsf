package work.lab8FX.client.models;

import work.lab8FX.client.controllers.ConnectionController;
import work.lab8FX.client.controllers.RegistrationController;
import work.lab8FX.client.exceptions.ExceptionWithAlert;
import work.lab8FX.client.exceptions.FieldsValidationException;
import work.lab8FX.client.exceptions.NoResponseException;
import work.lab8FX.client.util.ClientSocketWorker;
import work.lab8FX.client.util.PathToViews;
import work.lab8FX.client.util.validators.ConnectionValidator;
import work.lab8FX.common.abstractions.AbstractResponse;
import work.lab8FX.common.util.requests.ConnectionRequest;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Objects;

public class ConnectionModel extends AbstractModel {

    private final ConnectionController currentController;

    public ConnectionModel(ClientSocketWorker clientSocketWorker, Stage currentStage, ConnectionController currentController) {
        super(clientSocketWorker, currentStage);
        this.currentController = currentController;
    }


    public void connect(String address, String port) throws ExceptionWithAlert, FieldsValidationException {
        List<String> errorList = ConnectionValidator.validateConnection(address, port);
        if (errorList.stream().anyMatch(Objects::nonNull)) {
            throw new FieldsValidationException(errorList);
        }
        try {
            String currentAddress = ConnectionValidator.validateAddress(address);
            Integer currentPort = ConnectionValidator.validatePort(port);
            if (currentAddress != null) {
                getClientSocketWorker().setAddress(currentAddress);
            }
            if (currentPort != null) {
                getClientSocketWorker().setPort(currentPort);
            }
            AbstractResponse response = getClientSocketWorker().proceedTransaction(new ConnectionRequest(getClientInfo()));
            if (response.isSuccess()) {
                currentController.switchScene(PathToViews.REGISTRATION_VIEW, comp -> new RegistrationController(getClientSocketWorker()), currentController.getResourceBundle());
            }
        } catch (NoResponseException e) {
            throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.idMismatch"), true);
        } catch (SocketTimeoutException e) {
            throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.time"));
        } catch (IOException e) {
            throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.connection"));
        } catch (ClassNotFoundException e) {
            throw new ExceptionWithAlert(currentController.getResourceBundle().getString("connection_exception.response"));
        }
    }
}
