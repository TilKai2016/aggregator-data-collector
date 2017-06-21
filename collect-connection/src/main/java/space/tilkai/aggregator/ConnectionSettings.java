package space.tilkai.aggregator;

/**
 * @author tilkai
 */
public class ConnectionSettings {

    private int messageFragmentTimeout = 500000;

    public ConnectionSettings getConnectionSettings() {

        ConnectionSettings settings = new ConnectionSettings();

        settings.messageFragmentTimeout = messageFragmentTimeout;

        return settings;
    }
}
