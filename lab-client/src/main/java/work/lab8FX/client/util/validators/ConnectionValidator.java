package work.lab8FX.client.util.validators;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public final class ConnectionValidator {

    private ConnectionValidator() {
    }

    public static String validateAddress(String address) throws IllegalArgumentException {
        if ("".equals(address)) {
            return null;
        }
        try {
            InetAddress.getByName(address);
            return address;
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("connection_exception.wrong_address");
        }
    }

    public static Integer validatePort(String port) throws IllegalArgumentException {
        if ("".equals(port)) {
            return null;
        }
        int portNum = -1;
        try {
            portNum = Integer.parseInt(port);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("connection_exception.port_number");
        }
        if (portNum <= 0 || portNum > 65535) {
            throw new IllegalArgumentException("connection_exception.port_range");
        }
        return portNum;
    }

    public static List<String> validateConnection(String address, String port) {
        List<String> errorList = new ArrayList<>();
        try {
            validateAddress(address);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }
        try {
            validatePort(port);
            errorList.add(null);
        } catch (IllegalArgumentException e) {
            errorList.add(e.getMessage());
        }
        return errorList;
    }
}
