package cc.mrbird.febs.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MrBird
 */
@Data
public class MenuTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String href;
    private String name;
    private boolean checked = false;
    private List<MenuTree<T>> children = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private T data;

}