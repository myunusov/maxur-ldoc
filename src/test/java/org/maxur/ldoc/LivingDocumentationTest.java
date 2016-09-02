package org.maxur.ldoc;

import com.github.jabbalaci.graphviz.GraphViz;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javadoc.JavadocTool;
import com.sun.tools.javadoc.Messager;
import com.sun.tools.javadoc.ModifierFilter;
import lombok.extern.slf4j.Slf4j;
import mockit.Delegate;
import mockit.Expectations;
import mockit.integration.junit4.JMockit;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;

@Slf4j
@RunWith(JMockit.class)
public class LivingDocumentationTest {

    private static final Logger logger = Logger.getLogger(LivingDocumentationTest.class.getName());

    private GraphViz graphViz = new GraphViz();

    private TextWriter textWriter = new TextWriter();

    private Supplier<RootDoc> rootMaker;

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
        javaNames.append("org.maxur.ldoc.model.domain.subdomainB");
        javaNames.append("org.maxur.ldoc.model.domain.subdomainB.subdir");
        javaNames.append("org.maxur.ldoc.model.domain.subdomainC");
        final ListBuffer<String[]> options = new ListBuffer<>();
        final ListBuffer<String> packageNames = new ListBuffer<>();
        final ListBuffer<String> excludedPackages = new ListBuffer<>();

        rootMaker = () -> {
            try {
                return javadocTool.getRootDocImpl(
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
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    @Test
    public void testOptionLength() throws Exception {
        assertThat(LivingDocumentation.optionLength("-basedir")).isEqualTo(2);
        assertThat(LivingDocumentation.optionLength("-all")).isEqualTo(1);
        assertThat(LivingDocumentation.optionLength("-contextmap")).isEqualTo(1);
        assertThat(LivingDocumentation.optionLength("-glossary")).isEqualTo(1);

        Throwable thrown = catchThrowable(() -> LivingDocumentation.optionLength("-invalid"));
        assertThat(thrown).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("-invalid");
    }


    @Test
    public void testOptionValid() throws Exception {
        assertThat(LivingDocumentation.validOptions(new String[][]{
                new String[]{"-all"}
            },
            null)).isTrue();

    }

    @Test
    public void testCreateDocumentations() throws Exception {
        final ContextMapDrawer contextMapDrawer = new ContextMapDrawer(graphViz, false);
        final GlossaryWriter glossaryWriter = new GlossaryWriter(textWriter, false, null);
        LivingDocumentation livingDocumentation =
            new LivingDocumentation(contextMapDrawer, glossaryWriter);

        new Expectations(graphViz) {{
            graphViz.writeTo("contextMap");
            times = 1;
        }};
        new Expectations(textWriter) {{
            textWriter.write(Paths.get("Subdomaina-glossary.md"),
                with(new Delegate<String>() {
                         public void validate(String arg) {
                             assertThat(arg).contains("SubDomainA");
                             assertThat(arg).contains("ConceptA");
                             assertThat(arg).contains("ConceptExample");
                         }
                     }
                )
            );
            times = 1;
            textWriter.write(Paths.get("Subdomainb-glossary.md"),
                with(new Delegate<String>() {
                         public void validate(String arg) {
                             assertThat(arg).contains("SubDomainB");
                             assertThat(arg).contains("ConceptB");
                             assertThat(arg).contains("ConceptExample");
                         }
                     }
                )
            );
            times = 1;
            textWriter.write(Paths.get("Subdomainc-glossary.md"),
                with(new Delegate<String>() {
                         public void validate(String arg) {
                             assertThat(arg).contains("SubDomainC");
                         }
                     }
                )
            );
            times = 1;
        }};

        livingDocumentation.createDocumentations(rootMaker.get());
    }

    private static class LogWriter extends Writer {

        private final Level level;

        LogWriter(Level level) {
            this.level = level;
        }

        @Override
        public void write(@NotNull char[] chars, int offset, int length) throws IOException {
            String s = new String(Arrays.copyOf(chars, length));
            if (!s.equals("\n"))
                logger.log(level, s);
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
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