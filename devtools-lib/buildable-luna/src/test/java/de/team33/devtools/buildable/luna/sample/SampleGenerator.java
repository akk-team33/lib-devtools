package de.team33.devtools.buildable.luna.sample;

import de.team33.devtools.buildable.luna.BuildableGenerator;

public class SampleGenerator {

    public static void main(final String[] args) throws ClassNotFoundException {

        final Class<?> doClass = Class.forName(args[0]);
        BuildableGenerator.of(doClass)
                          .javaCodeLines()
                          .forEach(System.out::println);
    }
}
