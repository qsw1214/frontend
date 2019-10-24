package cc.mrbird.febs.basicInfo.service;

import cc.mrbird.febs.basicInfo.entity.Timetable;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author wsq
 * @date 2019-10-23 15:34:24
 */
public interface ITimetableService extends IService<Timetable> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param timetable timetable
     * @return IPage<Timetable>
     */
    IPage<Timetable> findTimetables(QueryRequest request, Timetable timetable);

    /**
     * 查询（所有）
     *
     * @param timetable timetable
     * @return List<Timetable>
     */
    List<Timetable> findTimetables(Timetable timetable);

    /**
     * 新增
     *
     * @param timetable timetable
     */
    void createTimetable(Timetable timetable);

    /**
     * 修改
     *
     * @param timetable timetable
     */
    void updateTimetable(Timetable timetable);

    /**
     * 删除
     *
     * @param
     */
    void deleteTimetable(String tids);
}
