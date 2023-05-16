package org.sample.cron.parser;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.sample.cron.input.CronElementConfiguration;
import org.sample.cron.input.CronWithCommand;
import org.sample.cron.output.CronValue;
import org.sample.cron.parser.part.CronPartWithLimits;
import org.sample.cron.parser.part.PartParser;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RequiredArgsConstructor(staticName = "of")
public class CronParser {
    private static final String SPLIT_REGEX = " ";

    private final PartParser partParser;
    private final Collection<CronElementConfiguration> configs;

    public CronValue parse(
            final CronWithCommand cronWithCommand
    ) {
        validate(cronWithCommand);
        val cronParts = cronWithCommand.getCron().split(SPLIT_REGEX);
        val cronElements = configs.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toMap(
                                        CronElementConfiguration::getLabel,
                                        config -> partParser.parse(
                                                CronPartWithLimits.of(
                                                        cronParts[config.getPosition()],
                                                        config.getMin(),
                                                        config.getMax()
                                                )
                                        ),
                                        (a, b) -> {
                                            throw new RuntimeException("Duplicate label in configuration found: " + a);
                                        },
                                        LinkedHashMap::new
                                ),
                                Collections::unmodifiableMap
                        )
                );
        return CronValue.builder()
                .cronParts(cronElements)
                .command(cronWithCommand.getCommand())
                .build();
    }

    private void validate(
            final CronWithCommand cronWithCommand
    ) {
        if (isNull(cronWithCommand.getCommand())) {
            throw new IllegalArgumentException("Cannot parse CronWithCommandArgument with null command.");
        }
        if (isNull(cronWithCommand.getCron())) {
            throw new IllegalArgumentException("Cannot parse CronWithCommandArgument with null cron.");
        }
    }
}