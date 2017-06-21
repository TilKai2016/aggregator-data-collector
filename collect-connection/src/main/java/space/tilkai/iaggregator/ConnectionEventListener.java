package space.tilkai.iaggregator;

import java.io.IOException;

/**
 * ConnectionEventListener used to accept the ASdu(So implement class can operate the ASdu.)
 * And also can close the eventListener interface.
 *
 * @author tilkai
 */
public interface ConnectionEventListener {

    void newASdu();

    void connectionClosed(IOException e);
}
