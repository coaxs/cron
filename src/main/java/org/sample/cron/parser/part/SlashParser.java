package org.sample.cron.parser.part;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(staticName = "of")
public class SlashParser extends BasePartParser {
    private static final String SLASH = "/";

    @Getter(AccessLevel.PACKAGE)
    private final PartParser successor;

    @Override
    boolean isSupported(final String part) {
        return part.contains(SLASH);
    }

    @Override
    Set<Integer> process(CronPartWithLimits cronPart) {
        val part = cronPart.getPart();
        val arguments = part.split(SLASH);
        checkArguments(arguments);
        val results = new HashSet<Integer>();
        val start = arguments[0].equals("*") ? cronPart.getMin() : Integer.parseInt(arguments[0]);
        val step = Integer.parseInt(arguments[1]);
        var actual = start;
        while (actual <= cronPart.getMax()) {
            results.add(actual);
            actual += step;
        }
        return results;
    }

    private void checkArguments(
            final String[] cron
    ) {
        if (cron.length != 2) {
            throw new ParsingException("Slash should have 2 arguments.");
        } else if (cron[0].isEmpty() || cron[1].isEmpty()) {
            throw new ParsingException("Empty value is not allowed in slash.");
        }
    }
}