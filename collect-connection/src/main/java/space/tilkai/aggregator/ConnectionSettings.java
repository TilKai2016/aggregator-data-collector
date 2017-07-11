package space.tilkai.aggregator;

/**
 * @author tilkai
 */
public class ConnectionSettings {

    private int messageFragmentTimeout = 500000;

    // the maximum time in ms that no acknowledgement has been received
    public int maxTimeNoAckReceived = 15000000;
    // the maximum time in ms before confirming received messages that have not yet been acknowledged
    public int maxTimeNoAckSent = 100000;
    // the maximum time in ms that the connection may be idle before sending a test frame
    public int maxIdleTime = 200000;



    public ConnectionSettings getConnectionSettings() {

        ConnectionSettings settings = new ConnectionSettings();

        settings.messageFragmentTimeout = messageFragmentTimeout;

        return settings;
    }
}
