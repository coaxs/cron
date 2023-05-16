package org.sample.cron.input;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor(staticName = "of")
public class ArgsBasedCronWithCommandExtractor {
    public CronWithCommand extract(
            final String[] args
    ) {
        checkArgumentsLength(args);
        val cronWithCommand = args[0];
        return CronWithCommand.builder()
                .cron(extractCron(cronWithCommand))
                .command(extractCommand(cronWithCommand))
                .build();
    }

    private String extractCommand(
            final String cronWithCommand
    ) {
        return cronWithCommand.substring(extractLastIndex(cronWithCommand) + 1);
    }

    private String extractCron(
            final String cronWithCommand
    ) {
        return cronWithCommand.substring(0, extractLastIndex(cronWithCommand));
    }

    private int extractLastIndex(
            final String cronWithCommand
    ) {
        val lastSpaceIndex = cronWithCommand.lastIndexOf(" ");
        checkLastIndex(lastSpaceIndex);
        return lastSpaceIndex;
    }

    private void checkLastIndex(
            final int lastSpaceIndex
    ) {
        if (lastSpaceIndex < 0) {
            throw new IllegalArgumentException("Wrong argument provided you should provide cron and command as one argument with space as separator.");
        }
    }

    private void checkArgumentsLength(
            final String[] args
    ) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Wrong numbers of arguments provided. You should provide cron and command as one argument.");
        }
    }
}