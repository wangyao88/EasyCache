package com.mohan.project.easycache;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mohan.project.easycache.core.EasyCache;

import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        Map<String, String> map = Maps.newHashMap();
        map.put("121", "aaa00");


        System.out.println(map);

    }
}
