package com.mohan.project.easycache.selection;

/**
 * 数据淘汰策略枚举类
 * @author mohan
 * @date 2018-08-05 22:03:11
 */
public enum SelctionStrategyEnum {

    /**
     * 从已设置过期时间的数据集中挑选最近最少使用的数据淘汰
     */
    VOLATILE_LRU("从已设置过期时间的数据集中挑选最近最少使用的数据淘汰"),

    /**
     * 从已设置过期时间的数据集中挑选将要过期的数据淘汰
     */
    VOLATILE_TTL("从已设置过期时间的数据集中挑选将要过期的数据淘汰"),

    /**
     * 从已设置过期时间的数据集中任意选择数据淘汰
     */
    VOLATILE_RANDOM("从已设置过期时间的数据集中任意选择数据淘汰"),

    /**
     * 从数据集中挑选最近最少使用的数据淘汰
     */
    ALLKEYS_LRU("从数据集中挑选最近最少使用的数据淘汰"),

    /**
     * 从数据集中任意选择数据淘汰
     */
    ALLKEYS_RANDOM("从数据集中任意选择数据淘汰"),

    /**
     * 禁止驱逐数据
     */
    NO_ENVICTION("禁止驱逐数据");

    private String name;

    SelctionStrategyEnum(String name) {
        this.name = name;
    }
}