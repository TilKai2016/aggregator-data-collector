package space.tilkai.aggregator;

import space.tilkai.iaggregator.ServerEventListener;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @author tilkai
 */
public class Server {

    private ServerThread serverThread;

    private final ServerSocketFactory serverSocketFactory;
    private final int maxConnections;
    private final int port;
    private final InetAddress inetAddress;
    private final int backlog;
    private final ConnectionSettings settings;

    /**
     * Inner class {@code Server.Builder} used to create a instance of Server.
     */
    public static class Builder extends CommonBuilder<Builder> {
        // TODO: 2017/6/21 whether or not to extends a basic class.

        private ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
        private int port = 9070;
        private InetAddress inetAddress;
        private int backlog = 0;
        private int maxConnections = 100;

        /**
         * Sets the ServerSocketFactory to be used to create the ServerSocket. Default is ServerSocketFactory.getDefault()
         * @param serverSocketFactory
         * @return Builder
         */
        public Builder setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
            this.serverSocketFactory = serverSocketFactory;
            return this;
        }

        /**
         * Sets the tcp port that the server will listen on.
         * Default tsingcon aggregator protocol communication port is 9070.
         *
         * @param port
         * @return Builder
         */
        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Sets the IP address to bind to. It is passed to {@link java.net.ServerSocket}
         *
         * @param inetAddress
         * @return Builder
         */
        public Builder setInetAddress(InetAddress inetAddress) {
            this.inetAddress = inetAddress;
            return this;
        }

        /**
         * Sets the backlog that is passed to the {@link java.net.ServerSocket}
         * About why the socket use a backlog, see {http://blog.csdn.net/huang_xw/article/details/7338487}
         *
         * @param backlog
         * @return
         */
        public Builder setBacklog(int backlog) {
            this.backlog = backlog;
            return this;
        }

        /**
         * Sets the maximum number of client connections that are allowed in parallel.
         *
         * @param maxConnections
         * @return Builder
         */
        public Builder setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        /**
         * by default, you can direct use this method.
         * or you can invoker setter method first, to meet your custom demand.
         * @return
         */
        public Server build() {
            return new Server(this);
        }

    }

    private Server(Builder builder) {
        serverSocketFactory = builder.serverSocketFactory;
        maxConnections = builder.maxConnections;
        port = builder.port;
        inetAddress = builder.inetAddress;
        backlog = builder.backlog;
        settings = builder.settings.getConnectionSettings();
    }

    public void start(ServerEventListener listener) throws IOException{
        serverThread = new ServerThread(serverSocketFactory.createServerSocket(port, backlog, inetAddress),
                settings, maxConnections, listener);
        serverThread.start();
    }

    public void stop() {
        serverThread.stopServer();
        serverThread = null;
    }
}
