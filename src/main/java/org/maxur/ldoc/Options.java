package org.maxur.ldoc;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
class Options {

    private static final String BASEDIR = "-basedir";
    private static final String ALL = "-all";
    private static final String CONTEXT_MAP = "-contextmap";
    private static final String GLOSSARY = "-glossary";

    private final Map<String, String> optionsMap;

    Options(final String[][] options) {
        Arrays.stream(options).forEach(
            option -> log.debug(valueOf(option))
        );

        optionsMap = Arrays.stream(options)
            .filter(option -> option.length == 2)
            .collect(
                Collectors.toMap(
                    option -> option[0].toLowerCase(), option -> option[1], (s1, s2) -> s2
                )
            );

        Arrays.stream(options)
            .filter(option -> option.length == 1)
            .forEach(option -> optionsMap.put(option[0].toLowerCase(), null));

    }

    private String valueOf(String[] option) {
        StringBuilder result = new StringBuilder(option.length == 1 ? option[0] : option[0] + " = ");
        for (int i = 1; i < option.length; i++) {
            result.append(option[i]).append(" ");
        }
        return result.toString();
    }

    static int lengthFor(final String option) {
        switch (option.toLowerCase()) {
            case BASEDIR:
                return 2;
            case ALL:
            case CONTEXT_MAP:
            case GLOSSARY:
                return 1;
            default:
                throw new IllegalStateException(format("Illegal option (%s) of LivingDocumentation doclet", option));
        }


    }

    String baseDir() {
        return optionsMap.get(BASEDIR);
    }

    boolean isGlossary() {
        return optionsMap.containsKey(ALL) || optionsMap.containsKey(GLOSSARY);
    }

    boolean isContextMap() {
        return optionsMap.containsKey(ALL) || optionsMap.containsKey(CONTEXT_MAP);
    }
}