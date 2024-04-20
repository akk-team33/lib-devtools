package de.team33.devtools.buildable.luna.publics;

import de.team33.devtools.buildable.luna.BuildableGenerator;
import de.team33.devtools.buildable.luna.sample.EmptyDO;
import de.team33.devtools.buildable.luna.sample.SampleDO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildableGeneratorTest {

    private static final String NEW_LINE = String.format("%n");
    private static final String EXPECTED_EMPTY_DO = //
            "package de.team33.devtools.buildable.luna.sample;%n" +
            "%n" +
            "public class EmptyDO {%n" +
            "%n" +
            "}";
    private static final String EXPECTED_SAMPLE_DO = //
            "package de.team33.devtools.buildable.luna.sample;%n" +
            "%n" +
            "import java.math.BigInteger;%n" +
            "import java.time.Instant;%n" +
            "%n" +
            "public class SampleDO {%n" +
            "%n" +
            "    int intValue;%n" +
            "    String stringValue;%n" +
            "    Double doubleValue;%n" +
            "    BigInteger bigIntegerValue;%n" +
            "    Instant instantValue;%n" +
            "}";

    @ParameterizedTest
    @EnumSource
    final void javaCodeLines(final JavaCodeLinesCase testCase) {
        final String result = BuildableGenerator.of(testCase.doClass)
                                                .javaCodeLines()
                                                .collect(Collectors.joining(NEW_LINE));
        assertEquals(testCase.expected, result);
    }

    private enum JavaCodeLinesCase {

        EMPTY_DO(EmptyDO.class, EXPECTED_EMPTY_DO),
        SAMPLE_DO(SampleDO.class, EXPECTED_SAMPLE_DO);

        private final Class<?> doClass;
        private final String expected;

        JavaCodeLinesCase(final Class<?> doClass, final String expected) {
            this.doClass = doClass;
            this.expected = String.format(expected);
        }
    }
}
