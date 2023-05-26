package org.sample.cron.parser.part;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor(staticName = "of")
public class CronPartWithLimits {
    @With
    String part;
    Integer min;
    Integer max;
}