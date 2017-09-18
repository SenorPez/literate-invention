package com.senorpez.projectcars.staticdata;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class APIService {
    <T> T findOne(final Collection<T> collection, final Predicate<T> predicate, final Supplier<RuntimeException> exceptionSupplier) {
        return collection.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(exceptionSupplier);
    }
}
