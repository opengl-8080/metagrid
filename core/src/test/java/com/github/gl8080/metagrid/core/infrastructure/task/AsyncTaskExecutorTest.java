package com.github.gl8080.metagrid.core.infrastructure.task;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class AsyncTaskExecutorTest {

    @Test
    public void 指定したタスクが別スレッドで実行されること() throws Exception {
        // setup
        final String mainThreadName = Thread.currentThread().getName();
        
        class TestTask implements Task {
            
            private boolean success;

            @Override
            public void run() {
                // verify
                String threadName = Thread.currentThread().getName();
                assertThat(threadName).as("スレッド名").isNotEqualTo(mainThreadName);
                this.success = true;
                this.notifyAll();
            }
        }
        
        AsyncTaskExecutor executor = new AsyncTaskExecutor(1);
        
        TestTask task = new TestTask();
        
        // exercise
        executor.dispatch(task);
        
        synchronized (task) {
            task.wait(500);
        }
        
        assertThat(task.success).as("タスクが実行された").isTrue();
    }

    @Test
    public void 別スレッドの名前のチェック() throws Exception {
        // setup
        class TestTask implements Task {
            private String threadName;

            @Override
            public void run() {
                this.threadName = Thread.currentThread().getName();
            }
        }
        
        AsyncTaskExecutor executor = new AsyncTaskExecutor(3);
        
        TestTask task1 = new TestTask();
        TestTask task2 = new TestTask();
        TestTask task3 = new TestTask();
        TestTask task4 = new TestTask();
        
        // exercise
        executor.dispatch(task1);
        Thread.sleep(20);
        executor.dispatch(task2);
        Thread.sleep(20);
        executor.dispatch(task3);
        Thread.sleep(20);
        executor.dispatch(task4);
        Thread.sleep(20);
        
        // verify
        assertThat(task1.threadName).isEqualTo("MetaGridAsyncTask-1");
        assertThat(task2.threadName).isEqualTo("MetaGridAsyncTask-2");
        assertThat(task3.threadName).isEqualTo("MetaGridAsyncTask-3");
        assertThat(task4.threadName).isEqualTo("MetaGridAsyncTask-1");
    }

    @Test
    public void 指定した数のスレッドだけが同時実行され_それ以降のスレッドは待機させられる() throws Exception {
        // setup
        class TestTask implements Task {
            
            private boolean continued = true;
            private boolean start;
            
            @Override
            public void run() {
                this.start = true;
                while (this.continued) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {}
                }
            }
            
            public void stop() {
                this.continued = false;
            }
        };
        
        TestTask task1 = new TestTask();
        TestTask task2 = new TestTask();
        TestTask task3 = new TestTask();
        
        AsyncTaskExecutor executor = new AsyncTaskExecutor(2);
        
        // exercise
        executor.dispatch(task1);
        executor.dispatch(task2);
        executor.dispatch(task3);
        
        Thread.sleep(100);
        
        // verify
        assertThat(task1.start).as("タスク１は実行済み").isTrue();
        assertThat(task2.start).as("タスク２は実行済み").isTrue();
        assertThat(task3.start).as("タスク３はまだ未実行").isFalse();
        
        task1.stop();
        
        Thread.sleep(100);

        assertThat(task2.start).as("タスク２は実行済み").isTrue();
        assertThat(task3.start).as("タスク３は実行済み").isTrue();
    }
    
    
}
