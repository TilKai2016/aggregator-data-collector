package space.tilkai.aggregator;

/**
 * @author tilkai
 */
class CommonBuilder<T extends CommonBuilder<T>> {

    final ConnectionSettings settings = new ConnectionSettings();

    /**
     * Access the casted this reference.
     *
     * @return the reference of the object.
     */
    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }

    /**
     * Sets the maximum time in ms that no acknowledgement has been received (for I-Frames or Test-Frames) before
     * actively closing the connection. This timeout is called t1 by the standard. Default is 15s, minimum is 1s,
     * maximum is 255s.
     *
     * @param time
     *            the maximum time in ms that no acknowledgement has been received before actively closing the
     *            connection.
     * @return this builder
     */
    public T setMaxTimeNoAckReceived(int time) {
        if (time < 1000 || time > 255000) {
            throw new IllegalArgumentException(
                    "invalid timeout: " + time + ", time must be between 1000ms and 255000ms");
        }
        settings.maxTimeNoAckReceived = time;
        return self();
    }

    /**
     * Sets the maximum time in ms before confirming received messages that have not yet been acknowledged using an S
     * format APDU. This timeout is called t2 by the standard. Default is 10s, minimum is 1s, maximum is 255s.
     *
     * @param time
     *            the maximum time in ms before confirming received messages that have not yet been acknowledged using
     *            an S format APDU.
     * @return this builder
     */
    public T setMaxTimeNoAckSent(int time) {
        if (time < 1000 || time > 255000) {
            throw new IllegalArgumentException(
                    "invalid timeout: " + time + ", time must be between 1000ms and 255000ms");
        }
        settings.maxTimeNoAckSent = time;
        return self();
    }

    /**
     * Sets the maximum time in ms that the connection may be idle before sending a test frame. This timeout is called
     * t3 by the standard. Default is 20s, minimum is 1s, maximum is 172800s (48h).
     *
     * @param time
     *            the maximum time in ms that the connection may be idle before sending a test frame.
     * @return this builder
     */
    public T setMaxIdleTime(int time) {
        if (time < 1000 || time > 172800000) {
            throw new IllegalArgumentException(
                    "invalid timeout: " + time + ", time must be between 1000ms and 172800000ms");
        }
        settings.maxIdleTime = time;
        return self();
    }

}
