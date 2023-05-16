package org.sample.cron.parser.part;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.rangeClosed;

@RequiredArgsConstructor(staticName = "of")
public class AsteriskParser extends BasePartParser {
    private static final String ASTERISK = "*";

    @Getter(AccessLevel.PROTECTED)
    private final PartParser successor;

    @Override
    boolean isSupported(String part) {
        return part.equals(ASTERISK);
    }

    @Override
    Set<Integer> process(final CronPartWithLimits cronPart) {
        return rangeClosed(cronPart.getMin(), cronPart.getMax())
                .boxed()
                .collect(Collectors.toSet());
    }
}
