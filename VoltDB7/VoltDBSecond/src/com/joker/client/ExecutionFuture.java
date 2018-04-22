package com.joker.client;

import org.voltdb.client.ClientResponse;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hunter on 2017/12/4.
 */
public class ExecutionFuture implements Future<ClientResponse> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final long timeout;
    private AtomicInteger status = new AtomicInteger(0);
    private static final int STATUS_RUNNING = 0;
    private static final int STATUS_SUCCESS = 1;
    private static final int STATUS_TIMEOUT = 2;
    private static final int STATUS_FAILURE = 3;
    private static final int STATUS_ABORTED = 4;
    private ClientResponse response = null;

    protected ExecutionFuture(long timeout) {
        this.timeout = timeout;
    }

    protected void set(ClientResponse response) {
        if (!status.compareAndSet(STATUS_RUNNING, STATUS_SUCCESS))
            return;
        this.response = response;
        this.latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return status.get() == STATUS_ABORTED;
    }

    @Override
    public boolean isDone() {
        return status.get() != STATUS_RUNNING;
    }

    //Waits if necessary for the computation to complete, and then retrieves its result.
    @Override
    public ClientResponse get() throws InterruptedException, ExecutionException {
        try {
            return get(this.timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException to) {
            status.compareAndSet(STATUS_RUNNING, STATUS_TIMEOUT);
            throw new ExecutionException(to);
        }
    }

    @Override
    public ClientResponse get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            status.compareAndSet(STATUS_RUNNING, STATUS_TIMEOUT);
            throw new TimeoutException();
        }
        if (isCancelled()) {
            throw new CancellationException();
        } else if (this.response.getStatus() != ClientResponse.SUCCESS) {
            status.compareAndSet(STATUS_RUNNING, STATUS_FAILURE);
            throw new ExecutionException(new Exception(response.getStatusString()));
        } else {
            status.compareAndSet(STATUS_RUNNING, STATUS_SUCCESS);
            return this.response;
        }
    }
}
