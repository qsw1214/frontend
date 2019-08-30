package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @author MrBird
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    Map findIdByName(Map params);


    Map selectRoleByUser();
}
