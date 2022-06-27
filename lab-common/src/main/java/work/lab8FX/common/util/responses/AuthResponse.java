package work.lab8FX.common.util.responses;

import work.lab8FX.common.abstractions.AbstractResponse;

public class AuthResponse extends AbstractResponse {

    public AuthResponse(boolean isSuccess, String message) {
        super(isSuccess, message);
    }
}
