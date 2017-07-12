package space.tilkai.aggregator;

/**
 * @author tilkai
 */
public class ConnectionSettings {

    private int messageFragmentTimeout = 5000;

    // the maximum time in ms that no acknowledgement has been received
    public int maxTimeNoAckReceived = 15000;
    // the maximum time in ms before confirming received messages that have not yet been acknowledged
    public int maxTimeNoAckSent = 10000;
    // the maximum time in ms that the connection may be idle before sending a test frame
    public int maxIdleTime = 20000;

    public int maxUnconfirmedIPdusReceived = 8;



    public ConnectionSettings getConnectionSettings() {

        ConnectionSettings settings = new ConnectionSettings();

        settings.messageFragmentTimeout = messageFragmentTimeout;

        settings.maxTimeNoAckReceived = maxTimeNoAckReceived;
        settings.maxTimeNoAckSent = maxTimeNoAckSent;
        settings.maxIdleTime = maxIdleTime;

        settings.maxUnconfirmedIPdusReceived = maxUnconfirmedIPdusReceived;

        return settings;
    }
}
