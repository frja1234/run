package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbRole;
import com.scy.running.page.MyVo;
import com.scy.running.service.ITbRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 11264 kB 前端控制器
 * </p>
 *
 * @author scy
 * @since 2021-07-09
 */
@RestController
@RequestMapping("/api/tb-role")
@Api(tags = "角色管理的接口")
public class TbRoleController {


    @Autowired
    private ITbRoleService roleService;

    /**
     * 查找角色
     * @param response
     * @param role
     * @throws IOException
     */
    @ApiOperation("查询角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vo", value = "分页对象", defaultValue = "null"),
            @ApiImplicitParam(name = "role", value = "查询条件接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    public void selectRole(HttpServletResponse response,
                                 MyVo vo, TbRole role) throws IOException {
        // 根据用户的权限来确定的，暂时写定，到时候通过session动态获取
        /**
         * Page(current,size)
         * current:当前页,long类型
         * size:每页显示的数量,long类型
         * 可参考其构造方法
         */
        IPage<TbRole> roleIPage = roleService.selectRole(new Page<>(vo.getCurrent(), vo.getSize()), role);
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(roleIPage);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找所有角色
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询所有角色的接口")
    @RequestMapping(method = RequestMethod.GET, value = "/select_all")
    public void selectRoleNoPage(HttpServletResponse response) throws IOException {
        List<TbRole> tbRoles = roleService.selectRoleNoPage();
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(tbRoles);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查找单个角色的信息
     * @param response
     * @param roleId
     * @throws IOException
     */
    @ApiOperation("查询角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public void selectRoleById(HttpServletResponse response,
                           Integer roleId) throws IOException {

        TbRole tbRole = roleService.selectRoleById(roleId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",roleId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(tbRole != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbRole);
        }else{
            result = new ResultInfo(Status.WARN);
            result.setMessage("角色已不存在，请刷新页面");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 根据角色id查询角色权限表中的角色权限id集合
     * @param response
     * @param roleId
     * @throws IOException
     */
    @ApiOperation("查询角色权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色Id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info_permission")
    public void selectRolePermissionByRoleId(HttpServletResponse response,
                               Integer roleId) throws IOException {

        List<Integer> permissionIds = roleService.selectRolePermissionByRoleId(roleId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",roleId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(permissionIds != null && !permissionIds.isEmpty()){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(permissionIds);
        }else{
            result = new ResultInfo(Status.SUCCESS);
            result.setMessage("该角色仍未授予权限");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 新增角色
     * @param response
     * @param role
     * @throws IOException
     */
    @ApiOperation("新增角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "新增的角色对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public void insertRole(HttpServletResponse response, TbRole role) throws IOException {
        roleService.insertRoleByObj(role);

        // 返回的结果集
        ResultInfo result;
        if(role.getRoleId() != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(role);
        }else {
            result = new ResultInfo(Status.WARN);
            result.setMessage("新增失败, 角色代码可能已经存在");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",role);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 角色授权
     * @param response
     * @param roleId
     * @param permissionIds
     * @throws IOException
     */
    @ApiOperation("角色授权的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "需要授权的角色", required = true),
            @ApiImplicitParam(name = "permissionIds", value = "权限集合")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/grant")
    public void grantRole(HttpServletResponse response,
                          @RequestParam(required = true) Integer roleId,
                          @RequestParam String permissionIds ) throws IOException {

        List<Integer> permissionIdList = new ArrayList<>();
        // permissionIds不为null且不为空， 重新组装permissionIdList
        if(permissionIds != null && !permissionIds.isEmpty()){
            String[] permissionArr = permissionIds.split(",");
            for(String p : permissionArr){
                int temp = Integer.parseInt(p);
                permissionIdList.add(temp);
            }
        }
        // 授权操作
        roleService.grantRolePermissionByRoleIdAndPermissionIds(roleId, permissionIdList);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);
        Map<String,Object> map= new HashMap<>();
        // 参数
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("permissionIds", permissionIds);
        map.put("params",params);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 删除角色(包括修改对应的用户角色Id 以及 删除tb-role-permission表中的角色权限)
     * @param response
     * @param role
     * @throws IOException
     */
    @ApiOperation("删除角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "删除的角色对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deleteRole(HttpServletResponse response, TbRole role) throws IOException {
        roleService.deleteRoleByObj(role);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",role);
        map.put("method",RequestMethod.DELETE);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 注销角色
     * @param response
     * @param role
     * @throws IOException
     */
    @ApiOperation("注销角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "注销的角色对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/cancel")
    public void cancelRole(HttpServletResponse response, TbRole role) throws IOException {
        roleService.cancelRoleByObj(role);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",role);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 激活角色
     * @param response
     * @param role
     * @throws IOException
     */
    @ApiOperation("激活角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "激活的角色对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/activate")
    public void activateRole(HttpServletResponse response, TbRole role) throws IOException {
        roleService.activateRoleByObj(role);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",role);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 修改角色
     * @param response
     * @param role
     * @throws IOException
     */
    @ApiOperation("修改角色的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "修改的角色对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updateRole(HttpServletResponse response, TbRole role) throws IOException {
        roleService.updateRoleByObj(role);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<String, Object>();
        map.put("params",role);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

}
