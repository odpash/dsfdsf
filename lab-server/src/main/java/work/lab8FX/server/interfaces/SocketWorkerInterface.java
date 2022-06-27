package work.lab8FX.server.interfaces;

import work.lab8FX.common.abstractions.AbstractResponse;
import work.lab8FX.server.util.RequestWithAddress;

import java.io.IOException;
import java.net.SocketAddress;

public interface SocketWorkerInterface {
    RequestWithAddress listenForRequest() throws IOException, ClassNotFoundException;

    void sendResponse(AbstractResponse response, SocketAddress address) throws IOException;

    void stopSocketWorker() throws IOException;
}
