package com.senorpez.projectcars.staticdata;

import org.springframework.hateoas.Identifiable;

import java.util.Collection;
import java.util.List;

interface APIService<
        T,
        M extends Identifiable<Integer>,
        C extends Identifiable<Integer>> {
    M findOne(final Collection<T> entities, final int searchId);
    List<C> findAll(final Collection<T> entities);
}