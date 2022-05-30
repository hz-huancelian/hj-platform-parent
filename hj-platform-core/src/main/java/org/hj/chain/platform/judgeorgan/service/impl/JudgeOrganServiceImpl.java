package org.hj.chain.platform.judgeorgan.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.judgeorgan.mapper.JudgeOrganMapper;
import org.hj.chain.platform.judgeorgan.model.JudgeOrgan;
import org.hj.chain.platform.judgeorgan.service.IJudgeOrganService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.judgeorgan.JudgeOrganModifyTdo;
import org.hj.chain.platform.tdo.judgeorgan.JudgeOrganTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganListVo;
import org.hj.chain.platform.vo.judgeorgan.JudgeOrganVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/14  9:23 上午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/14    create
 */
@Service
public class JudgeOrganServiceImpl implements IJudgeOrganService {
    @Autowired
    private JudgeOrganMapper judgeOrganMapper;
    @Autowired
    private ISysUserService sysUserService;

    @Transactional
    @Override
    public Result<Object> addJudgeOrgan(JudgeOrganTdo addTdo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        String judgeOrganName = addTdo.getJudgeOrganName();
        JudgeOrgan dbOrgan = judgeOrganMapper.selectOne(Wrappers.<JudgeOrgan>lambdaQuery()
                .select(JudgeOrgan::getId)
                .eq(JudgeOrgan::getOrganId, organId)
                .eq(JudgeOrgan::getJudgeOrganName, judgeOrganName));
        if (dbOrgan != null) {
            return ResultUtil.busiError("该分包机构已存在！");
        }

        String userId = loginOutputVo.getUserId();
        LocalDateTime now = LocalDateTime.now();
        Integer count = judgeOrganMapper.selectCount(Wrappers.<JudgeOrgan>lambdaQuery().eq(JudgeOrgan::getOrganId, organId));
        String judgeOrganId = StringUtils.getFixLengStr(organId, 9, count + 1);
        JudgeOrgan po = new JudgeOrgan();
        po.setEmail(addTdo.getEmail())
                .setBankName(addTdo.getBankName())
                .setBankNumber(addTdo.getBankNumber())
                .setJudgeOrganLinker(addTdo.getJudgeOrganLinker())
                .setJudgeOrganLinkerPhone(addTdo.getJudgeOrganLinkerPhone())
                .setJudgeOrganName(addTdo.getJudgeOrganName())
                .setLegalPerson(addTdo.getLegalPerson())
                .setIsDelete("0")
                .setRemark(addTdo.getRemark())
                .setOrganId(organId)
                .setCreateUserId(userId)
                .setCreateTime(now)
                .setId(judgeOrganId);
        judgeOrganMapper.insert(po);
        return ResultUtil.success("新增成功！");
    }

    @Transactional
    @Override
    public Result<Object> modifyJudgeOrganById(JudgeOrganModifyTdo modifyTdo) {
        String id = modifyTdo.getId();
        JudgeOrgan dbOrgan = judgeOrganMapper.selectOne(Wrappers.<JudgeOrgan>lambdaQuery()
                .select(JudgeOrgan::getJudgeOrganName,
                        JudgeOrgan::getOrganId)
                .eq(JudgeOrgan::getId, id));
        if (dbOrgan == null) {
            return ResultUtil.busiError("该ID关联的分包机构不存在！");
        }

        String judgeOrganName = modifyTdo.getJudgeOrganName();
        if (!dbOrgan.getJudgeOrganName().equals(judgeOrganName)) {
            JudgeOrgan dbOrganN = judgeOrganMapper.selectOne(Wrappers.<JudgeOrgan>lambdaQuery()
                    .select(JudgeOrgan::getId)
                    .eq(JudgeOrgan::getOrganId, dbOrgan.getOrganId())
                    .eq(JudgeOrgan::getJudgeOrganName, judgeOrganName));
            if (dbOrganN != null) {
                return ResultUtil.busiError("该分包机构已存在！");
            }
        }

        LocalDateTime now = LocalDateTime.now();
        JudgeOrgan po = new JudgeOrgan();
        po.setEmail(modifyTdo.getEmail())
                .setBankName(modifyTdo.getBankName())
                .setBankNumber(modifyTdo.getBankNumber())
                .setJudgeOrganLinker(modifyTdo.getJudgeOrganLinker())
                .setJudgeOrganLinkerPhone(modifyTdo.getJudgeOrganLinkerPhone())
                .setJudgeOrganName(modifyTdo.getJudgeOrganName())
                .setLegalPerson(modifyTdo.getLegalPerson())
                .setRemark(modifyTdo.getRemark())
                .setUpdateTime(now)
                .setId(id);
        judgeOrganMapper.updateById(po);
        return ResultUtil.success("更新成功！");
    }

    @Override
    public Result<Object> delById(String id) {
        JudgeOrgan dbOrgan = judgeOrganMapper.selectOne(Wrappers.<JudgeOrgan>lambdaQuery()
                .select(JudgeOrgan::getIsDelete)
                .eq(JudgeOrgan::getId, id));
        if (dbOrgan == null) {
            return ResultUtil.busiError("该ID关联的分包机构不存在！");
        }

        LocalDateTime now = LocalDateTime.now();
        judgeOrganMapper.update(null, Wrappers.<JudgeOrgan>lambdaUpdate()
                .set(JudgeOrgan::getUpdateTime, now)
                .set(JudgeOrgan::getIsDelete, "1")
                .eq(JudgeOrgan::getId, id));
        return ResultUtil.success("删除成功！");
    }

    @Override
    public IPage<JudgeOrganVo> findPageByCondition(PageVo pageVo, String judgeOrganName) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Page<JudgeOrganVo> page = PageUtil.initMpPage(pageVo);
        judgeOrganMapper.findByCondition(page, organId, StrUtil.trimToNull(judgeOrganName));
        page.getRecords().forEach(item -> {
            UserVo userVo = sysUserService.findUserByUserId(item.getCreateUserId());
            if (userVo != null) {
                item.setCreateUserId(userVo.getEmpName());
            }
        });
        return page;
    }

    @Override
    public List<JudgeOrganListVo> findValidList() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<JudgeOrgan> dbList = judgeOrganMapper.selectList(Wrappers.<JudgeOrgan>lambdaQuery()
                .select(JudgeOrgan::getId,
                        JudgeOrgan::getJudgeOrganName,
                        JudgeOrgan::getRemark)
                .eq(JudgeOrgan::getOrganId, organId)
                .eq(JudgeOrgan::getIsDelete, "0"));
        if (dbList != null && !dbList.isEmpty()) {
            List<JudgeOrganListVo> listVos = dbList.stream().map(item -> {
                JudgeOrganListVo judgeOrganVo = new JudgeOrganListVo();
                judgeOrganVo.setId(item.getId())
                        .setJudgeOrganName(item.getJudgeOrganName())
                        .setRemark(item.getRemark());

                return judgeOrganVo;
            }).collect(Collectors.toList());
            return listVos;
        }
        return null;
    }
}