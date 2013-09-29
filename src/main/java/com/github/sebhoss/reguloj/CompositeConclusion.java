/*
 * Copyright © 2010 Sebastian Hoß <mail@shoss.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.github.sebhoss.reguloj;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;

final class CompositeConclusion<TOPIC> implements Conclusion<TOPIC> {

    private final Collection<Conclusion<TOPIC>> conclusions;

    CompositeConclusion(final Collection<Conclusion<TOPIC>> conclusions) {
        Preconditions.checkArgument(!conclusions.isEmpty());

        this.conclusions = conclusions;
    }

    @Override
    public boolean apply(final TOPIC topic) {
        return FluentIterable.from(conclusions).filter(Conclusions.conlusionApplies(topic)).size() > 0;
    }

}
