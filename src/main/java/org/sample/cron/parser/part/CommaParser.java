package org.sample.cron.parser.part;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
public class CommaParser implements PartParser {
    private static final String COMA = ",";

    private final PartParser successor;

    @Override
    public Set<Integer> parse(
            final CronPartWithLimits cron
    ) {
        val arguments = cron.getPart().split(COMA);
        if (arguments.length == 0) {
            throw new ParsingException("Cannot parse cron part: " + cron.getPart());
        } else if (arguments.length == 1)
            return successor.parse(cron);
        else {
            checkArgumentsAreNotEmpty(arguments);
            return Arrays.stream(arguments)
                    .map(cron::withPart)
                    .map(this::parse)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }
    }

    private void checkArgumentsAreNotEmpty(
            final String[] cron
    ) {
        Arrays.stream(cron).filter(String::isEmpty).findAny().ifPresent(
                empty -> {
                    throw new ParsingException("Empty value is not allowed in comma.");
                }
        );
    }
}