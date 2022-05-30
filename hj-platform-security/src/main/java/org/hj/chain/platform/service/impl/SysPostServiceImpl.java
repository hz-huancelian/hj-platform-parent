package org.hj.chain.platform.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.mapper.SysPostMapper;
import org.hj.chain.platform.model.SysPost;
import org.hj.chain.platform.service.ISysPostService;
import org.hj.chain.platform.tdo.SysPostTdo;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.hj.chain.platform.vo.SysPostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lijinku
 * @Project : hj-platform-parent
 * @description TODO
 * @Iteration : 1.0
 * @Date : 2021/7/26  6:29 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/07/26    create
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public List<SysPostVo> selectPostList(String postName, String postCode, String status) {
        postName = StrUtil.trimToNull(postName);
        postCode = StrUtil.trimToNull(postCode);
        status = StrUtil.trimToNull(status);
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LambdaQueryWrapper queryWrapper = Wrappers.<SysPost>lambdaQuery().eq(SysPost::getOrganId, loginOutputVo.getOrganId())
                .eq(postCode != null, SysPost::getPostCode, postCode)
                .eq(status != null, SysPost::getStatus, status)
                .like(postName != null, SysPost::getPostName, postName);
        queryWrapper.last("ORDER BY status ASC, CONVERT( post_name USING gbk ) COLLATE gbk_chinese_ci ASC");
        List<SysPost> posts = sysPostMapper.selectList(queryWrapper);

        if (posts != null && !posts.isEmpty()) {
            List<SysPostVo> postVos = posts.stream().map(item -> {
                SysPostVo postVo = buildSysPostVo(item);
                return postVo;
            }).collect(Collectors.toList());

            return postVos;
        }

        return null;
    }


    /**
     * TODO 构建职位VO
     *
     * @param item
     * @Author: lijinku
     * @Iteration : 1.0
     * @Date: 2021/7/27 3:03 下午
     */
    private SysPostVo buildSysPostVo(SysPost item) {
        SysPostVo postVo = new SysPostVo();
        postVo.setPostId(item.getPostId())
                .setPostCode(item.getPostCode())
                .setPostName(item.getPostName())
                .setPostSort(item.getPostSort())
                .setRemark(item.getRemark())
                .setStatus(item.getStatus())
                .setCreateTime(item.getCreateTime());
        return postVo;
    }

    @Override
    public List<SysPostVo> selectPostAll() {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        List<SysPost> posts = sysPostMapper.selectList(Wrappers.<SysPost>lambdaQuery()
                .eq(SysPost::getOrganId, loginOutputVo.getOrganId())
                .eq(SysPost::getStatus, "0")
                .orderByAsc(SysPost::getPostSort));

        if (posts != null && !posts.isEmpty()) {
            return posts.stream().map(item -> buildSysPostVo(item)).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public List<SysPostVo> selectPostsByUserId(String userId) {
        return sysPostMapper.selectPostsByUserId(userId);
    }

    @Override
    public List<Long> selectPostIdsByUserId(String userId) {
        return sysPostMapper.selectPostIdsByUserId(userId);
    }

    @Override
    public SysPostVo selectPostById(Long postId) {
        SysPost sysPost = sysPostMapper.selectById(postId);
        if (sysPost != null) {
            return buildSysPostVo(sysPost);
        }
        return null;
    }

    @Transactional
    @Override
    public int deletePostByIds(String ids) {
        LocalDateTime now = LocalDateTime.now();
        String[] idArrays = ids.split(",");
        List<Long> idList = Arrays.stream(idArrays).map(item -> Long.valueOf(item)).collect(Collectors.toList());
        int count = sysPostMapper.update(null, Wrappers.<SysPost>lambdaUpdate()
                .set(SysPost::getStatus, "1")
                .set(SysPost::getUpdateTime, now)
                .in(SysPost::getPostId, idList));
        return count;
    }

    @Override
    public int deletePostById(Long id) {
        LocalDateTime now = LocalDateTime.now();
        int count = sysPostMapper.update(null, Wrappers.<SysPost>lambdaUpdate()
                .set(SysPost::getStatus, "1")
                .set(SysPost::getUpdateTime, now)
                .eq(SysPost::getPostId, id));
        return count;
    }

    @Override
    public int insertPost(SysPostTdo post) {
        Long postId = post.getPostId();
        String postCode = post.getPostCode();
        String postName = post.getPostName();
//        int res = checkPostCodeUnique(postId, postCode);
//
//        if (res == 0) {
//            return 0;
//        }

//        int res = checkPostNameUnique(postId, postName);
//
//        if (res == 0) {
//            return 0;
//        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();

        SysPost po = new SysPost();
        po.setPostCode(postCode)
                .setPostName(postName)
                .setPostSort(post.getPostSort())
                .setRemark(post.getRemark())
                .setStatus("0")
                .setOrganId(loginOutputVo.getOrganId())
                .setCreateTime(now);

        int count = sysPostMapper.insert(po);
        return count;
    }

    @Override
    public int updatePost(SysPostTdo post) {
        Long postId = post.getPostId();
        String postCode = post.getPostCode();
        String postName = post.getPostName();
//        int res = checkPostCodeUnique(postId, postCode);
//
//        if (res == 0) {
//            return 0;
//        }
//
//        res = checkPostNameUnique(postId, postName);
//
//        if (res == 0) {
//            return 0;
//        }

        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
        LocalDateTime now = LocalDateTime.now();

        SysPost po = new SysPost();
        po.setPostCode(postCode)
                .setPostName(postName)
                .setPostSort(post.getPostSort())
                .setRemark(post.getRemark())
                .setStatus(post.getStatus())
                .setOrganId(loginOutputVo.getOrganId())
                .setPostId(postId)
                .setUpdateTime(now);

        int count = sysPostMapper.updateById(po);
        return count;
    }


    @Override
    public int checkPostNameUnique(Long postId, String postName) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        postId = postId == null ? -1 : postId;
        SysPost dbPost = sysPostMapper.selectOne(Wrappers.<SysPost>lambdaQuery()
                .select(SysPost::getPostId)
                .eq(SysPost::getOrganId, loginOutputVo.getOrganId())
                .eq(SysPost::getPostName, postName));

        if (dbPost != null && !dbPost.getPostId().equals(postId)) {
            return 0;
        }
        return 1;
    }

    @Override
    public int checkPostCodeUnique(Long postId, String postCode) {
        SaSession session = StpUtil.getSession();
        LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);

        postId = postId == null ? -1 : postId;
        SysPost dbPost = sysPostMapper.selectOne(Wrappers.<SysPost>lambdaQuery()
                .select(SysPost::getPostId)
                .eq(SysPost::getOrganId, loginOutputVo.getOrganId())
                .eq(SysPost::getPostCode, postCode));

        if (dbPost != null && !dbPost.getPostId().equals(postId)) {
            return 0;
        }
        return 1;
    }


    @Override
    public List<String> selectUserPostGroup(String userId) {
        return sysPostMapper.selectUserPostGroup(userId);
    }
}