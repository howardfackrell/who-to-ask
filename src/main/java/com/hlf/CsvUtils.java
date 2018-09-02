package com.hlf;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvUtils {
    public static List<String> readLines(String filePath) throws Exception {
        return Files.readAllLines(Paths.get(filePath));
    }

    public static <T> List<T> readFromCsv(final List<String> csvLines, final Function<String[], T> reader) {
        List<T> list =  csvLines.stream()
                .filter(s -> StringUtils.isNotBlank(s))
                //               s.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                .map(s -> s.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))
                .filter(a -> a != null && a.length > 0)
                .map(a -> reader.apply(a))
                .collect(Collectors.toList());

        return list;
    }
}
