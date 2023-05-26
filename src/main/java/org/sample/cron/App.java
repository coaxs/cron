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
        val defaultCronPartParser = RangeCheckerPartParser.of(CommaParser.of(AsteriskParser.of(DashParser.of(SlashParser.of(IntegerParser.of(ThrowingParser.of()))))));
        val daysCronPartParser = RangeCheckerPartParser.of(CommaParser.of(DaysParser.of(AsteriskParser.of(DashParser.of(SlashParser.of(IntegerParser.of(ThrowingParser.of())))))));
        val configs = ImmutableSet.of(
                CronElementConfiguration.builder()
                        .label("minutes")
                        .position(0)
                        .min(0)
                        .max(59)
                        .partParser(defaultCronPartParser)
                        .build(),
                CronElementConfiguration.builder()
                        .label("hours")
                        .position(1)
                        .min(0)
                        .max(23)
                        .partParser(defaultCronPartParser)
                        .build(),
                CronElementConfiguration.builder()
                        .label("day of month")
                        .position(2)
                        .min(0)
                        .max(31)
                        .partParser(defaultCronPartParser)
                        .build(),
                CronElementConfiguration.builder()
                        .label("month")
                        .position(3)
                        .min(1)
                        .max(12)
                        .partParser(defaultCronPartParser)
                        .build(),
                CronElementConfiguration.builder()
                        .label("day of week")
                        .position(4)
                        .min(1)
                        .max(7)
                        .partParser(daysCronPartParser)
                        .build(),
                CronElementConfiguration.builder()
                        .label("year")
                        .position(5)
                        .min(2020)
                        .max(2025)
                        .partParser(defaultCronPartParser)
                        .build()
        );
        val parser = CronParser.of(configs);
        val cronValue = parser.parse(cronWithCommand);
        System.out.println(cronValue);
    }
}
