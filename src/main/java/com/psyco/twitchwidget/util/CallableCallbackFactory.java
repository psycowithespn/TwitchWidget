package com.psyco.twitchwidget.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class CallableCallbackFactory {

    private final Consumer<Runnable> callbackExecutor;
    private final Consumer<Runnable> backgroundThread;

    public CallableCallbackFactory(Executor callbackExecutor, Executor backgroundThread) {
        this.callbackExecutor = callbackExecutor::execute;
        this.backgroundThread = backgroundThread::execute;
    }

    public <T> CallableCallback<T> submit(Callable<T> callable) {
        return new CallableCallback<T>(callbackExecutor, backgroundThread, callable);
    }
}
