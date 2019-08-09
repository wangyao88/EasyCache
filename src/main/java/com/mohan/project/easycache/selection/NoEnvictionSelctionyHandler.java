package com.mohan.project.easycache.selection;

import com.mohan.project.easycache.core.EasyCache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * 缓存数据淘汰实现类
 * SelctionStrategyEnum.NO_ENVICTION
 * 禁止驱逐数据
 * @author mohan
 * @date 2018-08-06 23:23:30
 */
public class NoEnvictionSelctionyHandler extends SelctionStrategyHandler{

    @Override
    protected <Key, Value> List<Key> doHandle(long selectionNum, EasyCache<Key, Value> easyCache) {
        String format = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        StringBuilder info = new StringBuilder();
        info.append(format)
            .append("\t")
            .append(EasyCache.DEFAULT_NAME)
            .append("\t")
            .append("缓存达到上限，并且数据淘汰策略设置为")
            .append(SelctionStrategyEnum.NO_ENVICTION.name())
            .append(", 无法进行数据淘汰！请手动删除数据！");
        System.out.println(info.toString());
        return Collections.emptyList();
    }
}
