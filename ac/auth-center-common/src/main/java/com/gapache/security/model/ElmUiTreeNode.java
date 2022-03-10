package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen
 * @since 2021/3/24 5:55 下午
 */
@Data
public class ElmUiTreeNode implements Serializable {
    private static final long serialVersionUID = 8171502749722849123L;

    private String id;

    private String label;

    private List<ElmUiTreeNode> children;

    private Object data;
}
