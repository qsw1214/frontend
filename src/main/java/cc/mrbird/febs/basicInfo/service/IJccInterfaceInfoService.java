package cc.mrbird.febs.basicInfo.service;


import cc.mrbird.febs.basicInfo.entity.JccInterfaceInfo;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Category;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author lb
 * @date 2019-10-11 09:27:33
 */
public interface IJccInterfaceInfoService extends IService<JccInterfaceInfo> {
    /**
     * 查找所有的类别 （树形结构）
     *
     * @return MenuTree<JccInterfaceInfo>
     */
    MenuTree<JccInterfaceInfo> findJccInterfaceInfosTree(JccInterfaceInfo jccInterfaceInfo);

    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<MenuTree<JccInterfaceInfo>> findJccInterfaceInfosTree();
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param jccInterfaceInfo jccInterfaceInfo
     * @return IPage<JccInterfaceInfo>
     */
    IPage<JccInterfaceInfo> findJccInterfaceInfos(QueryRequest request, JccInterfaceInfo jccInterfaceInfo);

    /**
     * 查询（所有）
     *
     * @param jccInterfaceInfo jccInterfaceInfo
     * @return List<JccInterfaceInfo>
     */
    List<JccInterfaceInfo> findJccInterfaceInfos(JccInterfaceInfo jccInterfaceInfo);

    /**
     * 新增
     *
     * @param jccInterfaceInfo jccInterfaceInfo
     */
    void createJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo);

    /**
     * 修改
     *
     * @param jccInterfaceInfo jccInterfaceInfo
     */
    void updateJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo);

    /**
     * 删除
     *
     * @param jccInterfaceInfo jccInterfaceInfo
     */
    void deleteJccInterfaceInfo(JccInterfaceInfo jccInterfaceInfo);

    /**
     * 删除接口类别
     *
     * @param interfaceIds 接口类别id
     */
    void deleteJccInterfaceInfos(String interfaceIds);
}
