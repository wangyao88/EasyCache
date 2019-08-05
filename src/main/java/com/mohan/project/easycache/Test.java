package com.mohan.project.easycache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mohan.project.easycache.core.EasyCache;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        EasyCache<String, String> easyCache = EasyCache.EasyCacheBuilder.builder().build();
        List<String> list = Lists.newArrayList("b", "a");
        list.sort(String::compareTo);
        for (String str : list) {
            System.out.println(str);
        }

        CacheBuilder.newBuilder().build();
    }
}
