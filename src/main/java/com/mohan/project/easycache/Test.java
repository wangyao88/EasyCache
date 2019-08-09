package com.mohan.project.easycache;

import com.google.common.base.Defaults;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Test {

    public static void main(String[] args) {
        Integer integer = Defaults.defaultValue(int.class);
        System.out.println(integer);
    }
}
