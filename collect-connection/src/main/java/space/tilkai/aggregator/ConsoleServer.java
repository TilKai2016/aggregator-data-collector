package space.tilkai.aggregator;

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

    public class ServerListener implements ServerEventListener {

        public class ConnectionListener implements ConnectionEventListener {

            @Override
            public void newASdu() {

            }

            @Override
            public void connectionClosed(IOException e) {

            }
        }

        @Override
        public void connectionIndication(Connection connection) {

            // TODO: 2017/6/21 connection'num++, used to compare with the connection pool max count.

            // TODO: 2017/6/21 invoker connection.waitForStartDT()
            connection.waitForStartDT();

            // TODO: 2017/6/21 write logger
        }

        @Override
        public void serverStoppedListeningIndication(IOException e) {

        }

        @Override
        public void connectionAttemptFailed(IOException e) {

        }
    }

    public void start() {

        // TODO: 2017/6/21 build a Server and invoker the Server's start method

    }
}
