package com.mohan.project.easycache;

import com.mohan.project.easycache.core.EasyCache;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        final EasyCache<String, String> easyCache = EasyCache.EasyCacheBuilder.<String, String>newBuilder().maxSize(2000000L).build();
        for (int i = 0; i < 2000000; i++) {
            easyCache.put(i+"", i+"");
        }
        final Random random = new Random();

        int num = 2;
        final CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(() -> {
                String key = random.nextInt(40) + "";
                easyCache.get(key);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        TimeUnit.SECONDS.sleep(1);
        easyCache.showStatistic();
    }
}