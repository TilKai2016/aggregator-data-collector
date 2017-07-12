package space.tilkai;

import space.tilkai.aggregator.Connection;
import space.tilkai.aggregator.Server;
import space.tilkai.iaggregator.ConnectionEventListener;
import space.tilkai.iaggregator.ServerEventListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ConsoleServer used to invoker Connection class by connectionIndication method.
 * And by start method create a socket to accept client's connect at last.
 *
 * @author tilkai
 */
public class ConsoleServer {

    private int connectionIdCounter = 1;

    public class ServerListener implements ServerEventListener {

        public class ConnectionListener implements ConnectionEventListener {

            private final Connection connection;
            private final int connectionId;

            public ConnectionListener(Connection connection, int connectionId) {
                this.connection = connection;
                this.connectionId = connectionId;
            }
            /**
             * Used this Method to process data.
             */
            @Override
            public void newASdu() {

            }

            @Override
            public void connectionClosed(IOException e) {
                System.out.println("Connection (" + connectionId + ") was closed. " + e.getMessage());
            }
        }

        @Override
        public void connectionIndication(Connection connection) {

            int thisConnectionId = connectionIdCounter++;

            System.out.println("A client has connected using TCP/IP. Will listen for a StartDT request. Connection ID: "
                    + thisConnectionId);

            try {
                connection.waitForStartDT(new ConnectionListener(connection, thisConnectionId), 5000);
            } catch (TimeoutException te) {

                System.out.println("connection.waitForStartDT has a error, connection id is " + thisConnectionId);
                te.printStackTrace();

            }

            System.out.println(
                    "Started data transfer on connection (" + thisConnectionId + ") Will listen for incoming commands.");
        }

        @Override
        public void serverStoppedListeningIndication(IOException e) {
            System.out.println(
                    "Server has stopped listening for new connections : \"" + e.getMessage() + "\". Will quit.");
        }

        /**
         *
         * @param e
         */
        @Override
        public void connectionAttemptFailed(IOException e) {

            System.out.println("Connection attempt failed: " + e.getMessage());

        }
    }

    /**
     * Create a Socket connection with ServerThread.
     */
    public void start() {

        Server server = new Server.Builder().build();

        try {
            server.start(new ServerListener());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
