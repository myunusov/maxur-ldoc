package org.maxur.ldoc;

import com.github.jabbalaci.graphviz.GraphViz;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.maxur.ldoc.model.BiLink;
import org.maxur.ldoc.model.Domain;
import org.maxur.ldoc.model.SubDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class ContextMapDrawer {

    private GraphViz graphViz;

    private final boolean isSkipped;

    ContextMapDrawer(final GraphViz graphViz, boolean isSkipped) {
        this.graphViz = graphViz;
        this.isSkipped = isSkipped;
    }

    static ContextMapDrawer make(final Options options) {
        boolean isSkipped = !options.isContextMap();
        return new ContextMapDrawer(new GraphViz(), isSkipped);
    }

    void drawBy(final Domain domains) {
        if (isSkipped) {
            return;
        }

        graphViz.setImageDpi(GraphViz.DpiSizes.DPI_249);

        graphViz.startGraph("ContextMap");

        for (SubDomain domain : domains.getSubDomains()) {
            graphViz.node(domain.getId(), domain.getTitle());
        }

        for (BiLink link : domains.getLinks()) {
            log.debug(link.toString());
            final List<String> attr = new ArrayList<String>();

            if (!Strings.isNullOrEmpty(link.getLabel())) {
                attr.add(String.format("[label=\"%s\"]", link.getLabel()));
            }

            if (link.isBiDirection()) {
                attr.add("[dir=\"both\"]");
            }

            graphViz.addln(String.format("%s -> %s %s;",
                link.getLeftId(),
                link.getRightId(),
                attr.stream().collect(Collectors.joining()))
            );
        }

        graphViz.endGraph();
        log.debug(graphViz.source());

        graphViz.writeTo("contextMap");
    }
}