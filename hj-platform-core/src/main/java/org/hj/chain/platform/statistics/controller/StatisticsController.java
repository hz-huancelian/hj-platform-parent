package org.hj.chain.platform.statistics.controller;

import cn.hutool.core.util.StrUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.statistics.service.IStatisticsService;
import org.hj.chain.platform.vo.SysUserDeptVo;
import org.hj.chain.platform.vo.statistics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private IStatisticsService statisticsService;

    /* 工作台-总经理 */

    /**
     * todo 有效合同统计
     * @param type 0-当月；1-当年（默认查当月）
     * @return {"contSum" : 120, "contAmount" : 1287490.00, "cusSum" : 169}
     */
    @RequestMapping(value = "/validContracts", method = RequestMethod.GET)
    public Result<ValidContractVo> validContracts(@RequestParam String type) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        return statisticsService.validContracts(type);
    }

//    /**
//     * todo 任务完成统计
//     * @param type 0-当月；1-当年（默认查当月）
//     * @return
//     */
//    @RequestMapping(value = "/completeTask", method = RequestMethod.GET)
//    public Result<List<CompleteTaskVo>> completeTask(@RequestParam String type) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        return statisticsService.completeTask(type);
//    }

//    /**
//     * todo 任务区域统计
//     * @param type 0-当月；1-当年（默认查当月）
//     * @return
//     */
//    @RequestMapping(value = "/taskCity", method = RequestMethod.GET)
//    public Result<List<TaskCityVo>> taskCity(@RequestParam String type) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        return statisticsService.taskCity(type);
//    }

    /**
     * todo 全部合同统计（当前月份及过去11个月的数据，有效的合同）
     * @return {"xNameData":["","",""], "contactNum":[,,,], "contactMoney":[,,,]}
     */
    @RequestMapping(value = "/validContractsForPass11Month", method = RequestMethod.GET)
    public Result<Object> validContractsForPass11Month() {
        return statisticsService.validContractsForPass11Month();
    }


    /**
     * todo 报告发放统计
     * @param type 0-当月；1-当年（默认查当月）
     * @return {"xNameData":["报告总数","报告签发数"], "xValData":[100, 60]}
     */
    @RequestMapping(value = "/issueReport", method = RequestMethod.GET)
    public Result<Object> issueReport(@RequestParam String type) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        return statisticsService.issueReport(type);
    }


    /**
     * todo 部门员工数统计
     * @param limit 限制返回记录数 (默认6条,-1返回全部)
     * @return
     */
    @RequestMapping(value = "/deptUserCnt", method = RequestMethod.GET)
    public Result<List<SysUserDeptVo>> deptUserCnt(@RequestParam Integer limit) {
        if(limit == null) {
            limit = 6;
        }
        return statisticsService.deptUserCnt(limit);
    }

    /**
     * todo 人员流动情况
     * @param type 0-当月；1-当年（默认查当月）
     * @return {"xNameData":["入职员工数","离职员工数"],"xValData":[50,26]}
     */
    @RequestMapping(value = "/staffTurnover", method = RequestMethod.GET)
    public Result<Object> staffTurnover(@RequestParam String type) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        return statisticsService.staffTurnover(type);
    }

    /* 工作台-调度部门-负责人 */

//    /**
//     * todo 待办事项
//     * @return [{"name":"待调度任务数", "value" : 59},{"name":"待分包判定任务数", "value" : 9}]
//     */
//    @RequestMapping(value = "/ddb/dispatchToDoList", method = RequestMethod.GET)
//    public Result<Object> dispatchToDoList() {
//        return statisticsService.dispatchToDoList();
//    }
//
//    /**
//     * todo 调度任务数
//     * @param type 0-当月；1-当年（默认查当月）
//     * @return {"xNameData":["已调度任务数","待调度任务数"],"xValData":[139,59]}
//     */
//    @RequestMapping(value = "/ddb/offerDispatchTaskCnt", method = RequestMethod.GET)
//    public Result<Object> offerDispatchTaskCnt(@RequestParam String type) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        return statisticsService.offerDispatchTaskCnt(type);
//    }

    /**
     * todo 分包判断任务数
     * @param type 0-当月；1-当年（默认查当月）
     * @return [{"name":"待分包判定任务数","value":9},{"name":"已分包判断任务数","value":180}]
     */
    @RequestMapping(value = "/ddb/offerJudgeInfoCnt", method = RequestMethod.GET)
    public Result<Object> offerJudgeInfoCnt(@RequestParam String type) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        return statisticsService.offerJudgeInfoCnt(type);
    }

//    /**
//     * todo 任务数
//     * @param type 0-当月；1-当年（默认查当月）
//     * @return [{"name":"总任务","value":100},{"name":"正在进行","value":20},{"name":"已完成","value":180}]
//     */
//    @RequestMapping(value = "/ddb/offerDispatchInfoCnt")
//    public Result<Object> offerDispatchInfoCnt(@RequestParam String type) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        return statisticsService.offerDispatchInfoCnt(type);
//    }

    /* 工作台-市场部-负责人 */

    /**
     * todo 待办事项
     * @return [{"name":"待审批报价单数","value":10},{"name":"待审批合同数","value":8},{"name":"待评审合同数","value":8}]
     */
    @RequestMapping(value = "/scb/managerToDoList", method = RequestMethod.GET)
    public Result<Object> managerToDoList() {
        return statisticsService.managerToDoList();
    }

    /**
     * 全员签订合同额（元）:技术评审和合同审核全部通过的合同
     * @param type 0-当月；1-当年（默认查当月）
     * @param limit 限制返回记录数 (默认5条,-1返回全部)
     * @return {"xNameData":["","",""], "xValData":[,,]}
     */
    @RequestMapping(value = "/scb/sortContAmount", method = RequestMethod.GET)
    public Result<Object> sortContAmount(@RequestParam String type, @RequestParam Integer limit) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        if(limit == null) {
            limit = 5;
        }
        return statisticsService.sortContAmount(type, limit);
    }

    /**
     * 全员签订合同数（份）:技术评审和合同审核全部通过的合同
     * @param type 0-当月；1-当年（默认查当月）
     * @param limit 限制返回记录数 (默认5条,-1返回全部)
     * @return {"xNameData":["","",""], "xValData":[,,]}
     */
    @RequestMapping(value = "/scb/sortContNum", method = RequestMethod.GET)
    public Result<Object> sortContNum(@RequestParam String type, @RequestParam Integer limit) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        if(limit == null) {
            limit = 5;
        }
        return statisticsService.sortContNum(type, limit);
    }

    /* 工作台-市场部-业务员 */
    /**
     * todo 我的合同统计（当前月份及过去11个月的数据，有效的合同）
     * @return {"xNameData":["","",""], "contactNum":[,,,], "contactMoney":[,,,]}
     */
    @RequestMapping(value = "/scb/ownerValidContractsForPass11Month", method = RequestMethod.GET)
    public Result<Object> ownerValidContractsForPass11Month() {
        return statisticsService.ownerValidContractsForPass11Month();
    }

    /* 工作台-采样部 */

//    /**
//     * todo 待办事项
//     * @return [{"name":"待分配的采样任务数","value":10},{"name":"待审批样品数","value":8},{"name":"待评审任务数","value":8}]
//     */
//    @RequestMapping(value = "/cyb/managerToDoList", method = RequestMethod.GET)
//    public Result<Object> cyManagerToDoList() {
//        return statisticsService.cyManagerToDoList();
//    }
//
//    /**
//     * todo 全部采样任务数
//     * @param type 0-当月；1-当年（默认查当月）
//     * @return  [{"name":"未分配", "value":100},{"name":"正在进行", "value":100},{"name":"已完成", "value":100}]
//     */
//    @RequestMapping(value = "/cyb/cyTaskCnt", method = RequestMethod.GET)
//    public Result<Object> cyTaskCnt(@RequestParam String type) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        return statisticsService.cyTaskCnt(type);
//    }

//    /**
//     * todo 全部已采集各类样品总数
//     * @param type 0-当月；1-当年（默认查当月）
//     * @param limit 限制返回记录数 (默认6条, -1返回全部)
//     * @return {"xNameData":["水","气","噪声"...], "xValData": [112, 98, 50...]}
//     */
//    @RequestMapping(value = "/cyb/sampleClassificationCnt", method = RequestMethod.GET)
//    public Result<Object> sampleClassificationCnt(@RequestParam String type, @RequestParam Integer limit) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        if(limit == null) {
//            limit = 6;
//        }
//        return statisticsService.sampleClassificationCnt(type, limit);
//    }

//    /**
//     * todo 全部已采集样品数
//     * @param type 0-当月；1-当年（默认查当月）
//     * @param limit 限制返回记录数 (默认5条, -1返回全部)
//     * @return {"xNameData":["张一山","张二山","张三山"...], "xValData": [1234567, 1234512, 1234011...]}
//     */
//    @RequestMapping(value = "/cyb/sortSampledNum", method = RequestMethod.GET)
//    public Result<Object> sortSampledNum(@RequestParam String type, @RequestParam Integer limit) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        if(limit == null) {
//            limit = 5;
//        }
//        return statisticsService.sortSampledNum(type, limit);
//    }
//
//    /**
//     * todo 全部采样统计（当前月份及过去11个月的数据，审核通过的样品）
//     * @return {"xNameData":["","",""], "xValData":[,,,]}
//     */
//    @RequestMapping(value = "/cyb/allSampledItemsForPass11Month", method = RequestMethod.GET)
//    public Result<Object> allSampledItemsForPass11Month() {
//        return statisticsService.allSampledItemsForPass11Month();
//    }

//    /**
//     * todo 我的采样统计（当前月份及过去11个月的数据，审核通过的样品）
//     * @return {"xNameData":["","",""], "xValData":[,,,]}
//     */
//    @RequestMapping(value = "/cyb/ownerSampledItemsForPass11Month", method = RequestMethod.GET)
//    public Result<Object> ownerSampledItemsForPass11Month() {
//        return statisticsService.ownerSampledItemsForPass11Month();
//    }

    /* 工作台-设备管理 */

    /**
     * todo 设备数
     * @return [{"name":"现场采样设备","value":"20"},{},{},{}]
     */
    @RequestMapping(value = "/sb/equipmentCnt", method = RequestMethod.GET)
    public Result<Object> equipmentCnt() {
        return statisticsService.equipmentCnt();
    }

    /**
     * todo 设备状态 idle-闲置中  use-使用中 maintain-维修中
     * @return {"xNameData":["现场采样设备","",], "idle":[12,6,],"use":[10,5,],"maintain":[11,4,]}
     */
    @RequestMapping(value = "/sb/equipmentStatsuCnt", method = RequestMethod.GET)
    public Result<Object> equipmentStatsuCnt() {
        return statisticsService.equipmentStatsuCnt();
    }

    /* 工作台-样品管理 */

//    /**
//     * todo 样品总数统计
//     * @param type 0-当月；1-当年（默认查当月）
//     * @return [{"name":"库存样品数","value":"99"},{"name":"入库样品数","value":"199"},{"name":"出库样品数","value":"100"}]
//     */
//    @RequestMapping(value = "/yp/sampleManagementCnt", method = RequestMethod.GET)
//    public Result<Object> sampleManagementCnt(@RequestParam String type) {
//        if(StrUtil.isBlank(type)) {
//            type = "0";
//        }
//        return statisticsService.sampleManagementCnt(type);
//    }

    /**
     * todo 在库各类样品数统计
     * @param limit 限制返回记录数 (默认6条, -1返回全部)
     * @return {"xNameData":["水","气","噪声"...], "xValData": [112, 98, 50...]}
     */
//    @RequestMapping(value = "/yp/storedSampleClassificationCnt", method = RequestMethod.GET)
//    public Result<Object> storedSampleClassificationCnt(@RequestParam Integer limit) {
//        if(limit == null) {
//            limit = 6;
//        }
//        return statisticsService.storedSampleClassificationCnt(limit);
//    }

    /* 工作台-报告 */

    /**
     * todo 报告统计
     * @return ["xNameData":[], "xValData":[]]
     */
    @RequestMapping(value = "/bg/issueReportForPass11Month", method = RequestMethod.GET)
    public Result<Object> issueReportForPass11Month() {
        return statisticsService.issueReportForPass11Month();
    }

    /**
     * todo 当前报告任务数
     * @return ["xNameData":["待制作","制作中","待审核","待签发"], "xValData":[16,10,12,6]]
     */
    @RequestMapping(value = "/bg/reportStatusCnt", method = RequestMethod.GET)
    public Result<Object> reportStatusCnt() {
        return statisticsService.reportStatusCnt();
    }

    /* 工作台-检测 */

    /**
     * todo 全部检测任务数
     * @param type 0-当月；1-当年（默认查当月）
     * @return [{"name":"待分配","value":100},{"name":"正在进行","value":20},{"name":"已完成","value":180}]
     */
    @RequestMapping(value = "/jc/checkTaskCnt")
    public Result<Object> checkTaskCnt(@RequestParam String type) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        return statisticsService.checkTaskCnt(type);
    }

    /**
     * todo 检测样品数排名
     * @param type 0-当月；1-当年（默认查当月）
     * @param limit 限制返回记录数 (默认5条, -1返回全部)
     * @return {"xNameData":["","","",""],"xValData":[,,,]}
     */
    @RequestMapping(value = "/jc/sortCheckedSample", method = RequestMethod.GET)
    public Result<Object> sortCheckedSample(@RequestParam String type, @RequestParam Integer limit) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        if(limit == null) {
            limit = 5;
        }
        return statisticsService.sortCheckedSample(type, limit);
    }

    /**
     * todo 各类已检测样品数
     * @param type 0-当月；1-当年（默认查当月）
     * @param limit 限制返回记录数 (默认6条, -1返回全部)
     * @return {"xNameData":["水","气","噪声"...], "xValData": [112, 98, 50...]}
     */
    @RequestMapping(value = "/jc/checkedSampleClassificationCnt", method = RequestMethod.GET)
    public Result<Object> checkedSampleClassificationCnt(@RequestParam String type, @RequestParam Integer limit) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        if(limit == null) {
            limit = 6;
        }
        return statisticsService.checkedSampleClassificationCnt(type, limit);
    }

    /**
     * todo 我的检测任务数
     * @param type 0-当月；1-当年（默认查当月）
     * @return [{"name":"正在进行","value":20},{"name":"已完成","value":180}]
     */
    @RequestMapping(value = "/jc/ownerCheckFactorCnt", method = RequestMethod.GET)
    public Result<Object> ownerCheckFactorCnt(@RequestParam String type) {
        if(StrUtil.isBlank(type)) {
            type = "0";
        }
        return statisticsService.ownerCheckFactorCnt(type);
    }

    /* 工作台-技术负责人-待办事项 */

    /**
     * todo 待办事项
     * @return [{"name":"待评审判定数","value":9},{"name":"待合同评审数","value":9}]
     */
    @RequestMapping(value = "/td/toDoList", method = RequestMethod.GET)
    public Result<Object> technicalDirectorToDoList() {
        return statisticsService.technicalDirectorToDoList();
    }

    /* 工作台-检测负责人-待办事项 */

    /**
     * todo 待办事项
     * @return [{"name":"待分配检测任务数","value":9},{"name":"待审核因子数","value":9},{"name":"待评审任务数","value":9}]
     */
    @RequestMapping(value = "/jc/checkManagerToDoList")
    public Result<Object> checkManagerToDoList() {
        return statisticsService.checkManagerToDoList();
    }

    /* 工作台-样品管理员-待办事项 */

//    /**
//     * todo 待办事项
//     * @return [{"name":"待入库样品数","value":9},{"name":"待出库样品数","value":9}]
//     */
//    @RequestMapping(value = "/yp/sampleManagerToDoList", method = RequestMethod.GET)
//    public Result<Object> sampleManagerToDoList() {
//        return statisticsService.sampleManagerToDoList();
//    }

    /* 工作台-总经理 */
    /**
     * todo 总经理顶部汇总指标
     * @return [{"name":"本月新增合同数","value":1689},{"name":"本月新增合同额","value":345660},{"name":"员工总数","value":530}]
     */
    @RequestMapping(value = "/gm/topAggregateIndicators")
    public Result<Object> gmTopAggregateIndicators() {
        return statisticsService.gmTopAggregateIndicators();
    }

    /* 工作台-采样部-采样组长-待办事项 */

//    /**
//     * todo 待办事项
//     * @return ["name":"待采样确认数","value":9]
//     */
//    @RequestMapping(value = "/cy/sampleLeaderToDoList", method = RequestMethod.GET)
//    public Result<Object> sampleLeaderToDoList() {
//        return statisticsService.sampleLeaderToDoList();
//    }
}
