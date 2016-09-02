package org.maxur.ldoc;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.maxur.ldoc.model.Domain;
import org.maxur.ldoc.model.SubDomain;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

@Slf4j
class GlossaryWriter {

    private static final String GLOSSARY_NAME_TEMPLATE = "%s-glossary.md";

    private final TextWriter textWriter;

    private final boolean isSkipped;

    private final Template template;

    GlossaryWriter(final TextWriter textWriter, final boolean isSkipped, final String baseDir) {
        this.textWriter = textWriter;
        this.isSkipped = isSkipped;
        template = isSkipped ? null : findTemplate(baseDir);
    }

    static GlossaryWriter make(final Options options) {
        boolean isSkipped = !options.isGlossary();
        String baseDir = options.baseDir();
        return new GlossaryWriter(new TextWriter(), isSkipped, baseDir);
    }

    /**
     * Write glossary.
     */
    void writeBy(final Domain domain) {
        if (isSkipped) {
            return;
        }
        domain.getSubDomains().forEach(
            subDomain -> writeGlossary(template, subDomain)
        );
    }

    private void writeGlossary(final Template template, final SubDomain domain) {
        final String glossary = createGlossary(template, domain);
        writeGlossary(domain, glossary);
    }

    private void writeGlossary(SubDomain domain, String glossary) {
        final Path path = Paths.get(String.format(GLOSSARY_NAME_TEMPLATE, domain.getName()));
        try {
            textWriter.write(path, glossary);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            throw new IllegalStateException(
                format("Glossary cannot be write to file '%s'", path)
            );
        }
    }

    private String createGlossary(final Template template, final SubDomain domain) {
        final String glossary;
        try {
            glossary = template.apply(domain);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            throw new IllegalStateException(
                format("Glossary Template is not applicable: %s", e.getMessage())
            );
        }
        return glossary;
    }

    private Template findTemplate(String basedirPath) {
        try {
            return handlebars(basedir(basedirPath)).compile("glossary");
        } catch (IOException e) {
            throw new IllegalStateException(
                format("Glossary Template is not found or is not accessible: %s", e.getMessage()),
                e
            );
        }
    }

    private static File basedir(final String pathname) {
        if (pathname == null) {
            return null;
        }
        final File dir = new File(pathname);
        if (dir.exists() && dir.isDirectory() && dir.canRead()) {
            return dir;
        } else {
            throw new IllegalStateException(
                format("Directory '%s' is not found or is not accessible", dir.getAbsolutePath())
            );
        }
    }

    @NotNull
    private static Handlebars handlebars(final File basedir) {
        return basedir == null ?
            new Handlebars() :
            new Handlebars(new FileTemplateLoader(basedir.getAbsolutePath()));
    }



}