package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.TbPermission;
import com.scy.running.page.MyVo;
import com.scy.running.service.ITbPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 11264 kB 前端控制器
 * </p>
 *
 * @author scy
 * @since 2021-07-12
 */
@RestController
@RequestMapping("/api/tb-permission")
@Api(tags = "权限管理的接口")
public class TbPermissionController {

    @Autowired
    private ITbPermissionService permissionService;

    /**
     * 查找权限
     * @param response
     * @param permission
     * @throws IOException
     */
    @ApiOperation("查询权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vo", value = "分页对象", defaultValue = "null"),
            @ApiImplicitParam(name = "permission", value = "查询条件接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    public void selectPermission(HttpServletResponse response
            , MyVo vo, TbPermission permission) throws IOException {
        // 根据用户的权限来确定的，暂时写定，到时候通过session动态获取
        //Integer defFlag = 0;
        //permission.setDefFlag(defFlag);
        /**
         * Page(current,size)
         * current:当前页,long类型
         * size:每页显示的数量,long类型
         * 可参考其构造方法
         */
        IPage<TbPermission> permissionIPage = permissionService.selectPermission(new Page<>(vo.getCurrent(), vo.getSize()), permission);
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(permissionIPage);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找权限根据id
     * @param response
     * @param permissionId
     * @throws IOException
     */
    @ApiOperation("查询权限信息的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permissionId", value = "权限id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public void selectPermissionById(HttpServletResponse response
            , Integer permissionId) throws IOException {
        TbPermission tbPermission = permissionService.selectPermissionById(permissionId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",permissionId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(tbPermission != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbPermission);
        }else{
            result = new ResultInfo(Status.WARN);
            result.setMessage("权限已不存在，请刷新页面");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 新增权限
     * @param response
     * @param permission
     * @throws IOException
     */
    @ApiOperation("新增权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permission", value = "新增的权限对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public void insertPermission(HttpServletResponse response, TbPermission permission) throws IOException {
        permissionService.insertPermissionByObj(permission);

        // 返回的结果集
        ResultInfo result;
        if(permission.getPermissionId() != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(permission);
        }else {
            result = new ResultInfo(Status.WARN);
            result.setMessage("新增失败, 权限名称可能已经存在");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",permission);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 删除权限
     * @param response
     * @param permission
     * @throws IOException
     */
    @ApiOperation("删除权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permission", value = "删除的权限对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deletePermission(HttpServletResponse response, TbPermission permission) throws IOException {
        permissionService.deletePermissionByObj(permission);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",permission);
        map.put("method",RequestMethod.DELETE);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 注销权限
     * @param response
     * @param permission
     * @throws IOException
     */
    @ApiOperation("注销权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permission", value = "注销的权限对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/cancel")
    public void cancelPermission(HttpServletResponse response, TbPermission permission) throws IOException {
        permissionService.cancelPermissionByObj(permission);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",permission);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改权限
     * @param response
     * @param permission
     * @throws IOException
     */
    @ApiOperation("修改权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permission", value = "修改的权限对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updatePermission(HttpServletResponse response, TbPermission permission) throws IOException {
        permission.setUpdateTime(new Date());
        permissionService.updatePermissionByObj(permission);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",permission);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 激活权限
     * @param response
     * @param permission
     * @throws IOException
     */
    @ApiOperation("激活权限的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permission", value = "激活的权限对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/activate")
    public void activatePermission(HttpServletResponse response, TbPermission permission) throws IOException {
        // 激活权限
        permissionService.activatePermissionByObj(permission);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",permission);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

}
