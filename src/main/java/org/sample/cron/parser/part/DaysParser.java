package org.sample.cron.parser.part;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Map;
import java.util.Set;

import static org.jooq.lambda.Seq.seq;

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
        val initialCronPart = cronPart.getPart();
        val newCron = seq(DAYS.entrySet().stream())
                .foldLeft(
                        initialCronPart,
                        (actualCron, dayOfWeekEntry) -> actualCron.replaceAll(dayOfWeekEntry.getKey(), dayOfWeekEntry.getValue())
                );
        return successor.parse(
                cronPart.toBuilder()
                        .part(newCron)
                        .build()
        );
    }
}