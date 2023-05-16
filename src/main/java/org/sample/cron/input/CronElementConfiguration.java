package org.sample.cron.input;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CronElementConfiguration {
    String label;
    Integer position;
    Integer min;
    Integer max;
}