package com.wcs.vcc.mvvm.util.diskexecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskExecutor implements Executor {

    private final Executor diskExecutor;

    public DiskExecutor() {
        this.diskExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Runnable command) {
        diskExecutor.execute(command);
    }
}
