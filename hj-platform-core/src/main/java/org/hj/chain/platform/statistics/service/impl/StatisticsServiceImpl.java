package org.hj.chain.platform.statistics.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.check.entity.CheckFactorInfo;
import org.hj.chain.platform.check.mapper.CheckFactorInfoMapper;
import org.hj.chain.platform.check.mapper.CheckTaskMapper;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.contract.entity.ContractInfo;
import org.hj.chain.platform.contract.mapper.ContractInfoMapper;
import org.hj.chain.platform.contract.mapper.CusContractBaseInfoMapper;
import org.hj.chain.platform.equipment.mapper.EquipmentInfoMapper;
import org.hj.chain.platform.equipment.service.IEquipmentInfoService;
import org.hj.chain.platform.judge.mapper.JudgeRecordMapper;
import org.hj.chain.platform.judge.mapper.JudgeTaskMapper;
import org.hj.chain.platform.mapper.SysUserMapper;
import org.hj.chain.platform.model.SysUser;
import org.hj.chain.platform.offer.entity.OfferInfo;
import org.hj.chain.platform.offer.entity.OfferJudgeInfo;
import org.hj.chain.platform.offer.mapper.OfferInfoMapper;
import org.hj.chain.platform.offer.mapper.OfferJudgeInfoMapper;
import org.hj.chain.platform.report.mapper.ReportInfoMapper;
import org.hj.chain.platform.report.model.ReportInfo;
import org.hj.chain.platform.sample.mapper.*;
import org.hj.chain.platform.statistics.service.IStatisticsService;
import org.hj.chain.platform.vo.DictParam;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.SysUserDeptVo;
import org.hj.chain.platform.vo.statistics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    private ContractInfoMapper contractInfoMapper;
    @Autowired
    private CusContractBaseInfoMapper cusContractBaseInfoMapper;
//    @Autowired
//    private SampleTaskInfoMapper sampleTaskInfoMapper;
    @Autowired
    private CheckTaskMapper checkTaskInfoMapper;
    @Autowired
    private ReportInfoMapper reportInfoMapper;
//    @Autowired
//    private SampleItemInfoMapper sampleItemInfoMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private OfferJudgeInfoMapper offerJudgeInfoMapper;
//    @Autowired
//    private OfferDispatchTaskMapper offerDispatchTaskMapper;
//    @Autowired
//    private OfferDispatchInfoMapper offerDispatchInfoMapper;
    @Autowired
    private OfferInfoMapper offerInfoMapper;
//    @Autowired
//    private SampleItemDetailMapper sampleItemDetailMapper;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private SampleDrawApplyMapper sampleDrawApplyMapper;
    @Autowired
    private SampleStoreApplyMapper sampleStoreApplyMapper;
    @Autowired
    private CheckFactorInfoMapper checkFactorInfoMapper;
    @Autowired
    private JudgeRecordMapper judgeRecordMapper;
    @Autowired
    private JudgeTaskMapper judgeTaskMapper;

    @Override
    public Result<ValidContractVo> validContracts(String type) {
        ValidContractVo vo = new ValidContractVo();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        int cusSum = 0;
        if(type.equals("0")) {
            vo = contractInfoMapper.validContractsForCurrMonth(organId);
            cusSum = cusContractBaseInfoMapper.newCusCountForCurrMonth(organId);
        }else if(type.equals("1")) {
            vo = contractInfoMapper.validContractsForCurrYear(organId);
            cusSum = cusContractBaseInfoMapper.newCusCountForCurrYear(organId);
        }
        if(vo != null) {
            if(vo.getContSum() == null) {
                vo.setCusSum(0);
            }
            if(vo.getContAmount() == null) {
                vo.setContAmount(new BigDecimal("0"));
            }
        }else {
            vo = new ValidContractVo();
            vo.setContAmount(new BigDecimal(0)).setContSum(0);
        }
        vo.setContSum(cusSum);
        return ResultUtil.data(vo);
    }

//    @Override
//    public Result<List<CompleteTaskVo>> completeTask(String type) {
//        List<CompleteTaskVo> result = new ArrayList<>();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        CompleteTaskVo sampleTask = new CompleteTaskVo();
//        CompleteTaskVo checkTask = new CompleteTaskVo();
//        CompleteTaskVo reportTask = new CompleteTaskVo();
//        if(type.equals("0")) {
//            sampleTask = sampleTaskInfoMapper.completeTaskForCurrMonth(organId);
//            checkTask = checkTaskInfoMapper.completeTaskForCurrMonth(organId);
//            reportTask = reportInfoMapper.completeTaskForCurrMonth(organId);
//        }else if(type.equals("1")) {
//            sampleTask = sampleTaskInfoMapper.completeTaskForCurrYear(organId);
//            checkTask = checkTaskInfoMapper.completeTaskForCurrYear(organId);
//            reportTask = reportInfoMapper.completeTaskForCurrYear(organId);
//        }
//        sampleTask.setName("采样任务");
//        if(sampleTask.getCompleteCnt() == null) {
//            sampleTask.setCompleteCnt(0);
//        }
//        if(sampleTask.getTotalCnt() == null) {
//            sampleTask.setTotalCnt(0);
//        }
//        result.add(sampleTask);
//        checkTask.setName("检测任务");
//        if(checkTask.getTotalCnt() == null) {
//            checkTask.setTotalCnt(0);
//        }
//        if(checkTask.getCompleteCnt() == null) {
//            checkTask.setCompleteCnt(0);
//        }
//        result.add(checkTask);
//        reportTask.setName("报告编制任务");
//        if(reportTask.getTotalCnt() == null) {
//            reportTask.setTotalCnt(0);
//        }
//        if(reportTask.getCompleteCnt() == null) {
//            reportTask.setCompleteCnt(0);
//        }
//        result.add(reportTask);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> issueReport(String type) {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        xNameData.add("报告总数");
        xNameData.add("报告签发数");
        JSONArray xValData = new JSONArray();
        ReportIssueVo vo = new ReportIssueVo();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        if(type.equals("0")) {
            vo = reportInfoMapper.issueReportForCurrMonth(organId);
        }else if(type.equals("1")) {
            vo = reportInfoMapper.issueReportForCurrYear(organId);
        }
        if(vo == null) {
            xValData.add(0);
            xValData.add(0);
        }else{
            if(vo.getTotalIssuedCnt() == null) {
                xValData.add(0);
            }else{
                xValData.add(vo.getTotalIssuedCnt());
            }
            if(vo.getIssuedCnt() == null) {
                xValData.add(0);
            }else{
                xValData.add(vo.getIssuedCnt());
            }
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> sampleClassificationCnt(String type, Integer limit) {
//        JSONObject result = new JSONObject();
//        JSONArray xAxis = new JSONArray();
//        JSONArray yAxis = new JSONArray();
//        List<String> sampleStatus = Arrays.asList("4","6","7","8","9");
//        Map<String, String> map = Arrays.stream(new Object[][]{
//                {"001", "水"},
//                {"002", "气"},
//                {"003", "土壤、底泥"},
//                {"004", "噪声"},
//                {"005", "固废"},
//                {"006", "污泥和生活垃圾"}
//        }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        List<SampleClassificationVo> vos = new ArrayList<>();
//        if(type.equals("0")) {
//            vos = sampleItemInfoMapper.sampleClassificationCntForCurrMonth(organId, sampleStatus);
//        }else if(type.equals("1")) {
//            vos = sampleItemInfoMapper.sampleClassificationCntForCurrYear(organId, sampleStatus);
//        }
//        if( limit > 0 && vos.size() >= limit) {
//            vos = vos.subList(0,limit);
//        }
//        vos.forEach(vo -> {
//            xAxis.add(map.get(vo.getClassId()));
//            yAxis.add(vo.getCnt());
//        });
//        result.put("xNameData", xAxis);
//        result.put("xValData", yAxis);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<List<SysUserDeptVo>> deptUserCnt(Integer limit) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<SysUserDeptVo> vos = sysUserMapper.deptUserCnt(organId);
        if( limit > 0 && vos.size() >= limit) {
            vos = vos.subList(0, limit);
        }
        return ResultUtil.data(vos);
    }

//    @Override
//    public Result<Object> offerDispatchTaskCnt(String type) {
//        JSONObject result = new JSONObject();
//        JSONArray xNameData = new JSONArray();
//        xNameData.add("已调度任务数");
//        xNameData.add("待调度任务数");
//        JSONArray xValData = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        OfferDispatchStatisVo vo = new OfferDispatchStatisVo();
//        if(type.equals("0")) {
//            vo = offerDispatchTaskMapper.offerDispatchTaskCntForCurrMonth(organId);
//        }else if(type.equals("1")) {
//            vo = offerDispatchTaskMapper.offerDispatchTaskCntForCurrYear(organId);
//        }
//        if(vo != null) {
//            if(vo.getDipatchedCnt() == null) {
//                xValData.add(0);
//            }else{
//                xValData.add(vo.getDipatchedCnt());
//            }
//            if(vo.getUnDispatchCnt() == null) {
//                xValData.add(0);
//            }else{
//                xValData.add(vo.getUnDispatchCnt());
//            }
//        }else{
//            xValData.add(0);
//            xValData.add(0);
//        }
//        result.put("xNameData", xNameData);
//        result.put("xValData", xValData);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> validContractsForPass11Month() {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray contactNum = new JSONArray();
        JSONArray contactMoney = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<ValidContractForPass11MonthVo> vos = contractInfoMapper.validContractsForPass11Month(organId);
        Map<String, ValidContractForPass11MonthVo> map = new HashMap<>();
        if(vos != null && !vos.isEmpty()) {
            vos.forEach(item -> map.put(item.getXNameData(), item));
        }
        LocalDate startMonth = LocalDate.now().minusMonths(11);
        String currMonth = startMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        xNameData.add(currMonth);
        if(map.containsKey(currMonth)) {
            contactNum.add(map.get(currMonth).getContactNum());
            contactMoney.add(map.get(currMonth).getContactMoney());
        }else{
            contactNum.add(0);
            contactMoney.add(0);
        }
        for(int i = 1; i < 12; i++) {
            currMonth = startMonth.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            xNameData.add(currMonth);
            if(map.containsKey(currMonth)) {
                contactNum.add(map.get(currMonth).getContactNum());
                contactMoney.add(map.get(currMonth).getContactMoney());
            }else{
                contactNum.add(0);
                contactMoney.add(0);
            }
        }
        result.put("xNameData", xNameData);
        result.put("contactNum", contactNum);
        result.put("contactMoney", contactMoney);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<List<TaskCityVo>> taskCity(String type) {
//        List<TaskCityVo> vos;
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        if("0".equals(type)) {
//            vos = offerDispatchInfoMapper.taskCityForCurrMonth(organId);
//        }else {
//            vos = offerDispatchInfoMapper.taskCityForCurrYear(organId);
//        }
//        return ResultUtil.data(vos);
//    }

    @Override
    public Result<Object> staffTurnover(String type) {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        xNameData.add("入职员工数");
        xNameData.add("离职员工数");
        JSONArray xValData = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery().eq(SysUser::getOrganId, organId);
        if("0".equals(type)) {
            queryWrapper.apply("DATE_FORMAT( create_time, '%Y%m' ) = DATE_FORMAT( CURDATE(), '%Y%m' )");
        }else{
            queryWrapper.apply("YEAR( create_time ) = YEAR ( NOW() )");
        }
        List<SysUser> users = sysUserMapper.selectList(queryWrapper);
        if(users != null && !users.isEmpty()) {
            xValData.add(users.stream().filter(user -> "0".equals(user.getStatus())).count());
            xValData.add(users.stream().filter(user -> "1".equals(user.getStatus())).count());
        }else{
            xValData.add(0);
            xValData.add(0);
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> managerToDoList() {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        int offCount = offerInfoMapper.selectCount(Wrappers.<OfferInfo>lambdaQuery()
                .eq(OfferInfo::getOrganId, organId).eq(OfferInfo::getStatus, "1"));
        buildResult(result, "待审批报价单数", offCount);
        int contCount = contractInfoMapper.selectCount(Wrappers.<ContractInfo>lambdaQuery()
                .eq(ContractInfo::getOrganId, organId).eq(ContractInfo::getContStatus, "1"));
        buildResult(result, "待审批合同数", contCount);
        int contCount_1 = contractInfoMapper.selectCountForJudge(organId);
        buildResult(result, "待评审合同数", contCount_1);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> sortContAmount(String type, Integer limit) {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray xValData = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<Map<String, Object>> list;
        if("0".equals(type)) {
            list = contractInfoMapper.sortContAmountForCurrMonth(organId);
        }else{
            list = contractInfoMapper.sortContAmountForCurrYear(organId);
        }
        if(list != null && !list.isEmpty()) {
            if( limit > 0 && list.size() >= limit) {
                list = list.subList(0, limit);
            }
            list.forEach(map -> {
                xNameData.add(map.get("empName"));
                xValData.add(map.get("amount"));
            });
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> sortContNum(String type, Integer limit) {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray xValData = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<Map<String, Object>> list;
        if("0".equals(type)) {
            list = contractInfoMapper.sortContNumForCurrMonth(organId);
        }else{
            list = contractInfoMapper.sortContNumForCurrYear(organId);
        }
        if(list != null && !list.isEmpty()) {
            if( limit > 0 && list.size() >= limit) {
                list = list.subList(0, limit);
            }
            list.forEach(map -> {
                xNameData.add(map.get("empName"));
                xValData.add(map.get("cnt"));
            });
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> ownerValidContractsForPass11Month() {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray contactNum = new JSONArray();
        JSONArray contactMoney = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<ValidContractForPass11MonthVo> vos = contractInfoMapper.ownerValidContractsForPass11Month(organId, loginOutputVo.getUserId());
        Map<String, ValidContractForPass11MonthVo> map = new HashMap<>();
        if(vos != null && !vos.isEmpty()) {
            vos.forEach(item -> map.put(item.getXNameData(), item));
        }
        LocalDate startMonth = LocalDate.now().minusMonths(11);
        String currMonth = startMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        xNameData.add(currMonth);
        if(map.containsKey(currMonth)) {
            contactNum.add(map.get(currMonth).getContactNum());
            contactMoney.add(map.get(currMonth).getContactMoney());
        }else{
            contactNum.add(0);
            contactMoney.add(0);
        }
        for(int i = 1; i < 12; i++) {
            currMonth = startMonth.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            xNameData.add(currMonth);
            if(map.containsKey(currMonth)) {
                contactNum.add(map.get(currMonth).getContactNum());
                contactMoney.add(map.get(currMonth).getContactMoney());
            }else{
                contactNum.add(0);
                contactMoney.add(0);
            }
        }
        result.put("xNameData", xNameData);
        result.put("contactNum", contactNum);
        result.put("contactMoney", contactMoney);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> dispatchToDoList() {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        int dispatchCount = offerDispatchTaskMapper.selectCountForToBeDispatched(organId);
//        int judgeCount = offerJudgeInfoMapper.pendingOfferJudgeCnt(organId);
//        buildResult(result, "待调度任务数", dispatchCount);
//        buildResult(result, "待分包判定任务数", judgeCount);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> offerJudgeInfoCnt(String type) {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<OfferJudgeInfo> list = offerJudgeInfoMapper.selectListByOrganId(organId);
        long cnt_1 = 0l, cnt_2 = 0l;
        if(list != null && !list.isEmpty()) {
            cnt_1 = list.stream().filter(item -> "0".equals(item.getStatus())).count();
            cnt_2 = list.stream().filter(item -> "1".equals(item.getStatus())).count();
        }
        buildResult(result, "待分包判定任务数", cnt_1);
        buildResult(result, "已分包判断任务数", cnt_2);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> offerDispatchInfoCnt(String type) {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        int dispatchInfoCnt = 0, finishDispatchInfoCnt = 0;
//        if("0".equals(type)) {
//            dispatchInfoCnt = offerDispatchInfoMapper.selectCountForCurrMonth(organId);
//            finishDispatchInfoCnt = reportInfoMapper.selectCountFinishedForCurrMonth(organId);
//        }else{
//            dispatchInfoCnt = offerDispatchInfoMapper.selectCountForCurrYear(organId);
//            finishDispatchInfoCnt = reportInfoMapper.selectCountFinishedForCurrYear(organId);
//        }
//        buildResult(result, "总任务", dispatchInfoCnt);
//        buildResult(result, "正在进行", dispatchInfoCnt - finishDispatchInfoCnt);
//        buildResult(result, "已完成", finishDispatchInfoCnt);
//        return ResultUtil.data(result);
//    }

//    @Override
//    public Result<Object> cyTaskCnt(String type) {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        List<SampleTaskInfo> list;
//        if("0".equals(type)) {
//            list = sampleTaskInfoMapper.selectListByOrganIdForCurrMonth(organId);
//        }else{
//            list = sampleTaskInfoMapper.selectListByOrganIdForCurrYear(organId);
//        }
//        buildResult(result, "未分配", list.stream().filter(task -> "0".equals(task.getTaskStatus())).count());
//        buildResult(result, "正在进行", list.stream().filter(task -> "1".equals(task.getTaskStatus())).count());
//        buildResult(result, "已完成", list.stream().filter(task -> "2".equals(task.getTaskStatus())).count());
//        return ResultUtil.data(result);
//    }

//    @Override
//    public Result<Object> sortSampledNum(String type, Integer limit) {
//        JSONObject result = new JSONObject();
//        JSONArray xNameData = new JSONArray();
//        JSONArray xValData = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        List<Map<String, Object>> list;
//        List<String> sampleStatus = Arrays.asList("4","6","7","8","9");
//        if("0".equals(type)) {
//            list = sampleItemDetailMapper.sortSampledNumForMonth(organId, sampleStatus);
//        }else{
//            list = sampleItemDetailMapper.sortSampledNumForYear(organId, sampleStatus);
//        }
//        if( limit > 0 && list.size() >= limit) {
//            list = list.subList(0,limit);
//        }
//        list.forEach(item -> {
//            xNameData.add(item.get("empName"));
//            xValData.add(item.get("cnt"));
//        });
//        result.put("xNameData", xNameData);
//        result.put("xValData", xValData);
//        return ResultUtil.data(result);
//    }

//    @Override
//    public Result<Object> allSampledItemsForPass11Month() {
//        JSONObject result = new JSONObject();
//        JSONArray xNameData = new JSONArray();
//        JSONArray xValData = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        List<String> sampleStatus = Arrays.asList("4","6","7","8","9");
//        List<Map<String, Object>> list = sampleItemDetailMapper.allSampledItemsForPass11Month(organId, sampleStatus);
//        LocalDate startMonth = LocalDate.now().minusMonths(11);
//        String currMonth = startMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
//        xNameData.add(currMonth);
//        Map<String, Object> map = new HashMap<>();
//        if(list != null && !list.isEmpty()) {
//            list.forEach(item -> map.put(item.get("xNameData").toString(), item.get("xValData")));
//        }
//        if(map.containsKey(currMonth)) {
//            xValData.add(map.get(currMonth));
//        }else{
//            xValData.add(0);
//        }
//
//        for(int i = 1; i < 12; i++) {
//            currMonth = startMonth.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
//            xNameData.add(currMonth);
//            if(map.containsKey(currMonth)) {
//                xValData.add(map.get(currMonth));
//            }else{
//                xValData.add(0);
//            }
//        }
//        result.put("xNameData", xNameData);
//        result.put("xValData", xValData);
//        return ResultUtil.data(result);
//    }

//    @Override
//    public Result<Object> ownerSampledItemsForPass11Month() {
//        JSONObject result = new JSONObject();
//        JSONArray xNameData = new JSONArray();
//        JSONArray xValData = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        List<String> sampleStatus = Arrays.asList("4","6","7","8","9");
//        List<Map<String, Object>> list = sampleItemDetailMapper.ownerSampledItemsForPass11Month(organId, loginOutputVo.getUserId(), sampleStatus);
//        LocalDate startMonth = LocalDate.now().minusMonths(11);
//        String currMonth = startMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
//        xNameData.add(currMonth);
//        Map<String, Object> map = new HashMap<>();
//        if(list != null && !list.isEmpty()) {
//            list.forEach(item -> map.put(item.get("xNameData").toString(), item.get("xValData")));
//        }
//        if(map.containsKey(currMonth)) {
//            xValData.add(map.get(currMonth));
//        }else{
//            xValData.add(0);
//        }
//
//        for(int i = 1; i < 12; i++) {
//            currMonth = startMonth.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
//            xNameData.add(currMonth);
//            if(map.containsKey(currMonth)) {
//                xValData.add(map.get(currMonth));
//            }else{
//                xValData.add(0);
//            }
//        }
//        result.put("xNameData", xNameData);
//        result.put("xValData", xValData);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> equipmentCnt() {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<Map<String, Object>> list = equipmentInfoMapper.equipmentCnt(organId);
        if(list != null && !list.isEmpty()) {
            List<DictParam> dictParams = equipmentInfoService.findEquipmentFirstType();
            Map<Long, String> map = new HashMap<>();
            dictParams.forEach(d -> map.put(d.getDictKey(), d.getDictVal()));
            list.forEach(item -> {
                buildResult(result, map.get(item.get("firstType")), item.get("cnt"));
            });
        }
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> equipmentStatsuCnt() {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray idle = new JSONArray();
        JSONArray use = new JSONArray();
        JSONArray maintain = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<EquipmentStatusStatictisVo> vos = equipmentInfoMapper.equipmentStatsuCnt(organId);
        if(vos != null && !vos.isEmpty()) {
            List<DictParam> dictParams = equipmentInfoService.findEquipmentFirstType();
            Map<Long, String> dMap = new HashMap<>();
            dictParams.forEach(d -> dMap.put(d.getDictKey(), d.getDictVal()));

            Map<Long, List<EquipmentStatusStatictisVo>> map = vos.stream().collect(Collectors.groupingBy(EquipmentStatusStatictisVo::getFirstType));
            map.forEach((k,v) -> {
                xNameData.add(dMap.get(k));
                idle.add(v.stream().filter(item -> "0".equals(item.getStatus())).mapToInt(EquipmentStatusStatictisVo::getCnt).sum());
                use.add(v.stream().filter(item -> "1".equals(item.getStatus())).mapToInt(EquipmentStatusStatictisVo::getCnt).sum());
                maintain.add(v.stream().filter(item -> "2".equals(item.getStatus())).mapToInt(EquipmentStatusStatictisVo::getCnt).sum());
            });
        }
        result.put("xNameData", xNameData);
        result.put("idle", idle);
        result.put("use", use);
        result.put("maintain", maintain);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> sampleManagementCnt(String type) {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        Integer totalCnt = sampleItemInfoMapper.selectCountForStoreListByOrganId(organId);
//        buildResult(result, "库存样品数", totalCnt);
//        Integer storeCnt = 0, outCnt = 0;
//        if("0".equals(type)) {
//            storeCnt = sampleStoreApplyMapper.selectCountByOrganIdForCurrMonth(organId);
//            outCnt = sampleDrawApplyMapper.selectCountByOrganIdForCurrMonth(organId);
//        }else{
//            storeCnt = sampleStoreApplyMapper.selectCountByOrganIdForCurrYear(organId);
//            outCnt = sampleDrawApplyMapper.selectCountByOrganIdForCurrYear(organId);
//        }
//        buildResult(result, "入库样品数", storeCnt);
//        buildResult(result, "出库样品数", outCnt);
//        return ResultUtil.data(result);
//    }

//    @Override
//    public Result<Object> storedSampleClassificationCnt(Integer limit) {
//        JSONObject result = new JSONObject();
//        JSONArray xAxis = new JSONArray();
//        JSONArray yAxis = new JSONArray();
//        List<String> sampleStatus = Arrays.asList("6","7");
//        Map<String, String> map = Arrays.stream(new Object[][]{
//                {"001", "水"},
//                {"002", "气"},
//                {"003", "土壤、底泥"},
//                {"004", "噪声"},
//                {"005", "固废"},
//                {"006", "污泥和生活垃圾"}
//        }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        List<SampleClassificationVo> vos = sampleItemInfoMapper.storedSampleClassificationCnt(organId, sampleStatus);
//        if( limit > 0 && vos.size() >= limit) {
//            vos = vos.subList(0,limit);
//        }
//        vos.forEach(vo -> {
//            xAxis.add(map.get(vo.getClassId()));
//            yAxis.add(vo.getCnt());
//        });
//        result.put("xNameData", xAxis);
//        result.put("xValData", yAxis);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> issueReportForPass11Month() {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray xValData = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<Map<String, Object>> list = reportInfoMapper.issueReportForPass11Month(organId);
        LocalDate startMonth = LocalDate.now().minusMonths(11);
        String currMonth = startMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        xNameData.add(currMonth);
        Map<String, Object> map = new HashMap<>();
        if(list != null && !list.isEmpty()) {
            list.forEach(item -> map.put(item.get("xNameData").toString(), item.get("xValData")));
        }
        if(map.containsKey(currMonth)) {
            xValData.add(map.get(currMonth));
        }else{
            xValData.add(0);
        }

        for(int i = 1; i < 12; i++) {
            currMonth = startMonth.plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            xNameData.add(currMonth);
            if(map.containsKey(currMonth)) {
                xValData.add(map.get(currMonth));
            }else{
                xValData.add(0);
            }
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> reportStatusCnt() {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray xValData = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<String> reportStatus = Arrays.asList("1","2","3","4");
        List<ReportInfo> list = reportInfoMapper.selectList(Wrappers.<ReportInfo>lambdaQuery()
                .select(ReportInfo::getReportCode, ReportInfo::getOrganId, ReportInfo::getReportStatus)
                .eq(ReportInfo::getOrganId, organId).in(ReportInfo::getReportStatus, reportStatus));
        xNameData.add("待制作");
        xNameData.add("制作中");
        xNameData.add("待审核");
        xNameData.add("待签发");
        if(list != null && !list.isEmpty()) {
            xValData.add(list.stream().filter(item -> "1".equals(item.getReportStatus())).count());
            xValData.add(list.stream().filter(item -> "2".equals(item.getReportStatus())).count());
            xValData.add(list.stream().filter(item -> "3".equals(item.getReportStatus())).count());
            xValData.add(list.stream().filter(item -> "4".equals(item.getReportStatus())).count());
        }else{
            xValData.add(0);
            xValData.add(0);
            xValData.add(0);
            xValData.add(0);
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> checkTaskCnt(String type) {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<Map<String, Object>> list;
        if("0".equals(type)) {
            list = checkTaskInfoMapper.checkTaskCntForCurrMonth(organId);
        }else{
            list = checkTaskInfoMapper.checkTaskCntForCurrYear(organId);
        }
        if(list != null && !list.isEmpty()) {
            list.forEach(map -> {
                if(map.get("taskStatus").equals("0")) {
                    buildResult(result, "待分配", map.get("cnt"));
                }else if(map.get("taskStatus").equals("1")) {
                    buildResult(result, "正在进行", map.get("cnt"));
                }else if(map.get("taskStatus").equals("2")) {
                    buildResult(result, "已完成", map.get("cnt"));
                }
            });
        }
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> sortCheckedSample(String type, Integer limit) {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray xValData = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<Map<String, Object>> list;
        if("0".equals(type)) {
            list = checkFactorInfoMapper.sortCheckedSampleForCurrMonth(organId);
        }else{
            list = checkFactorInfoMapper.sortCheckedSampleForCurrYear(organId);
        }
        if(list != null && !list.isEmpty()) {
            if( limit > 0 && list.size() >= limit) {
                list = list.subList(0, limit);
            }
            list.forEach(map -> {
                xNameData.add(map.get("xNameData"));
                xValData.add(map.get("xValData"));
            });
        }
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> checkedSampleClassificationCnt(String type, Integer limit) {
        JSONObject result = new JSONObject();
        JSONArray xNameData = new JSONArray();
        JSONArray xValData = new JSONArray();
        Map<String, String> map = Arrays.stream(new Object[][]{
                {"001", "水"},
                {"002", "气"},
                {"003", "土壤、底泥"},
                {"004", "噪声"},
                {"005", "固废"},
                {"006", "污泥和生活垃圾"}
        }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<SampleClassificationVo> vos ;
        if("0".equals(type)) {
            vos = checkFactorInfoMapper.checkedSampleClassificationCntForCurrMonth(organId);
        }else{
            vos = checkFactorInfoMapper.checkedSampleClassificationCntForCurrYear(organId);
        }
        if( limit > 0 && vos.size() >= limit) {
            vos = vos.subList(0,limit);
        }
        vos.forEach(vo -> {
            xNameData.add(map.get(vo.getClassId()));
            xValData.add(vo.getCnt());
        });
        result.put("xNameData", xNameData);
        result.put("xValData", xValData);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> ownerCheckFactorCnt(String type) {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        List<CheckFactorInfo> list;
        if("0".equals(type)) {
            list = checkFactorInfoMapper.ownerCheckFactorCntForCurrMonth(organId, loginOutputVo.getUserId());
        }else{
            list = checkFactorInfoMapper.ownerCheckFactorCntForCurrYear(organId, loginOutputVo.getUserId());
        }
        if(list != null && !list.isEmpty()) {
            long finish = list.stream().filter(item -> "5".equals(item.getCheckStatus())).count();
            buildResult(result, "已完成", finish);
            buildResult(result, "正在进行", list.size() - finish);
        }
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> cyManagerToDoList() {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        int assignSampleTaskCnt = sampleTaskInfoMapper.selectCountForAssign(organId);
//        buildResult(result, "待分配的采样任务数", assignSampleTaskCnt);
//        int pendSampleInfoCnt = sampleItemInfoMapper.selectCountForPending(organId);
//        buildResult(result, "待审批样品数", pendSampleInfoCnt);
//        int reviewTaskCnt = judgeRecordMapper.selectCountForReview(organId, loginOutputVo.getUserId());
//        buildResult(result, "待评审任务数", reviewTaskCnt);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> technicalDirectorToDoList() {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        int unJudgementCnt = judgeTaskMapper.selectCountForUnJudgement(organId);
        buildResult(result, "待评审判定数", unJudgementCnt);
        int unContractReviewCnt = judgeTaskMapper.selectCountForUnContractReview(organId, loginOutputVo.getUserId());
        buildResult(result, "待合同评审数", unContractReviewCnt);
        return ResultUtil.data(result);
    }

    @Override
    public Result<Object> checkManagerToDoList() {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        int unAssignCheckTaskCnt = checkTaskInfoMapper.selectCountForUnAssignCheckTask(organId);
        buildResult(result, "待分配检测任务数", unAssignCheckTaskCnt);
        int pendingReviewCheckFactorCnt = checkFactorInfoMapper.selectCountPendingReviewCheckFactor(organId);
        buildResult(result, "待审核因子数", pendingReviewCheckFactorCnt);
        int unContractReviewCnt = judgeTaskMapper.selectCountForUnContractReview(organId, loginOutputVo.getUserId());
        buildResult(result, "待评审任务数", unContractReviewCnt);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> sampleManagerToDoList() {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        String organId = loginOutputVo.getOrganId();
//        int toStoreSampleCnt = sampleStoreApplyMapper.selectCountToStoreSample(organId);
//        buildResult(result, "待入库样品数", toStoreSampleCnt);
//        int toDrawSampleCnt = sampleDrawApplyMapper.selectCountToDrawSample(organId);
//        buildResult(result, "待出库样品数", toDrawSampleCnt);
//        return ResultUtil.data(result);
//    }

    @Override
    public Result<Object> gmTopAggregateIndicators() {
        JSONArray result = new JSONArray();
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        String organId = loginOutputVo.getOrganId();
        ValidContractVo vo = contractInfoMapper.validContractsForCurrMonth(organId);
        if(vo != null) {
            buildResult(result, "本月新增合同数", vo.getContSum());
            buildResult(result, "本月新增合同额", vo.getContAmount());
        }else {
            buildResult(result, "本月新增合同数", 0);
            buildResult(result, "本月新增合同额", 0);
        }
        int totalEmp = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getOrganId, organId)
                .eq(SysUser::getUserType, "1")
                .eq(SysUser::getStatus, "0")
                .eq(SysUser::getDelFlag, "0"));
        buildResult(result, "员工总数", totalEmp);
        return ResultUtil.data(result);
    }

//    @Override
//    public Result<Object> sampleLeaderToDoList() {
//        JSONArray result = new JSONArray();
//        SaSession session = StpUtil.getSession();
//        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
//        int cnt = sampleItemInfoMapper.selectCount(Wrappers.<SampleItemInfo>lambdaQuery()
//                .eq(SampleItemInfo::getSampleUserId, loginOutputVo.getUserId())
//                .eq(SampleItemInfo::getSampleStatus, "2"));
//        buildResult(result, "待采样确认数", cnt);
//        return ResultUtil.data(result);
//    }

    public void buildResult(JSONArray result, String name, Object value) {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("value", value);
        result.add(obj);
    }

}
