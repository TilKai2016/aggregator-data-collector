package space.tilkai.iaggregator;

import space.tilkai.aggregator.Connection;

import java.io.IOException;

/**
 * @author tilkai
 */
public interface ServerEventListener {

    void connectionIndication(Connection connection);

    void serverStoppedListeningIndication(IOException e);

    void connectionAttemptFailed(IOException e);
}
