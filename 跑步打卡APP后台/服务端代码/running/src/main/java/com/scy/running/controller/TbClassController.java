package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.TbClass;
import com.scy.running.model.TbCollege;
import com.scy.running.model.TbUser;
import com.scy.running.page.MyVo;
import com.scy.running.service.ITbClassService;
import com.scy.running.service.ITbCollegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 11264 kB 前端控制器
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/api/tb-class")
@Api(tags = "班级管理的接口")
public class TbClassController {

    @Autowired
    private ITbClassService classService;


    /**
     * 查找班级（要展示班级所属的学院）
     * @param response
     * @param vo
     * @param tbClass
     * @throws IOException
     */
    @ApiOperation("查询班级的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vo", value = "分页对象", defaultValue = "null"),
            @ApiImplicitParam(name = "tbClass", value = "查询条件接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    public void selectClass(HttpServletResponse response
            , MyVo vo, TbClass tbClass) throws IOException {
        /**
         * Page(current,size)
         * current:当前页,long类型
         * size:每页显示的数量,long类型
         * 可参考其构造方法
         */
        IPage<TbClass> tbClassIPage = classService.selectClass(new Page<>(vo.getCurrent(), vo.getSize()), tbClass);
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(tbClassIPage);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查找所有班级（要展示班级所属的学院）
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询所有班级的接口")
    @RequestMapping(method = RequestMethod.GET, value = "/select_all")
    public void selectClassAll(HttpServletResponse response) throws IOException {

        List<TbClass> tbClasses = classService.selectClassAll();
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(tbClasses);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 新增班级
     * @param response
     * @param tbClass
     * @throws IOException
     */
    @ApiOperation("新增班级的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbClass", value = "新增的班级对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public void insertClass(HttpServletResponse response, TbClass tbClass) throws IOException {
        classService.insertClassByObj(tbClass);

        // 返回的结果集
        ResultInfo result;
        if(tbClass.getClassId() != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbClass);
        }else {
            result = new ResultInfo(Status.WARN); // 该学院已经存在
            result.setMessage("班级代码唯一！班级已经存在！");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",tbClass);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找单个班级的信息
     * @param response
     * @param classId
     * @throws IOException
     */
    @ApiOperation("查询单个班级的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "classId", value = "班级Id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public void selectClassById(HttpServletResponse response,
                                  Integer classId) throws IOException {

        TbClass tbClass = classService.selectClassById(classId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",classId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(tbClass != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbClass);
        }else{
            result = new ResultInfo(Status.WARN);
            result.setMessage("班级已不存在，请刷新页面");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改班级
     * @param response
     * @param tbClass
     * @throws IOException
     */
    @ApiOperation("修改班级的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbClass", value = "修改的班级对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updateClass(HttpServletResponse response, TbClass tbClass) throws IOException {
        boolean updateResult = classService.updateClassByObj(tbClass);

        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.WARN);
            result.setMessage("修改失败，新班级代码对应的班级已经存在，班级代码唯一！");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",tbClass);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 删除班级(对应的学生的class_id设为null)
     * @param response
     * @param tbClass
     * @throws IOException
     */
    @ApiOperation("删除班级的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbClass", value = "删除的班级对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deleteClass(HttpServletResponse response, TbClass tbClass) throws IOException {
        classService.deleteClassByObj(tbClass);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",tbClass);
        map.put("method",RequestMethod.DELETE);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }




}
