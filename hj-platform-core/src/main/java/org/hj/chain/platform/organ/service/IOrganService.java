package org.hj.chain.platform.organ.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.tdo.organ.RegOrganTdo;
import org.hj.chain.platform.vo.OrganSearchVo;
import org.hj.chain.platform.vo.OrganVo;

public interface IOrganService {


    /**
     * TODO 分页查询
     *
     * @param page
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/8 3:10 下午
     */
    IPage<OrganVo> findOrgansByCondition(PageVo page, OrganSearchVo sv);


    /**
     * @Description: TODO 密码修改
     * @Param: [userId, oldPassword, newPassword]
     * @return: org.hj.chain.platform.Result<java.lang.Object>
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/27 9:58 下午
     */
    Result<Object> modifyPassword(String userId, String oldPassword, String newPassword);


    /**
     * TODO 注册机构
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/4/30 9:20 上午
     */
    Result<Object> registOrigan(RegOrganTdo tdo);


    /**
     * TODO 初始化检测标准
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/7 12:45 下午
     */
    void initCheckStandard(String organId);

    /**
     * TODO 初始化角色权限
     *
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/7 9:00 下午
     */
    void initDeptRole(String organId,String organName,String leader);

    /**
     * TODO 初始化文件资源
     *
     * @param organId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/6/7 9:27 下午
     */
    void initFileResource(String organId);
}
