/*
 * Copyright 2016 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

package org.maxur.ldoc;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import lombok.extern.slf4j.Slf4j;
import org.maxur.ldoc.model.Domain;

/**
 * The type Living Documentation doclet.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>07.08.2016</pre>
 */
@Slf4j
public class LivingDocumentation {

    private static LivingDocumentation instance;

    private final Options options;

    private LivingDocumentation(final Options options) {
        this.options = options;
    }

    /**
     * Start boolean.
     *
     * @param root the root
     * @return the boolean
     */
    public static boolean start(final RootDoc root) {
        instance.createDocumentations(new Domain(root));
        return true;
    }

    /**
     * Used by javadoc to identify number of args for a given option
     *
     * @param option the option as a string
     * @return the number of expected args for the option.
     */
    public static int optionLength(final String option) {
        return Options.lengthFor(option);


    }

    /**
     * Valid options boolean.
     *
     * @param options  the options
     * @param reporter the reporter
     * @return the boolean
     */
    public static boolean validOptions(final String[][] options, final DocErrorReporter reporter){
        try {
            instance = new LivingDocumentation(new Options(options));
        } catch (IllegalStateException e) {
            log.debug(e.getMessage(), e);
            reporter.printError(e.getMessage());
            return false;
        }
        return true;
    }



    private void createDocumentations(final Domain domain) {
        if (options.isGlossary()) {
            GlossaryWriter.make(options.baseDir()).writeBy(domain);
        }
        if (options.isContextMap()) {
            ContextMapDrawer.make().drawBy(domain);
        }
    }




}
