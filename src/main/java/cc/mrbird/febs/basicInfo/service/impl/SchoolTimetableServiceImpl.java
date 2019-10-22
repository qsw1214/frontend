package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.SchoolTimetable;
import cc.mrbird.febs.basicInfo.mapper.SchoolTimetableMapper;
import cc.mrbird.febs.basicInfo.service.ISchoolTimetableService;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.LiveRadioReqUtil;
import cc.mrbird.febs.common.utils.RadioStatus;
import cc.mrbird.febs.common.utils.SortUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
        IPage<SchoolTimetable> pageList = this.baseMapper.findSchoolTimetables(page, schoolTimetable);
        List<SchoolTimetable> list = pageList.getRecords();
        for (int i = 0 ; i < list.size(); i++){
            SchoolTimetable timetable = list.get(i);
            String url = timetable.getUrl();
            List<RadioStatus> radioList = LiveRadioReqUtil.getRadioStatus(url);
            for (int j = 0 ; j < radioList.size();j++){
//                System.out.println("指定地址：" + radioList.get(j).getUrl() + " " + radioList.get(j).getStatus() + " " + radioList.get(j).getStartDate());
                if((DateUtil.getNowDateTime().after(timetable.getBeginDate()) && radioList.get(j).getStatus().equals("0"))){
                    timetable.setState("-1");
                }else{
                    timetable.setState(radioList.get(j).getStatus());
                }
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
    public void insertSchoolTimetable(List<SchoolTimetable> schoolTimetable) {
        this.saveBatch(schoolTimetable);
       /* for(int i=0; i<schoolTimetable.size(); i++){
            schoolTimeTableService.save(schoolTimetable.get(i).get());
        }*/
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

	@Override
	public IPage<SchoolTimetable> findSchoolTimetableByDept(QueryRequest request, SchoolTimetable schoolTimetable,
			Long deptId) {
		Page<SchoolTimetable> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.findSchoolTimetableByDept(page, schoolTimetable, deptId);
	}
}
