package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CarFilter {
    private final String field;
    private final Operation operation;
    private final String value;
    private final Set<String> orValues;

    enum Operation {
        EQUAL("equals"),
        NOTEQUAL("notequals"),
        GREATERTHAN("greaterthan"),
        LESSTHAN("lessthan");

        private final String jsonString;

        Operation(final String jsonString) {
            this.jsonString = jsonString;
        }

        private static Operation fromJsonString(final String jsonString) {
            for (final Operation operation : values()) {
                if (operation.jsonString.equalsIgnoreCase(jsonString)) return operation;
            }
            return null;
        }

        private Predicate<Car> predicate(final CarFilter carFilter) {
            return car -> {
                if (carFilter.operation == null) return true;
                try {
                    final Field classField = Car.class.getDeclaredField(carFilter.field);
                    classField.setAccessible(true);

                    // TODO: 09/14/17 This feels hacky, and should probably be made more elegant.
                    final String fieldValue;
                    if (carFilter.field.equalsIgnoreCase("carClass")) {
                        final CarClass carClass = (CarClass) classField.get(car);
                        fieldValue = carClass.getName();
                    } else {
                        fieldValue = classField.get(car).toString().toLowerCase();
                    }

                    switch (carFilter.operation) {
                        case EQUAL:
                            if (carFilter.value == null) {
                                return carFilter.orValues.contains(fieldValue);
                            } else {
                                return fieldValue.equalsIgnoreCase(carFilter.value);
                            }

                        case NOTEQUAL:
                            if (carFilter.value == null) {
                                return !carFilter.orValues.contains(fieldValue);
                            } else {
                                return !fieldValue.equalsIgnoreCase(carFilter.value);
                            }

                        case GREATERTHAN:
                            return carFilter.value != null && Integer.valueOf(fieldValue) > Integer.valueOf(carFilter.value);

                        case LESSTHAN:
                            return carFilter.value != null && Integer.valueOf(fieldValue) < Integer.valueOf(carFilter.value);

                        default:
                            return false;
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    return false;
                }
            };
        }
    }

    public CarFilter(
            @JsonProperty("field") final String field,
            @JsonProperty("operation") final String operation,
            @JsonProperty("value") final String value,
            @JsonProperty("orValues") final List<String> orValues) {
                this.field = field;
        this.operation = Operation.fromJsonString(operation);
                this.value = value;
        this.orValues = orValues == null ? null : orValues.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new));
    }

    Predicate<Car> getOperation() {
        return operation.predicate(this);
    }
}
