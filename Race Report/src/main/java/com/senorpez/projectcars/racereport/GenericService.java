package com.senorpez.projectcars.racereport;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.util.List;

public interface GenericService<T, U> {
    Resources<Resource<U>> findAll();

    List<Resource<U>> convertToModels(final List<T> entities);
    Resource<U> convertToModel(final int index, final T entity);
}
