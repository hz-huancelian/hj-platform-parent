package org.hj.chain.platform.fileresource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.fileresource.entity.FileResource;
import org.hj.chain.platform.vo.fileresource.FileResourceDisplayVo;
import org.hj.chain.platform.vo.fileresource.FileResourceSearchVo;

public interface IFileResourceService extends IService<FileResource> {
    Result<IPage<FileResourceDisplayVo>> findByCondition(PageVo pageVo, FileResourceSearchVo sv);
    String getFileNoByFileCodeAndOrganId(String fileCode, String organId);
}
