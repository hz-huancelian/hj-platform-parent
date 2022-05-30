package org.hj.chain.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hj.chain.platform.model.SysPost;
import org.hj.chain.platform.vo.SysPostVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysPostMapper extends BaseMapper<SysPost> {


    /**
     * TODO 根据用户ID查看用户的职位列表
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:11 下午
     */
    List<SysPostVo> selectPostsByUserId(String userId);


    /**
     * TODO 根据用户ID查看关联的职位IDs
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/2 2:02 下午
     */
    List<Long> selectPostIdsByUserId(String userId);

    /**
     * TODO 根据用户ID查询职位集合
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/1 12:17 下午
     */
    List<String> selectUserPostGroup(String userId);
}
