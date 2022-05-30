package org.hj.chain.platform.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouterUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.hj.chain.platform.constants.BusiConstants;
import org.hj.chain.platform.vo.LoginOutputVo;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Project : hj-platform-parent
 * @Description : TODO SaToken配置
 * @Author : lijinku
 * @Iteration : 1.0
 * @Date : 2021/4/27  10:06 下午
 * @ModificationHistory Who          When          What
 * ----------   ------------- -----------------------------------
 * lijinku          2021/04/27    create
 */
@Slf4j
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册sa-token的拦截器
    private final List<String> ignoreList = Arrays.asList("/sys/login/doLoginByUsername", "/sys/login/doLoginByPhone", "/login/auth",
            "/login/doLogin", "/login/doSlientLogin", "/login/doLoginByUsername", "/login/logout", "/dataImport/downloadCustomerExcel",
            "/dataImport/downloadEmployeeExcel", "/dataImport/downloadEquipmentExcel", "/dataImport/uploadEquipent", "/dataImport/exportEquipment",
            "/dataImport/uploadEmployee", "/dataImport/exportEmployee", "/dataImport/uploadCustomer", "/dataImport/exportCustomer",
            "/download/downloadControlFile");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaRouteInterceptor((request, response, handler) -> {

            // 拦截所有路由，排除多个路径/user/doLogin 用于开放登录
            SaRouterUtil.match(Arrays.asList("/**"), ignoreList, () -> StpUtil.checkLogin());


            // 角色认证 -- 拦截以 admin 开头的路由，必须具备[admin]角色或者[super-admin]角色才可以通过认证
//            SaRouterUtil.match("/admin/**", () -> StpUtil.checkRoleOr("admin", "super-admin"));
//
//            // 权限认证 -- 不同模块, 校验不同权限
//            SaRouterUtil.match("/user/**", () -> StpUtil.checkPermission("user"));
//            SaRouterUtil.match("/admin/**", () -> StpUtil.checkPermission("admin"));
//            SaRouterUtil.match("/goods/**", () -> StpUtil.checkPermission("goods"));
//            SaRouterUtil.match("/orders/**", () -> StpUtil.checkPermission("orders"));
//            SaRouterUtil.match("/notice/**", () -> StpUtil.checkPermission("notice"));
//            SaRouterUtil.match("/comment/**", () -> StpUtil.checkPermission("comment"));
            log.info("requstPath:" + request.getRequestPath());
            if (!ignoreList.contains(request.getRequestPath())) {
                log.info("校验角色、权限");
                SaSession session = StpUtil.getSession();
                LoginOutputVo loginOutputVo = session.getModel(BusiConstants.SESSION_USER_KEY, LoginOutputVo.class);
                if (!loginOutputVo.getUserType().equals("0")) {
                    Set<String> resources = loginOutputVo.getResources();
                    List<String> permissions = new ArrayList<>(resources);
                    StpUtil.checkPermissionOr(permissions.toArray(new String[permissions.size()]));
//                    String path = URLUtil.getPath(request.getRequestPath());
//                    StpUtil.checkPermission(path);
                }
            }

            log.info("跳过");

        })).addPathPatterns("/**");
    }

}