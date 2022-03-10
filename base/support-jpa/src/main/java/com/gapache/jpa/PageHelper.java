package com.gapache.jpa;

import com.gapache.commons.model.IPageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author HuSen
 * create on 2020/5/6 2:05 下午
 */
public class PageHelper {

    public static Pageable of(IPageRequest<?> pageRequest) {
        Integer page = pageRequest.getPage();
        Integer number = pageRequest.getNumber();
        Boolean asc = pageRequest.getAsc();
        String sort = pageRequest.getSort();
        if (StringUtils.isNotBlank(sort)) {
            return PageRequest.of(page - 1, number, asc == null || !asc ? Sort.by(sort).descending() : Sort.by(sort).ascending());
        } else {
            return PageRequest.of(page - 1, page);
        }
    }
}
