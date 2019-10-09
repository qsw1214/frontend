package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.basicInfo.mapper.SchoolTimetableMapper;
import cc.mrbird.febs.basicInfo.service.ISchoolTimetableService;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 *  Service实现
 *
 * @author psy
 * @date 2019-08-21 10:38:49
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SchoolTimetableServiceImpl extends ServiceImpl<SchoolTimetableMapper, SchoolTimetable> implements ISchoolTimetableService {

    @Autowired
    private SchoolTimetableMapper schoolTimetableMapper;

    @Override
    public IPage<SchoolTimetable> findSchoolTimetables(QueryRequest request, SchoolTimetable schoolTimetable) {
        Page<SchoolTimetable> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "course_id", FebsConstant.ORDER_ASC, false);
        if(schoolTimetable.getSchoolId() != null){
            //根据传递过来的学校id判断是否有主校,如果有主校,就将schoolId赋值为主校的
            Integer mainSchoolId = this.baseMapper.selectMainSchoolId(schoolTimetable.getSchoolId());
            if(mainSchoolId != null){
                schoolTimetable.setSchoolId(mainSchoolId);
            }
        }
        return this.baseMapper.findSchoolTimetables(page, schoolTimetable);
    }

    @Override
    public List<SchoolTimetable> findSchoolTimetables(SchoolTimetable schoolTimetable) {
	    LambdaQueryWrapper<SchoolTimetable> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createSchoolTimetable(SchoolTimetable schoolTimetable) {
        this.save(schoolTimetable);
    }

    @Override
    @Transactional
    public void updateSchoolTimetable(SchoolTimetable schoolTimetable) {
        this.saveOrUpdate(schoolTimetable);
    }

    @Override
    @Transactional
    public void deleteSchoolTimetable(String courseIds) {
        List<String> list = Arrays.asList(courseIds.split(StringPool.COMMA));
        this.baseMapper.delete(
                new QueryWrapper<SchoolTimetable>().lambda().in(SchoolTimetable::getCourseId, list));
	}

    @Override
    public SchoolTimetable findCourseById(Integer courseId){
        return this.getById(courseId);
    }
}
