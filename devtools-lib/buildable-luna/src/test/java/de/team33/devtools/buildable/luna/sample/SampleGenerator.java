package de.team33.devtools.buildable.luna.sample;

import de.team33.devtools.buildable.luna.BuildableGenerator;

public class SampleGenerator {

    public static void main(String[] args) {
        System.out.println(BuildableGenerator.of(SampleDO.class)
                                             .javaCode());
    }
}
