package jpolanco.applicationcore.user.infrastructure.adapters.output.criteria;

import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;

public class SpecBuilder<T> {
    private Specification<T> spec;

    public SpecBuilder(Specification<T> spec) {
        this.spec = spec;
    }

    public static <T> SpecBuilder<T> init() {
        Specification<T> initial = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        return new SpecBuilder<>(initial);
    }

    public SpecBuilder<T> isFalse(SingularAttribute<T, Boolean> attribute) {
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(attribute)));
        return this;
    }

    public SpecBuilder<T> isTrue(SingularAttribute<T, Boolean> attribute) {
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get(attribute)));
        return this;
    }

    public <V> SpecBuilder<T> eq(SingularAttribute<T, V> attribute, V value) {
        if (value == null) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(attribute), value));
        return this;
    }

    public <V> SpecBuilder<T> notEq(SingularAttribute<T, V> attribute, V value) {
        if (value == null) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(attribute), value));
        return this;
    }

    public SpecBuilder<T> or(Specification<T> otherSpec) {
        if (otherSpec == null) return this;
        this.spec = this.spec.or(otherSpec);
        return this;
    }

    public SpecBuilder<T> and(Specification<T> otherSpec) {
        if (otherSpec == null) return this;
        this.spec = this.spec.and(otherSpec);
        return this;
    }

    public <V> SpecBuilder<T> in(SingularAttribute<T, V> attribute, Iterable<V> values) {
        if (values == null || !values.iterator().hasNext()) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> {
            var inClause = criteriaBuilder.in(root.get(attribute));
            for (V value : values) {
                inClause.value(value);
            }
            return inClause;
        });
        return this;
    }

    public SpecBuilder<T> startsWith(SingularAttribute<T, String> attribute, String value) {
        if (value == null || value.isBlank()) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute)), value.toLowerCase() + "%"));
        return this;
    }

    public SpecBuilder<T> endsWith(SingularAttribute<T, String> attribute, String value) {
        if (value == null || value.isBlank()) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute)), "%" + value.toLowerCase()));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecBuilder<T> greaterThan(SingularAttribute<T, Y> attribute, Y value) {
        if (value == null) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(attribute), value));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecBuilder<T> lessThan(SingularAttribute<T, Y> attribute, Y value) {
        if (value == null) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(attribute), value));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecBuilder<T> greaterThanOrEqual(SingularAttribute<T, Y> attribute, Y value) {
        if (value == null) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), value));
        return this;
    }

    public <Y extends Comparable<? super Y>> SpecBuilder<T> lessThanOrEqual(SingularAttribute<T, Y> attribute, Y value) {
        if (value == null) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(attribute), value));
        return this;
    }

    public SpecBuilder<T> like(SingularAttribute<T, String> attribute, String pattern) {
        if (pattern == null || pattern.isBlank()) return this;
        this.spec = this.spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute)), "%" + pattern + "%"));
        return this;
    }

    public Specification<T> build() {
        return this.spec;
    }
}
