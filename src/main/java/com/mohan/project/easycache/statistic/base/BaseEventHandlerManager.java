package com.mohan.project.easycache.statistic.base;

import com.google.common.collect.Maps;
import com.mohan.project.easytools.log.LogTools;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * EasyCache统计信息事件处理类管理类
 * @author mohan
 * @date 2018-09-05 14:16:59
 */
public class BaseEventHandlerManager {

    private static final Map<String, BaseEventHandler> EVENT_HANDLER_MAP = Maps.newHashMap();

    static {
        init();
    }

    private static void init() {
        Reflections reflections = new Reflections("com.mohan.project.easycache.statistic");
        Set<Class<? extends BaseEventHandler>> classes = reflections.getSubTypesOf(BaseEventHandler.class);
        try {
            for (Class<? extends BaseEventHandler> clazz : classes) {
                BaseEventHandler baseEventHandler = clazz.newInstance();
                EVENT_HANDLER_MAP.put(baseEventHandler.getEventName(), baseEventHandler);
            }
        }catch (Exception e) {
            LogTools.error("EasyCache内部错误！注册EasyCache统计信息事件处理类失败", e);
        }
    }

    public static Optional<BaseEventHandler> get(String className) {
        if(isNotInit()) {
            init();
        }
        return Optional.ofNullable(EVENT_HANDLER_MAP.get(className));
    }

    private static boolean isNotInit() {
        return EVENT_HANDLER_MAP.isEmpty();
    }
}