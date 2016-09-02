package org.maxur.ldoc;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

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

    static void validate(String[][] options) {
        final Set<String> strings = Arrays.stream(options)
            .map(o -> o[0].toLowerCase())
            .collect(toSet());

        if (strings.contains(ALL)) {
            if (strings.contains(CONTEXT_MAP)) {
                throw new IllegalStateException("Invalid options of LivingDocumentation doclet. " +
                    "Must be or -all or -contextMap but not both");
            }
            if (strings.contains(GLOSSARY)) {
                throw new IllegalStateException("Invalid options of LivingDocumentation doclet. " +
                    "Must be or -all or -glossary but not both");
            }
        }

        if (strings.contains(BASEDIR)) {
            final String basePath = Arrays.stream(options)
                .filter(o -> Objects.equals(BASEDIR, o[0].toLowerCase()))
                .findFirst()
                .map(o -> o[1]).orElseThrow(
                    () -> new IllegalStateException("Option -basedir is invalid. Path not found")
                );

            final File dir = new File(basePath);
            if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) {
                throw new IllegalStateException(
                    format("Directory '%s' is not found or is not accessible", dir.getAbsolutePath())
                );
            }
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