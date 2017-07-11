package space.tilkai.aggregator;

import space.tilkai.iaggregator.ConnectionEventListener;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author tilkai
 */
public class Connection {

    private CountDownLatch startdtactSignal;
    private ConnectionEventListener aSduListener;

//    private final DataOutputStream os;
//    private final DataInputStream is;
    private final InputStream is;
    private final OutputStream os;

    private final Socket socket;

    private final ConnectionSettings settings;
    private final ServerThread serverThread;

    private final ScheduledExecutorService maxIdleTimeTimer = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> maxIdleTimeTimerFuture = null;
    private boolean DataTransferStarted = false;

    // TODO: 2017/6/30 what`s todu .
    /**
     * 一般被{@link ClientConnectionBuilder}实例创建连接时调用
     *
     * @param socket
     * @param serverThread
     * @param settings
     * @throws IOException
     */
    Connection(Socket socket, ServerThread serverThread, ConnectionSettings settings) throws IOException {
        try {
            os = socket.getOutputStream();
        } catch (IOException e) {
            socket.close();
            throw e;
        }
        try {
            is = socket.getInputStream();
        } catch (IOException e) {
            try {
                // this will also close the socket
                os.close();
            } catch (Exception e1) {
            }
            throw e;
        }

        this.socket = socket;
        this.settings = settings;
        this.serverThread = serverThread;

        ConnectionReader connectionReader = new ConnectionReader();
        connectionReader.start();

    }

    private class ConnectionReader extends Thread {

        @Override
        public void run() {

            try {
                while (true) {
                    socket.setSoTimeout(0);

                    // TODO: 2017/7/4 parse the aSdu.
                    try {

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                        String parseStr;
                        while ((parseStr = reader.readLine()) != null) {



                            parseData(parseStr);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void parseData(String parseStr) {
        switch (parseStr.length()) {
            case 1:
                break;
            default:
                String[] strArray = parseStr.split(";");

                String deviceId = strArray[0];
                String freq = strArray[1];

                String[] devices = strArray[3].replace("s", "").split(":");

                if (Integer.parseInt(devices[1]) == 1) {
                    // TODO: 2017/7/4 读取配置解析
                } else {
                    // TODO: 2017/7/4 从机离线的处理

                }
        }
    }
    /**
     * to be invoked by connectionIndication method.
     * @param listener The listener tobe notice while ASdu arrived.
     * @param timeout
     */
    public void waitForStartDT(ConnectionEventListener listener, int timeout) {

        if (timeout == 0) {
            try {
                startdtactSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (timeout > 0) {
            boolean success = true;
            try {
                success = startdtactSignal.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!success) {
                // TODO: 2017/7/3 throw...
            }
        } else {
            throw new IllegalArgumentException("Error : Timeout may not be negative.");
        }

        synchronized (this) {
            this.aSduListener = listener;
            DataTransferStarted = true;
        }

//        resetMaxIdleTimeTimer();
    }

    private void resetMaxIdleTimeTimer() {
        if (maxIdleTimeTimerFuture != null) {
            maxIdleTimeTimerFuture.cancel(true);
        }

        maxIdleTimeTimerFuture = maxIdleTimeTimer.schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (Connection.this) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    maxIdleTimeTimerFuture = null;
//                    scheduleMaxTime
                }
            }
        }, settings.maxTimeNoAckReceived, TimeUnit.MILLISECONDS);
    }

    private void scheduleMaxTimeNoTestConReceivedFuture() {

    }
}
