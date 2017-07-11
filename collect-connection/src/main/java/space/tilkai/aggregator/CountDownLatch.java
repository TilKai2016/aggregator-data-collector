package space.tilkai.aggregator;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 允许一个或多个线程等待直到在其他线程中执行的一组操作完成的同步辅助类。
 *
 * {@link CountDownLatch}使用给定的计数初始化，{@link #await await}方法阻塞，直到当前计数由于调用{@link #countDown}方法而达到零，之后所有等待的线程都被释放，并且任何后续的{@link #await await}调用立即返回。
 *
 * @author tilkai
 */
public class CountDownLatch {

    /**
     * CountDownLatch的同步控件，使用AQS状态来表示计数
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();}

        protected int tryAcquireShared(int acquires) {
            return (getState() == 0 ? 1:-1);
        }

        protected boolean tryReleaseShared(int releases) {
            for (;;) {
                int c = getState();
                if (c == 0) {
                    return false;
                }
                int nextc = c - 1;
                if (compareAndSetState(c, nextc)) {
                    return nextc == 0;
                }
            }
        }
    }

    private final Sync sync;

    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

    /**
     * 导致线程等待，直到counted down变为0，除非线程中断；
     *
     * 若当前计数等于0，则该方法立即返回；
     * 若当前计数大于0，则当前线程变为禁用以用于线程调度目的，并且处于休眠状态，直到发生以下两种情况之一：
     *     1. 由于{@link #countDown}方法的调用，计数到达零;
     *     2. 一些其他线程{@linkplain线程＃中断中断}当前线程。
     *
     * 如果当前线程：
     *     在进入此方法时设置其中断状态; 或在等待期间{@linkplain线程＃中断}，
     *     则抛出{@link InterruptedException}，并清除当前线程的中断状态。
     *
     * @throws InterruptedException 如果当前线程在等待时中断，则抛出InterruptedException
     */
    public void await() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /**
     * 减少latch的计数，如果计数达到零，释放所有等待的线程。
     *
     * 如果当前计数大于零，则递减。 如果新计数为零，则所有等待的线程被重新启用用于线程调度目的。
     *
     * 如果当前计数等于零，则什么也不发生。
     */
    public void countDown() {
        sync.releaseShared(1);
    }

    /**
     * 返回当前计数
     *
     * 此方法常用于调试目的；
     *
     * @return
     */
    public long getCount() {
        return sync.getCount();
    }

    /**
     * 返回一个标识这个锁存器的字符串，以及它的状态
     * @return
     */
    public String toString() {
        return super.toString() + "[Count = " + sync.getCount() + "]";
    }
}
