package space.tilkai.iaggregator;

import space.tilkai.aggregator.Connection;

import java.io.IOException;

/**
 * @author tilkai
 */
public interface ServerEventListener {

    void connectionIndication(Connection connection);

    /**
     * invoker this method when serverSocket.accept() occur IOException.
     * @param e
     */
    void serverStoppedListeningIndication(IOException e);

    /**
     * invoker this method when create a instance of connection is fault.
     * @param e
     */
    void connectionAttemptFailed(IOException e);
}
