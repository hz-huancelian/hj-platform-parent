package org.hj.chain.platform.fileresource.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.common.PageVo;
import org.hj.chain.platform.fileresource.entity.FileResource;
import org.hj.chain.platform.fileresource.service.IFileResourceService;
import org.hj.chain.platform.tdo.fileresource.FileResourceAddTdo;
import org.hj.chain.platform.tdo.fileresource.FileResourceEditTdo;
import org.hj.chain.platform.vo.fileresource.FileResourceDisplayVo;
import org.hj.chain.platform.vo.fileresource.FileResourceSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/fileResource")
public class FileResourceController {
    @Autowired
    private IFileResourceService fileResourceService;

    /**
     * todo 条件分页查询文件资源
     * @param pageVo
     * @param sv
     * @return
     */
    @RequestMapping(value = "/findByCondition", method = RequestMethod.GET)
    public Result<IPage<FileResourceDisplayVo>> findByCondition(@ModelAttribute PageVo pageVo,
                                                                @ModelAttribute FileResourceSearchVo sv) {
        sv.setFileType(StrUtil.trimToNull(sv.getFileType()));
        sv.setFileName(StrUtil.trimToNull(sv.getFileName()));
        return fileResourceService.findByCondition(pageVo, sv);
    }

    /**
     * todo 修改文件控制编号
     * @param tdo
     * @return
     */
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Result<Object> updateById(@Validated @RequestBody FileResourceEditTdo tdo) {
        FileResource fileResource = fileResourceService.getById(tdo.getId());
        if(fileResource == null) {
            return ResultUtil.busiError("文件资源不存在！");
        }
        fileResourceService.update(Wrappers.<FileResource>lambdaUpdate()
                .set(FileResource::getFileNo, tdo.getFileNo())
                .set(FileResource::getUpdateUserId, (String) StpUtil.getLoginId())
                .set(FileResource::getUpdateTime, LocalDateTime.now())
                .eq(FileResource::getId, tdo.getId()));
        return ResultUtil.success("文件控制编号修改成功！");
    }

    /**
     * todo 根据ID删除文件资源
     * @param id
     * @return
     */
    @RequestMapping(value = "/removeById/{id}", method = RequestMethod.DELETE)
    public Result<Object> removeById(@PathVariable Long id) {
        if(id == null) {
            return ResultUtil.validateError("文件ID不能为空！");
        }
        FileResource fileResource = fileResourceService.getById(id);
        if(fileResource == null) {
            return ResultUtil.busiError("文件资源不存在！");
        }
        fileResourceService.removeById(id);
        return ResultUtil.success("文件资源删除成功！");
    }

}
