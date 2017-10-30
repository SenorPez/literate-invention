package com.senorpez.projectcars.staticdata;

import org.springframework.restdocs.hypermedia.LinksSnippet;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;

class DocumentationCommon {
    static final LinksSnippet commonLinks = links(halLinks(),
            linkWithRel("self").ignored(),
            linkWithRel("index").ignored(),
            linkWithRel("curies").ignored());

}
