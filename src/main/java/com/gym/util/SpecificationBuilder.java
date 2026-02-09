package com.gym.util;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    public SpecificationBuilder<T> equals(String field, Object value) {
        if (value != null) {
            specifications.add((root, query, cb) ->
                    cb.equal(getPath(root, field), value)
            );
        }
        return this;
    }

    public SpecificationBuilder<T> likeIgnoreCase(String field, String value) {
        if (value != null && !value.isBlank()) {
            specifications.add((root, query, cb) ->
                    cb.like(
                            cb.lower(getPath(root, field).as(String.class)),
                            "%" + value.toLowerCase() + "%"
                    )
            );
        }
        return this;
    }

    public SpecificationBuilder<T> orLikeIgnoreCase(List<String> fields, String value) {
        if (value != null && !value.isBlank()) {
            specifications.add((root, query, cb) -> {

                List<Predicate> predicates = new ArrayList<>();

                for (String field : fields) {
                    predicates.add(
                            cb.like(
                                    cb.lower(getPath(root, field).as(String.class)),
                                    "%" + value.toLowerCase() + "%"
                            )
                    );
                }

                return cb.or(predicates.toArray(new Predicate[0]));
            });
        }
        return this;
    }

    public Specification<T> build() {
        return specifications.stream()
                .reduce((root, query, cb) -> cb.conjunction(), Specification::and);
    }

    private Path<?> getPath(Root<T> root, String field) {

        if (field.contains(".")) {
            String[] parts = field.split("\\.");
            Path<?> path = root.get(parts[0]);

            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }

            return path;
        }

        return root.get(field);
    }
}