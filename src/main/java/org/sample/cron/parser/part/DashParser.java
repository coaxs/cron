package org.sample.cron.parser.part;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(staticName = "of")
public class DashParser extends BasePartParser {
    private static final String DASH = "-";

    @Getter(AccessLevel.PACKAGE)
    private final PartParser successor;

    @Override
    boolean isSupported(final String part) {
        return part.contains(DASH);
    }

    @Override
    Set<Integer> process(final CronPartWithLimits cronPart) {
        val part = cronPart.getPart();
        val arguments = part.split(DASH);
        checkArguments(arguments);
        return IntStream.rangeClosed(
                        Integer.parseInt(arguments[0]),
                        Integer.parseInt(arguments[1])
                ).boxed()
                .collect(Collectors.toSet());
    }

    private void checkArguments(
            final String[] cron
    ) {
        if (cron.length != 2) {
            throw new ParsingException("Dash should have 2 arguments.");
        } else if (cron[0].isEmpty() || cron[1].isEmpty()) {
            throw new ParsingException("Empty value is not allowed in dash.");
        }
    }
}