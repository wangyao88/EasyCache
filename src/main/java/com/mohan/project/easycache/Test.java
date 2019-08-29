package com.mohan.project.easycache;

import com.mohan.project.easycache.core.EasyCache;

import java.util.Random;

public class Test {

    public static void main(String[] args) {
        EasyCache<String, String> easyCache = EasyCache.EasyCacheBuilder.<String, String>newBuilder().maxSize(200000L).build();
        for (int i = 0; i < 100000; i++) {
            easyCache.put(i+"", i+"");
        }
        Random random = new Random();
        for (int i = 0; i < 100000000; i++) {
            String key = random.nextInt(100000000) + "";
            easyCache.get(key);
        }
        easyCache.showStatistic();
    }
}
