package cc.mrbird.febs.basicInfo.service.impl;

import cc.mrbird.febs.basicInfo.entity.DeviceInfo;
import cc.mrbird.febs.basicInfo.entity.Timetable;
import cc.mrbird.febs.basicInfo.mapper.TimetableMapper;
import cc.mrbird.febs.basicInfo.service.ITimetableService;
import cc.mrbird.febs.common.entity.QueryRequest;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *  Service实现
 *
 * @author wsq
 * @date 2019-10-23 15:34:24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TimetableServiceImpl extends ServiceImpl<TimetableMapper, Timetable> implements ITimetableService {

    @Autowired
    private TimetableMapper timetableMapper;

    @Override
    public IPage<Timetable> findTimetables(QueryRequest request, Timetable timetable) {
        LambdaQueryWrapper<Timetable> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        if (timetable.getTimetableDate() != null) {
            queryWrapper.eq(Timetable::getTimetableDate, timetable.getTimetableDate());
        }
        if (timetable.getTimetableWeek() != null) {
            queryWrapper.eq(Timetable::getTimetableWeek, timetable.getTimetableWeek());
        }
        Page<Timetable> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Timetable> findTimetables(Timetable timetable) {
	    LambdaQueryWrapper<Timetable> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
        if (timetable.getTimetableDate() != null) {
            queryWrapper.eq(Timetable::getTimetableDate, timetable.getTimetableDate());
        }
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createTimetable(Timetable timetable) {
        timetable.setTimetableWeek(dateToWeek(timetable.getTimetableDate()));
        this.save(timetable);
    }

    @Override
    @Transactional
    public void updateTimetable(Timetable timetable) {
        timetable.setTimetableWeek(dateToWeek(timetable.getTimetableDate()));
        this.saveOrUpdate(timetable);
    }

    @Override
    @Transactional
    public void deleteTimetable(String tids) {
        List<String> list = Arrays.asList(tids.split(StringPool.COMMA));
        this.baseMapper.delete(
                new QueryWrapper<Timetable>().lambda().in(Timetable::getTid, list));
        //this.baseMapper.deleteById(tids);
	}

    public static String dateToWeek(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = sdf.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }
}
