package org.sample.cron.parser.part;

import lombok.val;

import java.util.Set;

public abstract class BasePartParser implements PartParser {
    @Override
    public Set<Integer> parse(final CronPartWithLimits cronPart) {
        val part = cronPart.getPart();
        if (isSupported(part)) {
            return process(cronPart);
        } else {
            val successor = getSuccessor();
            return successor.parse(cronPart);
        }
    }

    abstract boolean isSupported(final String part);

    abstract Set<Integer> process(final CronPartWithLimits cronPart);

    abstract PartParser getSuccessor();
}