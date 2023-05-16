package org.sample.cron.input;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CronWithCommand {
    String cron;
    String command;
}