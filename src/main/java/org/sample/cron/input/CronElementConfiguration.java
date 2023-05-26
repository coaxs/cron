package org.sample.cron.input;

import lombok.Builder;
import lombok.Value;
import org.sample.cron.parser.part.PartParser;

@Value
@Builder
public class CronElementConfiguration {
    String label;
    Integer position;
    Integer min;
    Integer max;
    PartParser partParser;
}