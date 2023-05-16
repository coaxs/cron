package org.sample.cron.parser.checker;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sample.cron.parser.part.CronPartWithLimits;
import org.sample.cron.parser.part.PartParser;

import java.util.Set;

@RequiredArgsConstructor(staticName = "of")
public class RangeCheckerPartParser implements PartParser {
    private final PartParser partParser;

    @Override
    public Set<Integer> parse(final CronPartWithLimits cronPart) {
        val parsedValues = partParser.parse(cronPart);
        parsedValues.forEach(value -> {
            if (value < cronPart.getMin() || value > cronPart.getMax()) {
                throw new OutOfRangeException("Value " + value + " is out of range.");
            }
        });
        return parsedValues;
    }
}
