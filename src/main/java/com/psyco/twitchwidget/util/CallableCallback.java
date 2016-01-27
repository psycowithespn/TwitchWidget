package com.psyco.twitchwidget.util;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CallableCallback<T> implements Runnable {

    private final Consumer<Runnable> callbackExecutor;
    private final Consumer<Runnable> backgroundThread;
    private final Callable<T> task;

    private final AtomicBoolean executed = new AtomicBoolean(false);

    private Consumer<T> onSucceeded;
    private Consumer<Exception> onException;

    private T value;
    private Exception exception;

    public CallableCallback(Consumer<Runnable> callbackExecutor, Consumer<Runnable> backgroundThread, Callable<T> task) {
        this.callbackExecutor = callbackExecutor;
        this.backgroundThread = backgroundThread;
        this.task = task;
    }

    public void start() {
        if (executed.compareAndSet(false, true)) {
            backgroundThread.accept(this);
        } else {
            throw new IllegalStateException("Callable already executed");
        }
    }

    @Override
    public final void run() {
        try {
            value = task.call();
            callbackExecutor.accept(() -> onSucceeded.accept(value));
        } catch (Exception e) {
            exception = e;
            callbackExecutor.accept(() -> onException.accept(exception));
        }
    }

    public CallableCallback<T> withOnSucceeded(Consumer<T> onSucceeded) {
        this.onSucceeded = onSucceeded;
        return this;
    }

    public CallableCallback<T> withOnException(Consumer<Exception> onException) {
        this.onException = onException;
        return this;
    }
}
