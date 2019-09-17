package cc.mrbird.febs.resource.service;

import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.resource.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lb
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 查找所有的类别 （树形结构）
     *
     * @return MenuTree<Category>
     */
    MenuTree<Category> findCategorys(Category category);
    
    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<MenuTree<Category>> findCategorys();

    /**
     * 查找所有的类别
     *
     * @return MenuTree<Category>
     */
    List<Category> findCategoryList(Category category);

    /**
     * 新增类别
     *
     * @param category 类别对象
     */
    void createCategory(Category category);

    /**
     * 修改类别
     *
     * @param category 类别对象
     */
    void updateCategory(Category category);

    /**
     * 删除类别
     *
     * @param categoryIds 类别id
     */
    void deleteMeuns(String categoryIds);
}
