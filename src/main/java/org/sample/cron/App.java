package org.sample.cron;

import com.google.common.collect.ImmutableSet;
import lombok.val;
import org.sample.cron.input.ArgsBasedCronWithCommandExtractor;
import org.sample.cron.input.CronElementConfiguration;
import org.sample.cron.parser.CronParser;
import org.sample.cron.parser.checker.RangeCheckerPartParser;
import org.sample.cron.parser.part.*;

public class App {
    public static void main(final String[] args) {
        val argsBasedCronWithCommandExtractor = ArgsBasedCronWithCommandExtractor.of();
        val cronWithCommand = argsBasedCronWithCommandExtractor.extract(args);
        val configs = ImmutableSet.of(
                CronElementConfiguration.builder()
                        .label("minutes")
                        .position(0)
                        .min(0)
                        .max(59)
                        .build(),
                CronElementConfiguration.builder()
                        .label("hours")
                        .position(1)
                        .min(0)
                        .max(23)
                        .build(),
                CronElementConfiguration.builder()
                        .label("day of month")
                        .position(2)
                        .min(0)
                        .max(31)
                        .build(),
                CronElementConfiguration.builder()
                        .label("month")
                        .position(3)
                        .min(1)
                        .max(12)
                        .build(),
                CronElementConfiguration.builder()
                        .label("day of week")
                        .position(4)
                        .min(1)
                        .max(7)
                        .build()
        );
        val cronPartParser = RangeCheckerPartParser.of(CommaParser.of(AsteriskParser.of(DashParser.of(SlashParser.of(IntegerParser.of(ThrowingParser.of()))))));
        val parser = CronParser.of(cronPartParser, configs);
        val cronValue = parser.parse(cronWithCommand);
        System.out.println(cronValue);
    }
}
