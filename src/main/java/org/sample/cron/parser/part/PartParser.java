package org.sample.cron.parser.part;

import java.util.Set;

public interface PartParser {
    Set<Integer> parse(final CronPartWithLimits cronPart);
}
