package org.hj.chain.platform.controller;

import cn.hutool.core.util.StrUtil;
import org.hj.chain.platform.Result;
import org.hj.chain.platform.ResultUtil;
import org.hj.chain.platform.service.ISysPostService;
import org.hj.chain.platform.tdo.SysPostTdo;
import org.hj.chain.platform.vo.SysPostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/27  3:34 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/27    create
 */
@RestController
@RequestMapping("/post")
public class SysPostController {

    @Autowired
    private ISysPostService sysPostService;

    /**
     * TODO 条件查询职位列表
     *
     * @param postName
     * @param postCode
     * @param status
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:37 下午
     */
    @RequestMapping(value = "/selectPostList", method = RequestMethod.GET)
    public Result<List<SysPostVo>> selectPostList(@RequestParam String postName, @RequestParam String postCode, @RequestParam String status) {

        List<SysPostVo> postVos = sysPostService.selectPostList(postName, postCode, status);
        return ResultUtil.data(postVos);
    }


    /**
     * TODO 查询所有职位列表
     *
     * @param
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:38 下午
     */
    @RequestMapping(value = "/selectPostAll", method = RequestMethod.GET)
    public Result<List<SysPostVo>> selectPostAll() {

        List<SysPostVo> postVos = sysPostService.selectPostAll();
        return ResultUtil.data(postVos);
    }


    /**
     * TODO 根据用户ID查看职位列表
     *
     * @param userId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:41 下午
     */
    @RequestMapping(value = "/selectPostsByUserId/{userId}", method = RequestMethod.GET)
    public Result<List<SysPostVo>> selectPostsByUserId(@PathVariable String userId) {
        if (userId == null) {
            return ResultUtil.validateError("用户ID不能为空！");
        }
        List<SysPostVo> postVos = sysPostService.selectPostsByUserId(userId);
        return ResultUtil.data(postVos);
    }


    /**
     * TODO 根据职位ID查看职位详细信息
     *
     * @param postId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:43 下午
     */
    @RequestMapping(value = "/selectPostById/{postId}", method = RequestMethod.GET)
    public Result<SysPostVo> selectPostById(@PathVariable Long postId) {
        if (postId == null) {
            return ResultUtil.validateError("职位ID不能为空！");
        }
        SysPostVo postVo = sysPostService.selectPostById(postId);
        return ResultUtil.data(postVo);
    }


    /**
     * TODO 根据ID删除
     *
     * @param postId
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/29 1:19 下午
     */
    @RequestMapping(value = "/deletePostById/{postId}", method = RequestMethod.GET)
    public Result<Object> deletePostByIds(@PathVariable Long postId) {
        if (postId == null) {
            return ResultUtil.validateError("职位ID不能为空！");
        }
        int count = sysPostService.deletePostById(postId);

        if (count > 0) {
            return ResultUtil.success("删除成功！");
        }
        return ResultUtil.error("删除失败！");
    }

    /**
     * TODO 批量删除职位
     *
     * @param postIds
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:47 下午
     */
    @RequestMapping(value = "/deletePostByIds/{postIds}", method = RequestMethod.GET)
    public Result<Object> deletePostByIds(@PathVariable String postIds) {
        if (StrUtil.isBlank(postIds)) {
            return ResultUtil.validateError("职位ID不能为空！");
        }
        int count = sysPostService.deletePostByIds(postIds);

        if (count > 0) {
            return ResultUtil.success("删除成功！");
        }
        return ResultUtil.error("删除失败！");
    }


    /**
     * TODO 新增职位
     *
     * @param postTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:50 下午
     */
    @RequestMapping(value = "/insertPost", method = RequestMethod.POST)
    public Result<Object> insertPost(@RequestBody SysPostTdo postTdo) {
        int count = sysPostService.insertPost(postTdo);

        if (count > 0) {
            return ResultUtil.success("新增成功！");
        }
        return ResultUtil.error("新增失败！");
    }

    /**
     * TODO 更新职位
     *
     * @param postTdo
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:50 下午
     */
    @RequestMapping(value = "/updatePost", method = RequestMethod.POST)
    public Result<Object> updatePost(@RequestBody SysPostTdo postTdo) {
        if (postTdo.getPostId() == null) {
            return ResultUtil.validateError("职位ID不能为空！");
        }
        int count = sysPostService.updatePost(postTdo);

        if (count > 0) {
            return ResultUtil.success("更新成功！");
        }
        return ResultUtil.error("更新失败，职位名称或职位编码重复！");
    }
}