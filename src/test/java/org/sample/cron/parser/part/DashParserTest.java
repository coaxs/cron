package org.sample.cron.parser.part;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@Tag("unit")
class DashParserTest {
    private PartParser fakeSuccessor;

    @BeforeEach
    void setUp() {
        fakeSuccessor = mock(PartParser.class);
    }

    @Test
    void parse_whenCronPartDoesNotContainDash_shouldPassProcessingToSuccessor() {
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        val cronPart = "10";
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val dashParser = DashParser.of(fakeSuccessor);

        dashParser.parse(fakeCronPartWithLimits);

        verify(fakeSuccessor).parse(fakeCronPartWithLimits);
    }

    @Test
    void parse_whenCronPartContainsDashAndValidArguments_shouldReturnSetOfResults() {
        val cronPart = "10-12";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val dashParser = DashParser.of(fakeSuccessor);

        val actualResult = dashParser.parse(fakeCronPartWithLimits);

        assertThat(actualResult).containsExactly(10, 11, 12);
    }

    @Test
    void parse_whenCronPartContainsOnlyDash_shouldThrowParsingException() {
        val cronPart = "-";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val dashParser = DashParser.of(fakeSuccessor);

        val actualException = catchThrowable(() -> dashParser.parse(fakeCronPartWithLimits));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronPartContainsDashWithLeftEmptyArgument_shouldThrowParsingException() {
        val cronPart = "-4";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val dashParser = DashParser.of(fakeSuccessor);

        val actualException = catchThrowable(() -> dashParser.parse(fakeCronPartWithLimits));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronPartContainsDashWithRightEmptyArgument_shouldThrowParsingException() {
        val cronPart = "4-";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val dashParser = DashParser.of(fakeSuccessor);

        val actualException = catchThrowable(() -> dashParser.parse(fakeCronPartWithLimits));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }
}