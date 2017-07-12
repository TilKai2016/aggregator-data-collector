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

    private boolean stopServer = false;
    private int numConnections = 0;

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
            try {
                serverConnection = new Connection(socket, serverThread, settings);
            } catch (IOException e) {
                synchronized (ServerThread.this) {
                    numConnections--;
                }
                serverEventListener.connectionAttemptFailed(e); // e.printStackTrace();
                return;
            }

            serverEventListener.connectionIndication(serverConnection);
        }
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            Socket clientSocket = null;

            while (true) {
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {

                    if (stopServer == false) {
                        serverEventListener.serverStoppedListeningIndication(e);
                    }

                    e.printStackTrace();

                    return;
                }

                boolean startConnection = false;

                synchronized (this) {
                    if (numConnections < maxConnections) {
                        numConnections++;
                        startConnection = true;
                    }
                }

                if (startConnection) {
                    ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, this);
                    executor.execute(connectionHandler);
                }else {
                    serverEventListener.connectionAttemptFailed(new IOException("connection numbers is overflow maxCommections number..."));
                }
            }

        } finally {
            executor.shutdown();
        }
    }

    void connectionClosedSignal() {
        // TODO: 2017/6/30 what`s this.
        synchronized (this) {
            numConnections--;
        }
    }

    void stopServer() {
        stopServer = true;
        if (serverSocket.isBound()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
