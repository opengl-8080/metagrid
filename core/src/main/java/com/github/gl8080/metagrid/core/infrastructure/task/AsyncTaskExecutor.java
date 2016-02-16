package com.github.gl8080.metagrid.core.infrastructure.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gl8080.metagrid.core.config.AsyncTask;
import com.github.gl8080.metagrid.core.config.MetagridConfig;

public class AsyncTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskExecutor.class);
    
    private static AsyncTaskExecutor instance;
    
    public static void initialize() {
        AsyncTask asyncTask = MetagridConfig.getInstance().getAsyncTask();
        instance = new AsyncTaskExecutor(asyncTask.getMax());
    }
    
    public static AsyncTaskExecutor getInstance() {
        return instance;
    }
    
    private ExecutorService service;
    
    AsyncTaskExecutor(int n) {
        this.service = Executors.newFixedThreadPool(n, new TaskThreadFactory());
        logger.info("非同期タスク数 {} で初期化されました。", n);
    }

    public void dispatch(Task task) {
        this.service.submit(task);
    }
    
    private static class TaskThreadFactory implements ThreadFactory {
        private int cnt = 0;
        
        @Override
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r);
            th.setName("MetaGridAsyncTask-" + (++this.cnt));
            return th;
        }
    }
}
