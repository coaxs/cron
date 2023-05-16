package org.sample.cron.output;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Value
@Builder
public class CronValue {
    private static final int SPACE_COLUMN_SIZE = 14;
    private static final String JOINING_TOKEN = " ";

    Map<String, Set<Integer>> cronParts;
    String command;

    @Override
    public String toString() {
        return cronParts.entrySet()
                .stream()
                .map(stringSetEntry -> formatSetWithTitle(stringSetEntry.getKey(), stringSetEntry.getValue()))
                .collect(Collectors.joining("")) +
                format("%-" + SPACE_COLUMN_SIZE + "s %s", "command", command);
    }

    private String formatSetWithTitle(
            final String title,
            final Set<Integer> list
    ) {
        if (Objects.isNull(list)) {
            return format("%-" + SPACE_COLUMN_SIZE + "s%n", title);
        } else {
            return format(
                    "%-" + SPACE_COLUMN_SIZE + "s %s%n",
                    title,
                    setToStringContainsSortedValue(list)
            );
        }
    }

    private static String setToStringContainsSortedValue(
            final Set<Integer> set
    ) {
        return set.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(JOINING_TOKEN));
    }
}