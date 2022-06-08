package org.hj.chain.platform.factor.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.CodeBuildUtil;
import org.hj.chain.platform.FileUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.factor.entity.FactorRain;
import org.hj.chain.platform.factor.entity.FactorRainField;
import org.hj.chain.platform.factor.entity.FactorRainFile;
import org.hj.chain.platform.factor.mapper.FactorRainFieldMapper;
import org.hj.chain.platform.factor.mapper.FactorRainFileMapper;
import org.hj.chain.platform.factor.mapper.FactorRainMapper;
import org.hj.chain.platform.factor.service.FactorRainService;
import org.hj.chain.platform.fileresource.entity.FileResource;
import org.hj.chain.platform.model.FactorClassInfo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.factor.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zzl
 * @description 雨水因子服务实现类
 * @Date : 2022.6.1
 */
@Slf4j
@Service
public class FactorRainServiceImpl extends ServiceImpl<FactorRainMapper, FactorRain> implements FactorRainService {

    @Autowired
    private FactorRainService factorRainService;
    @Autowired
    private FactorRainMapper factorRainMapper;
    @Autowired
    private FactorRainFieldMapper factorRainFieldMapper;
    @Autowired
    private FactorRainFileMapper factorRainFileMapper;
    @Value("${file.ht.upload}")
    private String uploadContPath;

    @Override
    public IPage<FactorRainVo> findFactorRainByCondition(PageVo pageVo, FactorRainSearchVo fr) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        // 保证雨水表数据同步性，需先删除在将查询出的数据插入
        List<FactorRain> factorRainIds = factorRainMapper.findFactorsRainByIdCondition();
        if (factorRainIds != null && !factorRainIds.isEmpty()){
            List<Long> ids = factorRainIds.stream().map(item -> item.getId()).collect(Collectors.toList());
            int count = factorRainMapper.delete(Wrappers.<FactorRain>lambdaQuery().in(FactorRain::getId, ids));
            if (count > 0) {
                log.info("雨水因子表数据删除成功!");
            }
        }
        // 批量插入
        List<FactorRainVo> factorRainVos = factorRainMapper.findFactorsRainByCondition(loginOutputVo.getOrganId(), fr);
        List<FactorRain> factorRains = factorRainVos.stream().map(d -> {
            FactorRain factorRain = new FactorRain();
            BeanUtils.copyProperties(d, factorRain);
            factorRain.setCreateTime(LocalDateTime.now());
            factorRain.setIsDeleted(0);
            return factorRain;
        }).collect(Collectors.toList());
        factorRainService.saveBatch(factorRains);
        Page<FactorRainVo> page = PageUtil.initMpPage(pageVo);
        this.baseMapper.findFactorsRainByCondition(page, loginOutputVo.getOrganId(), fr);
        return page;
    }

    @Override
    public Result<List<FactorRainFieldVo>> findFactorClassInfoCondition(FactorRainFieldSearchVo ff) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        // 通过控制编号id、文件类型、机构id查询出文件名称
        FileResource fileResourceName = factorRainFieldMapper.findClassInfoFileCondition(ff,loginOutputVo.getOrganId());
        if (fileResourceName != null){
            // 文件名称截取前两个文字用于模糊查询，拿到分类id
            String fileName = fileResourceName.getFileName().substring(0,2);
            FactorClassInfo secdClassId = factorRainFieldMapper.findFactorClassIdInfoCondition(fileName);
            // 通过分类id查询出二级类别的字段
            List<FactorRainField> factorRainFields = factorRainFieldMapper.findFactorsClassInfoCondition(secdClassId.getId());
            if (factorRainFields != null && !factorRainFields.isEmpty()) {
                List<FactorRainFieldVo> factorRainFieldVos = factorRainFields.stream().map(item -> {
                    FactorRainFieldVo factorRainFieldVo = new FactorRainFieldVo();
                    factorRainFieldVo.setId(item.getId())
                            .setSecdClassId(item.getSecdClassId())
                            .setSecdClassName(item.getSecdClassName())
                            .setFieldName(item.getFieldName())
                            .setCreateTime(item.getCreateTime())
                            .setIsDeleted(item.getIsDeleted());
                    return factorRainFieldVo;
                }).collect(Collectors.toList());
                return ResultUtil.data(factorRainFieldVos);
            }
        }
        return ResultUtil.data(null);
    }

    @Transactional
    @Override
    public Result<Object> uploadFactorRainFile(MultipartFile file, String fileType, String fileName, String fileNo) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        Integer count = factorRainFileMapper.selectCount(Wrappers.<FactorRainFile>lambdaQuery()
                                            .eq(FactorRainFile::getOrganId, organId));
        String factorCode = CodeBuildUtil.genContractCode(organId, LocalDate.now().toString(), count);
        LocalDateTime now = LocalDateTime.now();
        // 获取上传文件的id
        String factorFileId = FileUtil.fileUploadByFileName(file, factorCode, uploadContPath);
        if (factorFileId == null) {
            return ResultUtil.busiError("程序错误，请重新上传！");
        }
        FactorRainFile factorFile = new FactorRainFile();
        factorFile.setFileType(fileType)
                .setFileName(fileName)
                .setFileNo(fileNo)
                .setOrganId(organId)
                .setFactorFileId(factorFileId)
                .setCreateTime(now);
        factorRainFileMapper.insert(factorFile);
        return ResultUtil.success("上传成功！");
    }

}
