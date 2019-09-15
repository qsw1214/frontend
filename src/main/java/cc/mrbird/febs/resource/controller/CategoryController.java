package cc.mrbird.febs.resource.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.resource.entity.Category;
import cc.mrbird.febs.resource.service.ICategoryService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 资源类别
 * @author lb
 */
@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController extends BaseController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("tree")
    public FebsResponse getMenuTree(Category category) throws FebsException {
        try {
            MenuTree<Category> categorys = this.categoryService.findCategorys(category);
            return new FebsResponse().success().data(categorys.getChildren());
        } catch (Exception e) {
            String message = "获取菜单树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    
    @GetMapping("select/tree")
    public List<MenuTree<Category>> getDeptTree() throws FebsException {
        try {
            return this.categoryService.findCategorys();
        } catch (Exception e) {
            String message = "获取类别树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("新增类别")
    @PostMapping
    @RequiresPermissions("category:add")
    public FebsResponse addCategory(@Valid Category category) throws FebsException {
        try {
            this.categoryService.createCategory(category);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增类别失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除类别")
    @GetMapping("delete/{categoryIds}")
    @RequiresPermissions("category:delete")
    public FebsResponse deleteCategorys(@NotBlank(message = "{required}") @PathVariable String categoryIds) throws FebsException {
        try {
            this.categoryService.deleteMeuns(categoryIds);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除类别失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改类别")
    @PostMapping("update")
    @RequiresPermissions("category:update")
    public FebsResponse updateCategory(@Valid Category category) throws FebsException {
        try {
            this.categoryService.updateCategory(category);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改类别失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("category:export")
    public void export(Category category, HttpServletResponse response) throws FebsException {
        try {
            List<Category> categorys = this.categoryService.findCategoryList(category);
            ExcelKit.$Export(Category.class, response).downXlsx(categorys, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}

