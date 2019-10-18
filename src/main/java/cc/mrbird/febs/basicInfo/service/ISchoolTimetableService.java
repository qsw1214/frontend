package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.resource.entity.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author psy
 * @date 2019-08-21 10:38:49
 */
public interface ISchoolTimetableService extends IService<SchoolTimetable> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param schoolTimetable schoolTimetable
     * @return IPage<SchoolTimetable>
     */
    IPage<SchoolTimetable> findSchoolTimetables(QueryRequest request, SchoolTimetable schoolTimetable);

    /**
     * 查询（所有）
     *
     * @param schoolTimetable schoolTimetable
     * @return List<SchoolTimetable>
     */
    List<SchoolTimetable> findSchoolTimetables(SchoolTimetable schoolTimetable);

    /**
     * 新增
     *
     * @param schoolTimetable schoolTimetable
     */
    void createSchoolTimetable(SchoolTimetable schoolTimetable);

    /**
     * 批量新增数据(Excel)
     */
    void insertSchoolTimetable(List<SchoolTimetable> schoolTimetable);

    /**
     * 修改
     *
     * @param schoolTimetable schoolTimetable
     */
    void updateSchoolTimetable(SchoolTimetable schoolTimetable);

    /**
     * 删除
     */
    void deleteSchoolTimetable(String courseIds);

    public SchoolTimetable findCourseById(Integer courseId);

}
