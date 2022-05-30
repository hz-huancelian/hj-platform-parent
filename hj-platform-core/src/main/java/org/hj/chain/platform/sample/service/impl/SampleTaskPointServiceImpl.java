package org.hj.chain.platform.sample.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.chain.platform.sample.entity.SampleTaskPoint;
import org.hj.chain.platform.sample.mapper.SampleTaskPointMapper;
import org.hj.chain.platform.sample.service.ISampleTaskPointService;
import org.springframework.stereotype.Service;

/**
 * @Project : hj-platform-parent
 * @Description : TODO
 * @Author : chh
 * @Iteration : 3.0
 * @Date : 2022/3/13
 */
@Service
public class SampleTaskPointServiceImpl extends ServiceImpl<SampleTaskPointMapper, SampleTaskPoint>
        implements ISampleTaskPointService {
}
