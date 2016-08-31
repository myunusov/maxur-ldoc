package org.maxur.ldoc.model;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import com.sun.tools.javadoc.Messager;
import com.sun.tools.javadoc.ModifierFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.maxur.ldoc.LivingDocumentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class DomainTest {

    private static final Logger logger = Logger.getLogger(DomainTest.class.getName());

    private DocErrorReporter reporter;

    private RootDoc rootDocImpl;

    @Before
    public void setUp() throws Exception {
        Context context = new Context();
        Options compOpts = Options.instance(context);
        compOpts.put("-sourcepath", "src/test/java");

        new PublicMessager(context, "test",
            new PrintWriter(new LogWriter(Level.SEVERE), true),
            new PrintWriter(new LogWriter(Level.WARNING), true),
            new PrintWriter(new LogWriter(Level.FINE), true)
        );

        final JavadocTool javadocTool = JavadocTool.make0(context);
        final ListBuffer<String> javaNames = new ListBuffer<>();
        javaNames.append("org.maxur.ldoc.model.domain.subdomainA");
        javaNames.append("org.maxur.ldoc.model.domain.subdomainA.subdir");
        javaNames.append("org.maxur.ldoc.model.domain.subdomainB");
        javaNames.append("org.maxur.ldoc.model.domain.subdomainC");
        final ListBuffer<String[]> options = new ListBuffer<>();
        final ListBuffer<String> packageNames = new ListBuffer<>();
        final ListBuffer<String> excludedPackages = new ListBuffer<>();

        rootDocImpl = javadocTool.getRootDocImpl(
            "en",
            "",
            new ModifierFilter(ModifierFilter.ALL_ACCESS),
            javaNames.toList(),
            options.toList(),
            Collections.emptyList(),
            false,
            packageNames.toList(),
            excludedPackages.toList(),
            false,
            false,
            false
        );
        logger.info(rootDocImpl.getRawCommentText());

    }

    @Test
    public void name() throws Exception {
        LivingDocumentation.optionLength("-all");
        LivingDocumentation.validOptions(new String[][]{new String[] {"-all"}}, reporter);
        LivingDocumentation.start(rootDocImpl);
    }

    private static class LogWriter extends Writer {

        Level level;

        LogWriter(Level level) {
            this.level = level;
        }

        @Override
        public void write(char[] chars, int offset, int length) throws IOException {
            String s = new String(Arrays.copyOf(chars, length));
            if (!s.equals("\n"))
                logger.log(level, s);
        }
        @Override
        public void flush() throws IOException {}
        @Override
        public void close() throws IOException {}
    }

    private static class PublicMessager extends Messager {

        PublicMessager(
            Context context,
            String s,
            PrintWriter printWriter,
            PrintWriter printWriter1,
            PrintWriter printWriter2
        ) {
            super(context, s, printWriter, printWriter1, printWriter2);
        }
    }

}