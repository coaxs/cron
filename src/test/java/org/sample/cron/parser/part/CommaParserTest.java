package org.sample.cron.parser.part;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.*;

@Tag("unit")
class CommaParserTest {
    private PartParser fakeSuccessor;

    @BeforeEach
    void setUp() {
        fakeSuccessor = mock(PartParser.class);
    }

    @Test
    void parse_whenCronPartDoesNotContainComa_shouldPassProcessingToSuccessor() {
        val cronPart = "10";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val comaParser = CommaParser.of(fakeSuccessor);

        comaParser.parse(fakeCronPartWithLimits);

        verify(fakeSuccessor).parse(fakeCronPartWithLimits);
    }

    @Test
    void parse_whenCronPartContainsComaAndArguments_shouldProcessEveryArgument() {
        val firstCronPart = "10";
        val secondCronPart = "12";
        val cronPart = firstCronPart + "," + secondCronPart;
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val firstExpectedCronPartWithLimits = mock(CronPartWithLimits.class);
        when(firstExpectedCronPartWithLimits.getPart()).thenReturn(firstCronPart);
        when(fakeCronPartWithLimits.withPart(firstCronPart)).thenReturn(firstExpectedCronPartWithLimits);
        val secondExpectedCronPartWithLimits = mock(CronPartWithLimits.class);
        when(secondExpectedCronPartWithLimits.getPart()).thenReturn(secondCronPart);
        when(fakeCronPartWithLimits.withPart(secondCronPart)).thenReturn(secondExpectedCronPartWithLimits);
        val comaParser = CommaParser.of(fakeSuccessor);

        comaParser.parse(fakeCronPartWithLimits);

        assertSoftly(softly -> {
                    softly.assertThatCode(
                            () -> verify(fakeSuccessor).parse(firstExpectedCronPartWithLimits)
                    ).doesNotThrowAnyException();
                    softly.assertThatCode(
                            () -> verify(fakeSuccessor).parse(secondExpectedCronPartWithLimits)
                    ).doesNotThrowAnyException();
                }
        );
    }

    @Test
    void parse_whenCronPartContainsOnlyComa_shouldThrowParsingException() {
        val cronPart = ",";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val comaParser = CommaParser.of(fakeSuccessor);

        val actualException = catchThrowable(() -> comaParser.parse(fakeCronPartWithLimits));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }

    @Test
    void parse_whenCronPartContainsComaWithOneEmptyArgument_shouldThrowParsingException() {
        val cronPart = ",4";
        val fakeCronPartWithLimits = mock(CronPartWithLimits.class);
        when(fakeCronPartWithLimits.getPart()).thenReturn(cronPart);
        val comaParser = CommaParser.of(fakeSuccessor);

        val actualException = catchThrowable(() -> comaParser.parse(fakeCronPartWithLimits));

        assertThat(actualException).isInstanceOf(ParsingException.class);
    }
}