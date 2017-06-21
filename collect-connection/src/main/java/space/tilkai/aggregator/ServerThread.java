package space.tilkai.aggregator;

import space.tilkai.iaggregator.ServerEventListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Following SpringBootApplication starting,
 * as a thread to be listened the connect from aggregator.
 * @author tilkai
 */
public class ServerThread extends Thread {

    private final ServerSocket serverSocket;
    private final ConnectionSettings settings;
    private final int maxConnections;
    private final ServerEventListener serverEventListener;

    public ServerThread(ServerSocket serverSocket, ConnectionSettings settings,
                        int maxConnections, ServerEventListener listener) {
        this.serverSocket = serverSocket;
        this.settings = settings;
        this.maxConnections = maxConnections;
        this.serverEventListener = listener;
    }

    private class ConnectionHandler extends Thread {

        private final Socket socket;
        private final ServerThread serverThread;

        private ConnectionHandler(Socket socket, ServerThread serverThread) {
            this.socket = socket;
            this.serverThread = serverThread;
        }

        @Override
        public void run() {

            Connection serverConnection;
            serverConnection = new Connection(socket, serverThread, settings);

            serverEventListener.connectionIndication(serverConnection);
        }
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Socket clientSocket = null;

        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO: 2017/6/21 连接数判断，超出处理

            ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, this);
            executorService.execute(connectionHandler);
        }
    }
}
