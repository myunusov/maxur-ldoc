package org.maxur.ldoc;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextWriter {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public TextWriter() {
    }

    @NotNull
    Path write(final Path path, final String text) throws IOException {
        return Files.write(path, text.getBytes(CHARSET));
    }
}