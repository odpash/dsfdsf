package work.lab8FX.common.util.responses;

import work.lab8FX.common.abstractions.AbstractResponse;

public class ConnectionResponse extends AbstractResponse {
    public ConnectionResponse(boolean isSuccess, String message) {
        super(isSuccess, message);
    }
}
