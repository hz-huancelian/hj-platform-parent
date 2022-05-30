package org.hj.chain.platform.constants;

public interface PlatformConstants {


    //平台机构
    String PLATFORM_ORGAN_ID = "0100";


    //业务部门
    long BUSI_DEPT_ID = 100101L;
    //调度部门
    long DISPATCH_DEPT_ID = 100102L;
    //采样部门
    long SAMPING_DEPT_ID = 100103L;
    //样品部
    long SAMPLE_DEPT_ID = 100104L;
    //测试部
    long TEST_DEPT_ID = 100105L;
    //报告部
    long REPORT_DEPT_ID = 100106L;
    //设备部
    long EQUIP_DEPT_ID = 100107L;
    //总经理
    Long COM_MANAGER_ID = 100315L;


    //业务员
    Long BUSI_EMP_ID = 100301L;
    //合同编制员
    Long BUSI_CONT_EMP_ID = 100302L;
    //业务部经理
    Long BUSI_MANAGER_ID = 100303L;

    //调度员
    Long DISPATCH_EMP_ID = 100304L;

    //采样员
    Long SAMPLE_EMP_ID = 100305L;

    //采样组长
    Long SAMPLE_LEADER_ID = 100306L;

    //采样负责人
    Long SAMPLE_MANAGE_ID = 100307L;

    //样品管理员
    Long SAMP_MANAGER_ID = 100308L;

    //检测员
    Long CHECK_EMP_ID = 100309L;

    //检测负责人（实验室负责人）
    Long CHECK_MANAGE_ID = 100310L;
    //报告编制员
    Long REPORT_MAKE_ID = 100311L;
    //报告审核员
    Long REPORT_CHK_ID = 100312L;
    //报告签字授权人
    Long REPORT_SIGN_ID = 100313L;
    //设备管理员
    Long EQUIP_EMP_ID = 100314L;
    //技术负责人
    Long TEC_MANAGER_ID = 100316L;

    /*  文件资源  */
    //现场记录单-噪声
    Long FILE_SAMP_RECORD_1 = 100401l;
    //现场记录单-废水现场
    Long FILE_SAMP_RECORD_2 = 100402l;
    //现场记录单-地表水
    Long FILE_SAMP_RECORD_3 = 100403l;
    //现场记录单-地下水
    Long FILE_SAMP_RECORD_4 = 100404l;
    //现场记录单-空气和废气（含室内空气）
    Long FILE_SAMP_RECORD_5 = 100405l;
    //现场记录单-土壤
    Long FILE_SAMP_RECORD_6 = 100406l;
    //现场记录单-固废
    Long FILE_SAMP_RECORD_7 = 100407l;

    //合同-合同
    Long FILE_CONT = 100408l;

    //样品交接单-有组织
    Long FILE_SAMP_HANDOVER_1 = 100409l;
    //样品交接单-无组织
    Long FILE_SAMP_HANDOVER_2 = 100410l;
    //样品交接单-固废、土壤
    Long FILE_SAMP_HANDOVER_3 = 100411l;
    //样品交接单-水
    Long FILE_SAMP_HANDOVER_4 = 100412l;

    //实验室原始记录表-PH分析
    Long FILE_CHECK_ORIGINAL_RECORD_1 = 100413l;
    //实验室原始记录表-CODcr分析
    Long FILE_CHECK_ORIGINAL_RECORD_2 = 100414l;
    //实验室原始记录表-电导率分析
    Long FILE_CHECK_ORIGINAL_RECORD_3 = 100415l;
    //实验室原始记录表-分光光度法分析
    Long FILE_CHECK_ORIGINAL_RECORD_4 = 100416l;

}
