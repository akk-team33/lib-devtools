package de.team33.devtools.buildable.luna;

import de.team33.patterns.collection.ceres.Collecting;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.devtools.buildable.luna.GeneratorUtil.separated;

public class BuildableGenerator {

    private static final String NEWLINE = String.format("%n");
    private static final Collection<Package> STD_PACKAGES = Arrays.asList(String.class.getPackage(), null);

    private final Class<?> doClass;
    private final Set<Package> stdPackages = new HashSet<>(STD_PACKAGES);

    private BuildableGenerator(final Class<?> doClass) {
        this.doClass = doClass;
        stdPackages.add(doClass.getPackage());
    }

    public static BuildableGenerator of(final Class<?> doClass) {
        return new BuildableGenerator(doClass);
    }

    private Stream<Field> fields() {
        return Stream.of(doClass.getDeclaredFields());
    }

    public final String javaCode() {
        final List<String> lines = //
                Collecting.builder(() -> new LinkedList<String>())
                          .addAll(packageCode())
                          .addAll(importCode())
                          .add(String.format("public class %s {", doClass.getSimpleName()))
                          .add("")
                          .addAll(fieldCode())
                          .add("}")
                          .build();
        return String.join(NEWLINE, lines);
    }

    private Collection<String> importCode() {
        return separated(fields().map(Field::getType)
                                 .filter(this::isNotStdPackage)
                                 .distinct()
                                 .map(Class::getCanonicalName)
                                 .sorted()
                                 .map(name -> String.format("import %s;", name))
                                 .collect(Collectors.toList()));
    }

    private Collection<String> packageCode() {
        return separated(String.format("package %s;", doClass.getPackage().getName()));
    }

    private Collection<String> fieldCode() {
        return Collecting.builder(() -> new LinkedList<String>())
                         .build();
    }

    private boolean isNotStdPackage(final Class<?> type) {
        return !stdPackages.contains(type.getPackage());
    }
}
