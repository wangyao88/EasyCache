package com.mohan.project.easycache;

import com.mohan.project.easycache.core.EasyCache;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        EasyCache<String, String> easyCache = EasyCache.EasyCacheBuilder.<String, String>newBuilder().maxSize(200000L).build();
        for (int i = 0; i < 20; i++) {
            easyCache.put(i+"", i+"");
        }
        Random random = new Random();

        int num = 100;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            CompletableFuture.runAsync(() -> {
                String key = random.nextInt(40) + "";
                easyCache.get(key);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        TimeUnit.SECONDS.sleep(6);
        easyCache.showStatistic();
    }
}