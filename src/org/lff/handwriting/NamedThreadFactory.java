package org.lff.handwriting;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class NamedThreadFactory implements ThreadFactory {
    String name;
    AtomicInteger threadNo = new AtomicInteger(0);

    public NamedThreadFactory(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        this.name = name;
    }

    public Thread newThread(Runnable r) {
        String threadName = name + "-" + threadNo.incrementAndGet();
        return new Thread(r, threadName);
    }
}