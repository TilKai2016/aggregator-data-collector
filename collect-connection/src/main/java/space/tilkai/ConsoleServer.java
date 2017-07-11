package space.tilkai;

import space.tilkai.aggregator.Connection;
import space.tilkai.aggregator.Server;
import space.tilkai.iaggregator.ConnectionEventListener;
import space.tilkai.iaggregator.ServerEventListener;

import java.io.IOException;

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
             * By used this Method you can process input data stream.
             */
            @Override
            public void newASdu() {

            }

            @Override
            public void connectionClosed(IOException e) {

            }
        }

        @Override
        public void connectionIndication(Connection connection) {

            int thisConnectionId = connectionIdCounter++;
            // TODO: 2017/6/21 connection'num++, used to compare with the connection pool max count.

            // TODO: 2017/6/21 invoker connection.waitForStartDT()
            connection.waitForStartDT(new ConnectionListener(connection, thisConnectionId), 50000);

            // TODO: 2017/6/21 write logger
        }

        @Override
        public void serverStoppedListeningIndication(IOException e) {

        }

        /**
         *
         * @param e
         */
        @Override
        public void connectionAttemptFailed(IOException e) {

            // TODO: 2017/6/30 log4j
            System.err.println("...");

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
