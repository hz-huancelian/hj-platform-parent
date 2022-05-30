package org.hj.chain.platform.dataimport.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.StringUtils;
import org.hj.chain.platform.common.DownloadExcelUtil;
import org.hj.chain.platform.common.ExcelUtils;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.entity.CusContractBaseInfo;
import org.hj.chain.platform.contract.service.ICusContractBaseInfoService;
import org.hj.chain.platform.dataimport.entity.*;
import org.hj.chain.platform.dataimport.listener.CustomerExcelDataListener;
import org.hj.chain.platform.dataimport.listener.EquipmentExcelDataListener;
import org.hj.chain.platform.dataimport.listener.UserExcelDataListener;
import org.hj.chain.platform.dataimport.service.IDataImportService;
import org.hj.chain.platform.equipment.entity.EquipmentInfo;
import org.hj.chain.platform.equipment.service.IEquipmentInfoService;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.service.ISysDictService;
import org.hj.chain.platform.service.ISysUserService;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.UserVo;
import org.hj.chain.platform.vo.equipment.EquipmentSearchVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataImportServiceImpl implements IDataImportService {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ICusContractBaseInfoService cusContractBaseInfoService;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @SuppressWarnings("resource")
    @Override
    public void downloadModel(String modelName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ClassPathResource resource = new ClassPathResource("templates/excel/" + modelName);
        InputStream is = resource.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        //下载
        DownloadExcelUtil dUtil = new DownloadExcelUtil();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        xssfWorkbook.write(os);
        dUtil.download(os, response, modelName);
        os.flush();
        os.close();
        is.close();
    }

    @Override
    public Result<Object> uploadEquipent(MultipartFile file) {

        EquipmentExcelDataListener listener = new EquipmentExcelDataListener();
        try {
            List<EquipmentImportEntity> dataList = listener.parseImportFile(file.getInputStream());
            if (dataList != null && !dataList.isEmpty()) {
                SaSession session = StpUtil.getSession();
                LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
                LocalDateTime now = LocalDateTime.now();
                List<EquipmentInfo> equipmentInfos = dataList.stream().map(d -> {
                    EquipmentInfo equipmentInfo = new EquipmentInfo();
                    BeanUtils.copyProperties(d, equipmentInfo);
                    equipmentInfo.setEquipmentStatus("0")
                            .setOrganId(loginOutputVo.getOrganId())
                            .setIsDelete("0")
                            .setCreateUserId(loginOutputVo.getUserId())
                            .setCreateTime(now)
                            .setEquipmentFirstType(Long.valueOf(d.getEquipmentFirstType().split("_")[1]))
                            .setEquipmentSecondType(Long.valueOf(d.getEquipmentSecondType().split("_")[1]));
                    return equipmentInfo;
                }).collect(Collectors.toList());
                if (equipmentInfoService.saveBatch(equipmentInfos)) {
                    return ResultUtil.success("设备信息导入成功！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.busiError("设备信息文件解析异常！");
        }
        return ResultUtil.busiError("设备信息导入失败！");
    }

    @Override
    public void exportEquipment(HttpServletResponse response, EquipmentSearchVo sv) throws IOException {
        List<EquipmentExportEntity> dataList = Collections.emptyList();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<EquipmentInfo> infos = equipmentInfoService.list(Wrappers.<EquipmentInfo>lambdaQuery()
                .select(EquipmentInfo::getEquipmentNumber, EquipmentInfo::getEquipmentName, EquipmentInfo::getEquipmentModel,
                        EquipmentInfo::getEquipmentBrand, EquipmentInfo::getEquipmentFirstType, EquipmentInfo::getEquipmentSecondType,
                        EquipmentInfo::getCheckCircle, EquipmentInfo::getRemark)
                .eq(EquipmentInfo::getOrganId, loginOutputVo.getOrganId())
                .eq(StrUtil.isNotBlank(sv.getEquipmentNumber()), EquipmentInfo::getEquipmentNumber, sv.getEquipmentNumber())
                .eq(sv.getEquipmentFirstType() != null, EquipmentInfo::getEquipmentFirstType, sv.getEquipmentFirstType())
                .eq(sv.getEquipmentSecondType() != null, EquipmentInfo::getEquipmentSecondType, sv.getEquipmentSecondType())
                .eq(EquipmentInfo::getIsDelete, "0")
                .like(StrUtil.isNotBlank(sv.getEquipmentName()), EquipmentInfo::getEquipmentName, sv.getEquipmentName())
                .orderByDesc(EquipmentInfo::getCreateTime));
        if (infos != null && !infos.isEmpty()) {
            Map<String, DictParam> dictParamMap = sysDictService.findDictMap();
            dataList = infos.stream().map(item -> {
                EquipmentExportEntity entity = new EquipmentExportEntity();
                entity.setEquipmentNumber(item.getEquipmentNumber())
                        .setEquipmentName(item.getEquipmentName())
                        .setEquipmentModel(item.getEquipmentModel())
                        .setEquipmentBrand(item.getEquipmentBrand())
                        .setCheckCircle(item.getCheckCircle())
                        .setRemark(item.getRemark())
                        .setEquipmentFirstType(dictParamMap.get(String.valueOf(item.getEquipmentFirstType())).getDictVal());
                if (item.getEquipmentSecondType() != null) {
                    entity.setEquipmentSecondType(dictParamMap.get(String.valueOf(item.getEquipmentSecondType())).getDictVal());
                }
                return entity;
            }).collect(Collectors.toList());
        }
        String fileName = "设备列表-" + LocalDateTime.now().toString();
        ExcelUtils.write(response, EquipmentExportEntity.class, dataList, fileName, "设备列表");
    }

    @Override
    public Result<Object> uploadEmployee(MultipartFile file) {
        UserExcelDataListener listener = new UserExcelDataListener();
        try {
            List<UserImportEntity> dataList = listener.parseImportFile(file.getInputStream());
            if (dataList != null && !dataList.isEmpty()) {
                SaSession session = StpUtil.getSession();
                LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
                LocalDateTime now = LocalDateTime.now();
                String organId = loginOutputVo.getOrganId();
                int serNo = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getOrganId, organId));
                List<SysUser> users = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    UserImportEntity item = dataList.get(i);
                    SysUser user = new SysUser();
                    user.setCreateTime(now)
                            .setOrganId(loginOutputVo.getOrganId())
                            .setEmpCode(item.getEmpCode())
                            .setEmpName(item.getEmpName())
                            .setEmail(item.getEmail())
                            .setSex(item.getSex().equals("男") ? "0" : "1")
                            .setPhonenumber(item.getPhonenumber())
                            .setRemark(item.getRemark())
                            .setDeptId(0L)
                            .setStatus("0")
                            .setDelFlag("0")
                            .setIsAppLogin("1")
                            .setUserType("1")
                            .setUsername(StringUtils.getFixLengStr(organId, 7, serNo))
                            .setUserId(StringUtils.getFixLengStr(organId, 16, serNo))
                            .setPassword(BusiConstants.ENCRYPT_PREFIX + passwordEncoder.encode("123456"));
                    serNo++;
                    users.add(user);
                }
                if (sysUserService.saveBatch(users)) {
                    return ResultUtil.success("员工信息导入成功！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.busiError("员工信息文件解析异常！");
        }
        return ResultUtil.busiError("员工信息导入失败！");
    }

    @Override
    public void exportEmployee(HttpServletResponse response) throws IOException {
        List<UserExportEntity> dataList = Collections.emptyList();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<UserVo> vos = sysUserMapper.selectUserListByConditionNoPage(loginOutputVo.getOrganId());
        if (vos != null && !vos.isEmpty()) {
            dataList = vos.stream().map(item -> {
                UserExportEntity entity = new UserExportEntity();
                BeanUtils.copyProperties(item, entity);
                entity.setCreateDate(item.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                return entity;
            }).collect(Collectors.toList());
        }
        String fileName = "员工列表-" + LocalDateTime.now().toString();
        ExcelUtils.write(response, UserExportEntity.class, dataList, fileName, "员工列表");
    }

    @Override
    public Result<Object> uploadCustomer(MultipartFile file) {
        CustomerExcelDataListener listener = new CustomerExcelDataListener();
        try {
            List<CustomerImportEntity> dataList = listener.parseImportFile(file.getInputStream());
            if (dataList != null && !dataList.isEmpty()) {
                SaSession session = StpUtil.getSession();
                LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
                LocalDateTime now = LocalDateTime.now();
                List<CusContractBaseInfo> infos = dataList.stream().map(item -> {
                    CusContractBaseInfo info = new CusContractBaseInfo();
                    BeanUtils.copyProperties(item, info);
                    info.setCreateTime(now)
                            .setCreateUserId(loginOutputVo.getUserId())
                            .setDeptId(loginOutputVo.getDeptId())
                            .setOrganId(loginOutputVo.getOrganId());
                    return info;
                }).collect(Collectors.toList());
                if (cusContractBaseInfoService.saveBatch(infos)) {
                    return ResultUtil.success("客户信息导入成功！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.busiError("客户信息文件解析异常！");
        }
        return ResultUtil.busiError("客户信息导入失败！");
    }

    @Override
    public void exportCustomer(HttpServletResponse response) throws IOException {
        List<CustomerExportEntity> dataList = Collections.emptyList();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<CusContractBaseInfo> infos = cusContractBaseInfoService.list(Wrappers.<CusContractBaseInfo>lambdaQuery()
                .eq(CusContractBaseInfo::getOrganId, loginOutputVo.getOrganId())
                .orderByDesc(CusContractBaseInfo::getCreateTime));
        if (infos != null && !infos.isEmpty()) {
            dataList = infos.stream().map(item -> {
                CustomerExportEntity entity = new CustomerExportEntity();
                BeanUtils.copyProperties(item, entity);
                return entity;
            }).collect(Collectors.toList());
        }
        String fileName = "客户列表-" + LocalDateTime.now().toString();
        ExcelUtils.write(response, CustomerExportEntity.class, dataList, fileName, "客户列表");
    }
}
