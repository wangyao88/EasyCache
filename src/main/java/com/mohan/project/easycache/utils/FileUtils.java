package com.mohan.project.easycache.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * File工具类
 * @author mohan
 * @date 2018-08-29 13:15:22
 */
public class FileUtils {

    public static Optional<List<String>> getLines(Path path) {
        try {
            return Optional.ofNullable(Files.readAllLines(path, Charset.defaultCharset()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getContent(Path path) {
        Optional<List<String>> optional = getLines(path);
        List<String> lines = optional.orElse(new ArrayList<>());
        if(lines.isEmpty()) {
            return Optional.empty();
        }
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            content.append(line).append(Constant.LF);
        }
        return Optional.of(content.toString());
    }
}