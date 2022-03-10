package com.gapache.commons.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author HuSen
 * create on 2019/9/11 14:25
 */
@Getter
@Setter
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 245358283249310354L;

    private long total;
    private List<T> items;

    public static <T, PO> PageResult<T> of(long total, Function<PO, T> function, List<PO> data) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setItems(data.stream().map(function).collect(Collectors.toList()));
        return result;
    }

    public static <T, PO> PageResult<T> of(long total, Function<PO, T> function, Stream<PO> data) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setItems(data.map(function).collect(Collectors.toList()));
        return result;
    }

    public static <T> PageResult<T> empty() {
        PageResult<T> result = new PageResult<>();
        result.setTotal(0);
        result.setItems(Collections.emptyList());
        return result;
    }
}
