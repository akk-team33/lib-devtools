package de.team33.devtools.buildable.luna;

import de.team33.patterns.collection.ceres.Collecting;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.team33.devtools.buildable.luna.GeneratorUtil.separated;

/**
 * A tool for generating Java code for immutable but buildable data object classes based on existing rudimentary
 * implementations of the respective classes. During generation, the name, package and fields of the class are taken
 * into account. Getters are generated for the field values, equals() hashCode() and toString() as well as an inner
 * builder class and one or the other factory method.
 */
public class BuildableGenerator {

    private static final String NEWLINE = String.format("%n");
    private static final String EMPTY_LINE = "";
    private static final Collection<Package> STD_PACKAGES = Arrays.asList(String.class.getPackage(), null);
    private static final Supplier<Set<String>> NEW_SET = TreeSet::new;

    private final Class<?> doClass;
    private final Package doPackage;
    private final Set<Package> stdPackages = new HashSet<>(STD_PACKAGES);

    private BuildableGenerator(final Class<?> doClass) {
        this.doClass = doClass;
        this.doPackage = doClass.getPackage();
        stdPackages.add(doPackage);
    }

    public static BuildableGenerator of(final Class<?> doClass) {
        return new BuildableGenerator(doClass);
    }

    private static <T> Predicate<T> not(final Predicate<T> predicate) {
        return predicate.negate();
    }

    private Stream<Field> fields() {
        return Stream.of(doClass.getDeclaredFields());
    }

    private Stream<String> packageCodeLines() {
        return Optional.ofNullable(doPackage)
                       .map(Package::getName)
                       .map(name -> String.format("package %s;", name))
                       .map(line -> Stream.of(line, EMPTY_LINE))
                       .orElseGet(Stream::empty);
    }

    private Stream<String> importCodeLines() {
        return Optional.of(fields().map(Field::getType)
                                   .filter(not(type -> stdPackages.contains(type.getPackage())))
                                   .map(Class::getCanonicalName)
                                   .collect(Collectors.toCollection(NEW_SET)))
                       .filter(not(Set::isEmpty))
                       .map(Set::stream)
                       .map(stream -> stream.map(name -> String.format("import %s;", name)))
                       .map(stream -> Stream.concat(stream, Stream.of(EMPTY_LINE)))
                       .orElseGet(Stream::empty);
    }

    private Stream<String> classHeadLines() {
        return Stream.of(String.format("public class %s {", doClass.getSimpleName()));
    }

    private static String classFieldLine(final Field field) {
        return String.format("    private %s %s;",
                             field.getType().getSimpleName(),
                             field.getName());
    }

    private Stream<String> classFieldsLines() {
        return Collecting.builder(() -> new LinkedList<String>())
                         .add(EMPTY_LINE)
                         .addAll(fields().map(BuildableGenerator::classFieldLine))
                         .setup(list -> {
                             if (1 == list.size()) {
                                 list.clear();
                             }
                         })
                         .build()
                         .stream();
    }

    private Stream<String> classMethodsLines() {
        return Stream.empty(); // TODO
    }

    private Stream<String> classBuilderLines() {
        return Stream.empty(); // TODO
    }

    private Stream<String> classBodyLines() {
        return Stream.of(classFieldsLines(),
                         classMethodsLines(),
                         classBuilderLines())
                     .reduce(Stream::concat)
                     .orElseGet(Stream::empty);
    }

    private Stream<String> classClosingLines() {
        return Stream.of("}");
    }

    private Stream<String> classCodeLines() {
        return Stream.of(classHeadLines(),
                         classBodyLines(),
                         classClosingLines())
                     .reduce(Stream::concat)
                     .orElseGet(Stream::empty);
    }

    public final Stream<String> javaCodeLines() {
        return Stream.of(packageCodeLines(),
                         importCodeLines(),
                         classCodeLines())
                     .reduce(Stream::concat)
                     .orElseGet(Stream::empty);
    }
}
