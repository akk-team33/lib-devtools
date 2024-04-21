package de.team33.devtools.buildable.luna.publics;

import de.team33.devtools.buildable.luna.BuildableGenerator;
import de.team33.devtools.buildable.luna.sample.EmptyDO;
import de.team33.devtools.buildable.luna.sample.SampleDO;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import static de.team33.patterns.exceptional.dione.Conversion.supplier;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildableGeneratorTest {

    private static final String NEW_LINE = String.format("%n");
    private static final String EXPECTED_EMPTY_DO = //
            "package de.team33.devtools.buildable.luna.sample;%n" +
            "%n" +
            "public class EmptyDO {%n" +
            "}";
    private static final String EXPECTED_NO_PACKAGE_DO = //
            "public class NoPackageDO {%n" +
            "%n" +
            "    private int intValue;%n" +
            "    private String stringValue;%n" +
            "    private Double doubleValue;%n" +
            "}";
    private static final String EXPECTED_SAMPLE_DO = //
            "package de.team33.devtools.buildable.luna.sample;%n" +
            "%n" +
            "import java.math.BigInteger;%n" +
            "import java.time.Instant;%n" +
            "%n" +
            "public class SampleDO {%n" +
            "%n" +
            "    private int intValue;%n" +
            "    private String stringValue;%n" +
            "    private Double doubleValue;%n" +
            "    private BigInteger bigIntegerValue;%n" +
            "    private Instant instantValue;%n" +
            "}";

    @ParameterizedTest
    @EnumSource
    final void javaCodeLines(final JavaCodeLinesCase testCase) {
        final String result = BuildableGenerator.of(testCase.doClass.get())
                                                .javaCodeLines()
                                                .collect(Collectors.joining(NEW_LINE));
        assertEquals(testCase.expected, result);
    }

    @SuppressWarnings("unused")
    private enum JavaCodeLinesCase {

        EMPTY_DO(() -> EmptyDO.class, EXPECTED_EMPTY_DO),
        NO_PACKAGE_DO(supplier(() -> Class.forName("NoPackageDO")), EXPECTED_NO_PACKAGE_DO),
        SAMPLE_DO(() -> SampleDO.class, EXPECTED_SAMPLE_DO);

        private final Supplier<Class<?>> doClass;
        private final String expected;

        JavaCodeLinesCase(final Supplier<Class<?>> doClass, final String expected) {
            this.doClass = doClass;
            this.expected = String.format(expected);
        }
    }
}
