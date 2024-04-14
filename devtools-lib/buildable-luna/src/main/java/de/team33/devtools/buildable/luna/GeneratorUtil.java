package de.team33.devtools.buildable.luna;

import de.team33.patterns.collection.ceres.Collecting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public final class GeneratorUtil {

    private GeneratorUtil() {
    }

    public static Collection<String> separated(final String line) {
        return separated(Optional.ofNullable(line)
                                 .map(Collections::singleton)
                                 .orElseGet(Collections::emptySet));
    }

    public static Collection<String> separated(final Collection<String> lines) {
        if (lines.isEmpty()) {
            return lines;
        } else {
            return Collecting.builder(() -> new ArrayList<String>(lines.size() + 1))
                             .addAll(lines)
                             .add("")
                             .build();
        }
    }
}
