package org.sample.cron.parser.part;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor(staticName = "of")
public class ThrowingParser implements PartParser {
    @Override
    public Set<Integer> parse(
            final CronPartWithLimits cronPart
    ) {
        throw new ParserNotFoundException(
                String.format("Cannot find parser for parsing: %s", cronPart));
    }
}