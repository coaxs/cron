package org.sample.cron.parser.part;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor(staticName = "of")
public class DaysParser extends BasePartParser {
    private static final Map<String, String> DAYS = ImmutableMap.of(
            "MON", "1",
            "TUE", "2",
            "WED", "3",
            "THU", "4",
            "FRI", "5",
            "SAT", "6",
            "SUN", "7"
    );

    @Getter
    private final PartParser successor;

    @Override
    boolean isSupported(
            final String part
    ) {
        return DAYS.keySet()
                .stream()
                .anyMatch(part::contains);
    }

    @Override
    Set<Integer> process(
            final CronPartWithLimits cronPart
    ) {
        val newCron = DAYS.entrySet()
                .stream()
                .reduce(
                        cronPart.getPart(),
                        (p, entry) -> p.replaceAll(entry.getKey(), entry.getValue()),
                        (a, b) -> a
                );
        return successor.parse(
                cronPart.toBuilder()
                        .part(newCron)
                        .build()
        );
    }
}
