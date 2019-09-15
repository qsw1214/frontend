package cc.mrbird.febs.resource.service.impl;

import cc.mrbird.febs.common.authentication.ShiroRealm;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.resource.entity.Category;
import cc.mrbird.febs.resource.mapper.CategoryMapper;
import cc.mrbird.febs.resource.service.ICategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lb
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private ShiroRealm shiroRealm;


    @Override
    public MenuTree<Category> findCategorys(Category category) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(category.getCategoryName())) {
            queryWrapper.lambda().like(Category::getCategoryName, category.getCategoryName());
        }
        queryWrapper.lambda().orderByAsc(Category::getOrderNum);
        List<Category> categorys = this.baseMapper.selectList(queryWrapper);
        List<MenuTree<Category>> trees = this.convertCategorys(categorys);

        return TreeUtil.buildMenuTree(trees);
    }
    
    @Override
	public List<MenuTree<Category>> findCategorys() {
        List<Category> categorys = this.baseMapper.selectList(new QueryWrapper<>());
        List<MenuTree<Category>> trees = this.convertMenus(categorys);
        return TreeUtil.buildList(trees, "0");
	}
    
    private List<MenuTree<Category>> convertMenus(List<Category> categorys){
    	List<MenuTree<Category>> trees = new ArrayList<>();
        categorys.forEach(category -> {
        	MenuTree<Category> tree = new MenuTree<>();
            tree.setId(String.valueOf(category.getCategoryId()));
            tree.setParentId(String.valueOf(category.getParentId()));
            tree.setName(category.getCategoryName());
            tree.setIcon(category.getIcon());
            tree.setData(category);
            trees.add(tree);
        });
        return trees;
    }

    @Override
    public List<Category> findCategoryList(Category category) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(category.getCategoryName())) {
            queryWrapper.lambda().like(Category::getCategoryName, category.getCategoryName());
        }
        queryWrapper.lambda().orderByAsc(Category::getCategoryId).orderByAsc(Category::getOrderNum);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createCategory(Category category) {
    	if (category.getParentId() == null)
            category.setParentId(Category.TOP_NODE);
        this.baseMapper.insert(category);
    }


    @Override
    @Transactional
    public void updateCategory(Category category) {
    	if (category.getParentId() == null)
            category.setParentId(Category.TOP_NODE);
        this.baseMapper.updateById(category);

        shiroRealm.clearCache();
    }

    @Override
    @Transactional
    public void deleteMeuns(String categoryIds) {
        String[] categoryIdsArray = categoryIds.split(StringPool.COMMA);
        this.delete(Arrays.asList(categoryIdsArray));

        shiroRealm.clearCache();
    }

    private List<MenuTree<Category>> convertCategorys(List<Category> categorys) {
        List<MenuTree<Category>> trees = new ArrayList<>();
        categorys.forEach(category -> {
            MenuTree<Category> tree = new MenuTree<>();
            tree.setId(String.valueOf(category.getCategoryId()));
            tree.setParentId(String.valueOf(category.getParentId()));
            tree.setName(category.getCategoryName());
            tree.setIcon(category.getIcon());
            tree.setData(category);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> categoryIds) {
        removeByIds(categoryIds);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Category::getParentId, categoryIds);
        List<Category> categorys = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(categorys)) {
            List<String> categoryIdList = new ArrayList<>();
            categorys.forEach(m -> categoryIdList.add(String.valueOf(m.getCategoryId())));
            this.delete(categoryIdList);
        }
    }

}

