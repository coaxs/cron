package org.sample.cron.parser.part;

import lombok.Value;
import lombok.With;

@Value(staticConstructor = "of")
public class CronPartWithLimits {
    @With
    String part;
    Integer min;
    Integer max;
}