package org.sample.cron.parser.part;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@RequiredArgsConstructor(staticName = "of")
public class IntegerParser extends BasePartParser {
    @Getter
    private final PartParser successor;

    @Override
    boolean isSupported(
            final String part
    ) {
        return isNumeric(part);
    }

    @Override
    Set<Integer> process(
            final CronPartWithLimits cronPart
    ) {
        val part = cronPart.getPart();
        val parsedCronValue = Integer.parseInt(part);
        return ImmutableSet.of(parsedCronValue);
    }
}
