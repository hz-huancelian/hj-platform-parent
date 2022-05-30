package org.hj.chain.platform.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.contract.entity.ContractInfo;
import org.hj.chain.platform.tdo.contract.*;
import org.hj.chain.platform.vo.approval.ContractAuditRecordVo;
import org.hj.chain.platform.vo.contract.ContPerfectQryVo;
import org.hj.chain.platform.vo.contract.ContractInfoVo;
import org.hj.chain.platform.vo.contract.ContractSearchVo;
import org.hj.chain.platform.vo.contract.SubcontractVo;
import org.hj.chain.platform.word.ContractData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chh
 * @since 2021-04-30
 */
public interface IContractInfoService extends IService<ContractInfo> {

    /**
     * TODO 合同列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/6 6:55 下午
     */
    IPage<ContractInfoVo> findContInfosByCondition(PageVo pageVo, ContractSearchVo sv);

    /**
     * TODO 合同列表
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/8/6 6:55 下午
     */
    IPage<ContractInfoVo> findContApprovalsByCondition(PageVo pageVo, ContractSearchVo sv);


    /**
     * TODO 新增合同
     *
     * @param bo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:50 下午
     */
    int addContract(ContractBo bo);


    /**
     * TODO 分包合同查看
     *
     * @param pageVo
     * @param sv
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 11:45 上午
     */
    IPage<SubcontractVo> findSubContInfosByCondition(PageVo pageVo, ContractSearchVo sv);


    /**
     * TODO 新增分包合同
     *
     * @param bos
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/14 10:51 下午
     */
    int addSubContract(List<SubcontractBo> bos);

    /**
     * TODO 在线制作完善合同
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 3:47 下午
     */
    Result<Object> pefectContOnline(ContractSaveTdo tdo);

    /**
     * TODO 上传合同完善
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 4:30 下午
     */
    Result<Object> perfectContByUpload(ContractSave2Tdo tdo);

    /**
     * TODO 提交合同
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 4:37 下午
     */
    Result<Object> submitCont(Long contId);

    /**
     * TODO 合同审核
     *
     * @param tdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 4:47 下午
     */
    Result<Object> auditCont(ContractAuditTdo tdo);


    /**
     * TODO 根据合同ID查看审核记录
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/9 12:02 上午
     */
    Result<List<ContractAuditRecordVo>> findAuditRecordsByContId(Long contId);

    /**
     * TODO 合同作废
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 4:58 下午
     */
    Result<Object> invalidCont(Long contId);

    /**
     * TODO 合同恢复
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 4:59 下午
     */
    Result<Object> restoreCont(Long contId);

    /**
     * TODO 查询合同完善信息
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/15 5:17 下午
     */
    Result<ContPerfectQryVo> findContPerfectInfoById(Long contId);


    /**
     * TODO 根据合同编号获取合同基础信息
     *
     * @param contCode
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/19 10:03 上午
     */
    ContractData findLoadContractByContCode(String contCode);

    /**
     * TODO 根据ID查看合同附件地址
     *
     * @param contId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/22 9:09 下午
     */
    Result<String> findContFileById(Long contId);

    /**
     * TODO 上传分包合同
     *
     * @param file
     * @param id
     * @param signDate
     * @param validity
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/5/30 11:26 上午
     */
    Result<Object> uploadSubContFile(MultipartFile file, Long id, String signDate, String validity);

    Result<Object> checkSupContCode(String supContCode);
}
