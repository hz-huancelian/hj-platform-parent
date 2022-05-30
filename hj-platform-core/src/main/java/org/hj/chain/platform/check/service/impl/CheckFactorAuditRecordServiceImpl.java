package org.hj.chain.platform.check.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.check.entity.CheckFactorAuditRecord;
import org.hj.chain.platform.check.mapper.CheckFactorAuditRecordMapper;
import org.hj.chain.platform.check.service.ICheckFactorAuditRecordService;
import org.springframework.stereotype.Service;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 1.0
 * @Date : 2021-05-15
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * chh    2021-05-15
 */
@Service
public class CheckFactorAuditRecordServiceImpl extends ServiceImpl<CheckFactorAuditRecordMapper, CheckFactorAuditRecord>
        implements ICheckFactorAuditRecordService {
}
