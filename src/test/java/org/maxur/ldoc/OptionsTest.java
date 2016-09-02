package org.maxur.ldoc;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;

public class OptionsTest {

    @Test
    public void testDefault() throws Exception {
        final String[][] options = {};
        Options opt = new Options(options);
        assertThat(opt.baseDir()).isNull();
        assertThat(opt.isContextMap()).isFalse();
        assertThat(opt.isGlossary()).isFalse();
    }

    @Test
    public void testAll() throws Exception {
        final String[][] options = {
            new String[]{"-all"}
        };
        Options opt = new Options(options);
        assertThat(opt.isContextMap()).isTrue();
        assertThat(opt.isGlossary()).isTrue();
    }

    @Test
    public void testContextMap() throws Exception {
        final String[][] options = {
            new String[]{"-ContextMap"}
        };
        Options opt = new Options(options);
        assertThat(opt.isContextMap()).isTrue();
        assertThat(opt.isGlossary()).isFalse();
    }

    @Test
    public void testGlossary() throws Exception {
        final String[][] options = {
            new String[]{"-Glossary"}
        };
        Options opt = new Options(options);
        assertThat(opt.isContextMap()).isFalse();
        assertThat(opt.isGlossary()).isTrue();
    }

    @Test
    public void testBaseDir() throws Exception {
        final String[][] options = {
            new String[]{"-BaseDir", "/"}
        };
        Options opt = new Options(options);
        assertThat(opt.baseDir()).isEqualToIgnoringCase("/");
    }


    @Test
    public void testValidOptions() throws Exception {
        final String[][] options = {
            new String[]{"-all"},
            new String[]{"-BaseDir", "/"}
        };
        Options.validate(options);
    }

    @Test
    public void testInvalidOptionsWithAllAndGlossary() throws Exception {
        final String[][] options = {
            new String[]{"-all"},
            new String[]{"-Glossary"},
            new String[]{"-BaseDir", "/"}
        };
        Throwable thrown = catchThrowable(() -> Options.validate(options));
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("or -all or -glossary");
    }

    @Test
    public void testInvalidOptionsWithAllAndContextMap() throws Exception {
        final String[][] options = {
            new String[]{"-all"},
            new String[]{"-ContextMap"},
            new String[]{"-BaseDir", "/"}
        };
        Throwable thrown = catchThrowable(() -> Options.validate(options));
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("or -all or -contextMap");
    }

    @Test
    public void testInvalidOptionsWithEmptyBaseDir() throws Exception {
        final String[][] options = {
            new String[]{"-all"},
            new String[]{"-BaseDir", ""}
        };
        Throwable thrown = catchThrowable(() -> Options.validate(options));
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("is not found");
    }


}