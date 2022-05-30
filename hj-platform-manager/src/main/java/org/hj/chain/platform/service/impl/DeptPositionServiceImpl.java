package org.hj.chain.platform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.component.DictUtils;
import org.hj.chain.platform.mapper.DeptPositionRelMapper;
import org.hj.chain.platform.model.DeptPositionRel;
import org.hj.chain.platform.service.IDeptPositionService;
import org.hj.chain.platform.vo.DeptPositionRelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/5/13  6:02 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/05/13    create
 */
@Service
public class DeptPositionServiceImpl implements IDeptPositionService {
    @Autowired
    private DeptPositionRelMapper deptPositionRelMapper;
    @Autowired
    private DictUtils dictUtils;

    @CacheEvict(value = "platform:dept:position", key = "#deptId")
    @Override
    public int saveRel(Long deptId, Long positionId) {
        LocalDateTime now = LocalDateTime.now();
        DeptPositionRel rel = new DeptPositionRel();
        rel.setDeptId(deptId)
                .setPositionId(positionId)
                .setCreateTime(now);
        return deptPositionRelMapper.insert(rel);
    }

    @CacheEvict(value = "platform:dept:position", key = "#deptId")
    @Override
    public int delRelById(Long id) {
        return deptPositionRelMapper.delete(Wrappers.<DeptPositionRel>lambdaQuery().eq(DeptPositionRel::getId, id));
    }

    @Cacheable(value = "platform:dept:position", key = "#deptId")
    @Override
    public List<DeptPositionRelVo> findRelByDeptId(Long deptId) {
        List<DeptPositionRel> positionRels = deptPositionRelMapper.selectList(Wrappers.<DeptPositionRel>lambdaQuery().eq(DeptPositionRel::getDeptId, deptId));
        if (positionRels != null && !positionRels.isEmpty()) {
            List<DeptPositionRelVo> relVos = positionRels.stream().map(item -> {
                DeptPositionRelVo relVo = new DeptPositionRelVo();
                relVo.setId(item.getId())
                        .setPositionId(item.getPositionId())
                        .setPositionVal(dictUtils.getAllDictMap().get(String.valueOf(item.getPositionId())));
                return relVo;
            }).collect(Collectors.toList());

            return relVos;

        }
        return null;
    }
}