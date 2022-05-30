package org.hj.chain.platform.fileresource.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.Constants;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.fileresource.entity.FileResource;
import org.hj.chain.platform.fileresource.mapper.FileResourceMapper;
import org.hj.chain.platform.fileresource.service.IFileResourceService;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.tdo.fileresource.FileResourceAddTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.fileresource.FileResourceDisplayVo;
import org.hj.chain.platform.vo.fileresource.FileResourceSearchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileResourceServiceImpl extends ServiceImpl<FileResourceMapper, FileResource>
        implements IFileResourceService {
    @Autowired
    private ISysUserService sysUserService;
    @Override
    public Result<IPage<FileResourceDisplayVo>> findByCondition(PageVo pageVo, FileResourceSearchVo sv) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<FileResourceDisplayVo> vos = new ArrayList<>();
        Page<FileResource> page = PageUtil.initMpPage(pageVo);
        LambdaQueryWrapper queryWrapper = Wrappers.<FileResource>lambdaQuery()
                .eq(FileResource::getOrganId, loginOutputVo.getOrganId())
                .like(sv.getFileName() != null, FileResource::getFileName, sv.getFileName())
                .like(sv.getFileType() != null, FileResource::getFileType, sv.getFileType());

        queryWrapper.last("ORDER BY CONVERT( file_type USING gbk ) COLLATE gbk_chinese_ci ASC, CONVERT( file_name USING gbk ) COLLATE gbk_chinese_ci ASC");
        this.page(page, queryWrapper);
        if(page.getRecords() != null && !page.getRecords().isEmpty()) {
            vos = page.getRecords().stream().map(item -> {
                FileResourceDisplayVo vo = new FileResourceDisplayVo();
                BeanUtils.copyProperties(item, vo);
                UserVo cUser = sysUserService.findUserByUserId(item.getCreateUserId());
                if(cUser != null) {
                    vo.setCreateUser(cUser.getEmpName());
                }
                if(item.getUpdateUserId() != null) {
                    UserVo uUser = sysUserService.findUserByUserId(item.getCreateUserId());
                    if(uUser != null) {
                        vo.setUpdateUser(uUser.getEmpName());
                    }
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return ResultUtil.data(PageUtil.convertPageVo(page, vos));
    }

    @Override
    public String getFileNoByFileCodeAndOrganId(String fileCode, String organId) {
        if(StrUtil.isBlank(fileCode)) {
            return null;
        }
        if(StrUtil.isBlank(organId)) {
            organId = StpUtil.getSession().getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class).getOrganId();
        }
        FileResource fileResource = this.getOne(Wrappers.<FileResource>lambdaQuery()
                .eq(FileResource::getOrganId, organId).eq(FileResource::getFileCode, fileCode));
        if(fileResource != null) {
            return fileResource.getFileNo();
        }
        return null;
    }


}
