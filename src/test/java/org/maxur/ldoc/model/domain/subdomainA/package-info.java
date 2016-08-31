/*
 * Copyright 2016 Russian Post
 *
 * This source code is Russian Post Confidential Proprietary.
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse engineer the software.
 * Otherwise this violation would be treated by law and would be subject to legal prosecution.
 * Legal use of the software provides receipt of a license from the right holder only.
 */

/**
 * domain package contains business logic layer's classes.
 *
 * @author myunusov
 * @version 1.0
 * @since <pre>10.02.2016</pre>
 */
@BusinessDomain(
    name = "SubDomainA",
    description = "SubDomainA description"
)
@Link(related = "SubDomainB")
@Link(related = "SubDomainC")
package org.maxur.ldoc.model.domain.subdomainA;

import org.maxur.ldoc.annotation.BusinessDomain;
import org.maxur.ldoc.annotation.Link;