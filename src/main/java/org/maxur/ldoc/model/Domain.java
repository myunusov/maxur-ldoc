package org.maxur.ldoc.model;

import com.sun.javadoc.RootDoc;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Domain.
 *
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>8/29/2016</pre>
 */
public class Domain {

    @Getter
    @NotNull
    private final Collection<SubDomain> domains;

    @Getter
    @NotNull
    private final Set<BiLink> links;

    /**
     * Instantiates a new Domain.
     *
     * @param root the root
     */
    public Domain(final RootDoc root) {
        domains = collectDomainModels(root);
        links = collectLinks(domains);
    }

    @NotNull
    private Set<SubDomain> collectDomainModels(final RootDoc root) {
        return Arrays.stream(root.specifiedPackages())
            .map(SubDomain::makeBy)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

    @NotNull
    private Set<BiLink> collectLinks(final Collection<SubDomain> domains) {
        final Map<BiLink, BiLink> result = new HashMap<>();
        for (SubDomain domain : domains) {
            for (SubDomain.LinkModel link : domain.getLinks()) {
                final BiLink newLink = new BiLink(domain.getId(), link.getRelated(), link.getLabel());
                final BiLink oldLink = result.get(newLink);
                if (oldLink != null) {
                    final BiLink value = oldLink.mergeWith(newLink);
                    result.remove(oldLink);
                    result.put(value, value);
                } else {
                    result.put(newLink, newLink);
                }
            }
        }
        return result.keySet();
    }


}
