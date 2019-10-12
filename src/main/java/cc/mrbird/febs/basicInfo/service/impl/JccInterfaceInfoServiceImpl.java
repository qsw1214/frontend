package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.JccInterfaceInfo;
import cc.mrbird.febs.basicInfo.mapper.JccInterfaceInfoMapper;
import cc.mrbird.febs.basicInfo.service.IJccInterfaceInfoService;
import cc.mrbird.febs.common.authentication.ShiroRealm;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.resource.entity.Category;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Service实现
 *
 * @author lb
 * @date 2019-10-11 09:27:33
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JccInterfaceInfoServiceImpl extends ServiceImpl<JccInterfaceInfoMapper, JccInterfaceInfo> implements IJccInterfaceInfoService {

    @Autowired
    private JccInterfaceInfoMapper jccInterfaceInfoMapper;
    @Autowired
    private ShiroRealm shiroRealm;

    /**
     * 查找所有的类别
     * @param jccInterfaceInfo
     * @return
     */
    @Override
    public MenuTree<JccInterfaceInfo> findJccInterfaceInfosTree(JccInterfaceInfo jccInterfaceInfo) {
        QueryWrapper<JccInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(jccInterfaceInfo.getInterfaceName())) {
            queryWrapper.lambda().like(JccInterfaceInfo::getInterfaceName, jccInterfaceInfo.getInterfaceName());
        }
        queryWrapper.lambda().orderByAsc(JccInterfaceInfo::getOrderNum);
        List<JccInterfaceInfo> jccInterfaceInfos = this.baseMapper.selectList(queryWrapper);
        List<MenuTree<JccInterfaceInfo>> trees = this.convertCategorys(jccInterfaceInfos);

        return TreeUtil.buildMenuTree(trees);
    }

    /**
     * 获取部门树（下拉选使用）
     * @return
     */
    @Override
    public List<MenuTree<JccInterfaceInfo>> findJccInterfaceInfosTree() {
        List<JccInterfaceInfo> jccInterfaceInfo = this.baseMapper.selectList(new QueryWrapper<>());
        List<MenuTree<JccInterfaceInfo>> trees = this.convertMenus(jccInterfaceInfo);
        return TreeUtil.buildList(trees, "0");
    }
    @Override
    public IPage<JccInterfaceInfo> findJccInterfaceInfos(QueryRequest request, JccInterfaceInfo jccInterfaceInfo) {
        LambdaQueryWrapper<JccInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<JccInterfaceInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<JccInterfaceInfo> findJccInterfaceInfos(JccInterfaceInfo jccInterfaceInfo) {
	    LambdaQueryWrapper<JccInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo) {
        this.save(jccInterfaceInfo);
    }

    @Override
    @Transactional
    public void updateJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo) {
        this.saveOrUpdate(jccInterfaceInfo);
    }

    @Override
    @Transactional
    public void deleteJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo) {
        LambdaQueryWrapper<JccInterfaceInfo> wapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wapper);
	}

    @Override
    @Transactional
    public void deleteJccInterfaceInfos(String interfaceIds) {
        String[] interfaceIdsArray = interfaceIds.split(StringPool.COMMA);
        this.delete(Arrays.asList(interfaceIdsArray));

        shiroRealm.clearCache();
    }

    private List<MenuTree<JccInterfaceInfo>> convertCategorys(List<JccInterfaceInfo> jccInterfaceInfos) {
        List<MenuTree<JccInterfaceInfo>> trees = new ArrayList<>();
        jccInterfaceInfos.forEach(jccInterfaceInfo -> {
            MenuTree<JccInterfaceInfo> tree = new MenuTree<>();
            tree.setId(String.valueOf(jccInterfaceInfo.getInterfaceId()));
            tree.setParentId(String.valueOf(jccInterfaceInfo.getParentId()));
            tree.setName(jccInterfaceInfo.getInterfaceName());
            tree.setIcon(jccInterfaceInfo.getIcon());
            tree.setData(jccInterfaceInfo);
            trees.add(tree);
        });
        return trees;
    }

    private List<MenuTree<JccInterfaceInfo>> convertMenus(List<JccInterfaceInfo> jccInterfaceInfos){
        List<MenuTree<JccInterfaceInfo>> trees = new ArrayList<>();
        jccInterfaceInfos.forEach(jccInterfaceInfo -> {
            MenuTree<JccInterfaceInfo> tree = new MenuTree<>();
            tree.setId(String.valueOf(jccInterfaceInfo.getInterfaceId()));
            tree.setParentId(String.valueOf(jccInterfaceInfo.getParentId()));
            tree.setName(jccInterfaceInfo.getInterfaceName());
            tree.setIcon(jccInterfaceInfo.getIcon());
            tree.setData(jccInterfaceInfo);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> interfaceIds) {
        removeByIds(interfaceIds);

        LambdaQueryWrapper<JccInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(JccInterfaceInfo::getParentId, interfaceIds);
        List<JccInterfaceInfo> jccInterfaceInfos = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(jccInterfaceInfos)) {
            List<String> categoryIdList = new ArrayList<>();
            jccInterfaceInfos.forEach(m -> categoryIdList.add(String.valueOf(m.getInterfaceId())));
            this.delete(categoryIdList);
        }
    }
}
