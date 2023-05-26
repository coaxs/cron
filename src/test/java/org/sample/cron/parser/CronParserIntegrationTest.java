package org.sample.cron.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.sample.cron.input.CronElementConfiguration;
import org.sample.cron.input.CronWithCommand;
import org.sample.cron.parser.checker.OutOfRangeException;
import org.sample.cron.parser.checker.RangeCheckerPartParser;
import org.sample.cron.parser.part.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Tag("integration")
class CronParserIntegrationTest {
    private static final String LABEL = "minutes";
    private static final Integer POSITION = 0;
    private static final Integer MIN = 1;
    private static final Integer MAX = 10;
    private static final String COMMAND = "/usr/a";

    @Test
    void parse_whenCommandIsAbsentInCronWithCommand_shouldThrowIllegalArgumentException() {
        val cronWithCommand = prepareCronWithCommand("1")
                .toBuilder()
                .command(null)
                .build();
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void parse_whenCronIsAbsentInCronWithCommand_shouldThrowIllegalArgumentException() {
        val cronWithCommand = prepareCronWithCommand(null);
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void parse_whenCommandIsPresentInCronWithCommand_shouldReturnCronValueWithCommand() {
        val cronWithCommand = prepareCronWithCommand("1");
        val cronParser = createCronParser();

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCommand()).isEqualTo(COMMAND);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsComaWithArguments_shouldReturnCronValueWithCronPartsContainsEveryComaArgumentParsed() {
        val cronWithCommand = prepareCronWithCommand("1,2-4,5/3");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1, 2, 3, 4, 5, 8));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsOnlyComa_shouldThrowParsingException() {
        val cronWithCommand = prepareCronWithCommand(",");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsComaWithRightEmptyArgument_shouldThrowParserNotFoundException() {
        val cronWithCommand = prepareCronWithCommand("1,");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParserNotFoundException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsComaWithLeftEmptyArgument_shouldThrowParsingException() {
        val cronWithCommand = prepareCronWithCommand(",1");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandEqualsAsterisk_shouldReturnCronValueWithCronPartsContainingValuesFromMinToMax() {
        val cronWithCommand = prepareCronWithCommand("*");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsDashWithLeftAndRightArgument_shouldReturnCronValueWithCronPartsContainingValuesFromDashLeftArgumentToRightArgument() {
        val cronWithCommand = prepareCronWithCommand("1-3");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1, 2, 3));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsDashWithTwoEmptyArguments_shouldThrowParsingException() {
        val cronWithCommand = prepareCronWithCommand("-");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsDashWithOneEmptyArgument_shouldThrowParsingException() {
        val cronWithCommand = prepareCronWithCommand("-1");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsDashWithArgumentWhichAreNotIntegerParsable_shouldThrowNumberFormatException() {
        val cronWithCommand = prepareCronWithCommand("a-f");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(NumberFormatException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsSlashWithLeftAndRightArgument_shouldReturnCronValueWithCronPartsContainingAllConsecutiveSumsOfTheRightArgumentStartFromTheLeftArgumentAndEndBelowMaximum() {
        val cronWithCommand = prepareCronWithCommand("1/4");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1, 5, 9));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsSlashWithLeftArgumentEqualsAsteriskAndRightArgument_shouldReturnCronValueWithCronPartsContainingAllConsecutiveSumsOfTheRightArgumentStartFromTheMinimumAndEndBelowMaximum() {
        val cronWithCommand = prepareCronWithCommand("*/3");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1, 4, 7, 10));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsSlashWithTwoEmptyArguments_shouldThrowParsingException() {
        val cronWithCommand = prepareCronWithCommand("/");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsSlashWithOneEmptyArgument_shouldThrowParsingException() {
        val cronWithCommand = prepareCronWithCommand("/1");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsSlashWithArgumentWhichAreNotIntegerParsable_shouldThrowNumberFormatException() {
        val cronWithCommand = prepareCronWithCommand("a/f");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(NumberFormatException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsArgumentWhichIsSingleInteger_shouldReturnCronValueWithCronPartsContainingSingleInteger() {
        val cronWithCommand = prepareCronWithCommand("1");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsArgumentWhichIsNotSupported_shouldThrowParserNotFoundException() {
        val cronWithCommand = prepareCronWithCommand("NOT_SUPPORTED");
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(ParserNotFoundException.class);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsDayAsString_shouldReturnCronValueWithCronPartsContainingValueRepresentingProvidedDay() {
        val cronWithCommand = prepareCronWithCommand("MON");
        val cronParser = createCronParser();
        val expectedCronParts = ImmutableMap.of(LABEL, ImmutableSet.of(1));

        val actualResult = cronParser.parse(cronWithCommand);

        assertThat(actualResult.getCronParts()).isEqualTo(expectedCronParts);
    }

    @Test
    void parse_whenCronInCronWithCommandContainsArgumentWhichIsOutOfRange_shouldThrowOutOfRangeException() {
        val outOfRangeValue = "11";
        val cronWithCommand = prepareCronWithCommand(outOfRangeValue);
        val cronParser = createCronParser();

        val actualException = catchThrowable(() -> cronParser.parse(cronWithCommand));

        assertThat(actualException).isInstanceOf(OutOfRangeException.class);
    }

    private CronWithCommand prepareCronWithCommand(
            final String cron
    ) {
        return CronWithCommand.builder()
                .command(COMMAND)
                .cron(cron)
                .build();
    }

    private CronParser createCronParser() {
        val cronParser = RangeCheckerPartParser.of(CommaParser.of(DaysParser.of(AsteriskParser.of(DashParser.of(SlashParser.of(IntegerParser.of(ThrowingParser.of())))))));
        return CronParser.of(
                ImmutableSet.of(
                        CronElementConfiguration.builder()
                                .label(LABEL)
                                .position(POSITION)
                                .min(MIN)
                                .max(MAX)
                                .partParser(cronParser)
                                .build()
                )
        );
    }
}