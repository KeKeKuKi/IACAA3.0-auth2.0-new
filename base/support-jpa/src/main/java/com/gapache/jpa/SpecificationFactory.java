package com.gapache.jpa;

import com.gapache.commons.function.ThreeConsumer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/1/18 10:13 上午
 */
public final class SpecificationFactory {

    public static <T> Specification<T> produce(ThreeConsumer<List<Predicate>, Root<T>, CriteriaBuilder> consumer) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            consumer.accept(predicates, root, criteriaBuilder);
            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
}
