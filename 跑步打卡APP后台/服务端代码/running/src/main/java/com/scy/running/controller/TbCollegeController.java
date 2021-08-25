package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.*;
import com.scy.running.page.MyVo;
import com.scy.running.service.ITbCollegeService;
import com.scy.running.service.ITbPunchClockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
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
@RequestMapping("/api/tb-college")
@Api(tags = "学院管理的接口")
public class TbCollegeController {

    @Autowired
    private ITbCollegeService collegeService;


    /**
     * 查找学院
     * @param response
     * @param vo
     * @param college
     * @throws IOException
     */
    @ApiOperation("查询学院的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vo", value = "分页对象", defaultValue = "null"),
            @ApiImplicitParam(name = "college", value = "查询条件接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    public void selectCollege(HttpServletResponse response
            , MyVo vo, TbCollege college) throws IOException {
        /**
         * Page(current,size)
         * current:当前页,long类型
         * size:每页显示的数量,long类型
         * 可参考其构造方法
         */
        IPage<TbCollege> collegeIPage = collegeService.selectCollege(new Page<>(vo.getCurrent(), vo.getSize()), college);
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(collegeIPage);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找所有学院
     * @param response
     * @throws IOException
     */
    @ApiOperation("查询所有学院的接口")
    @RequestMapping(method = RequestMethod.GET, value = "/select_all")
    public void selectCollegeAll(HttpServletResponse response) throws IOException {

        List<TbCollege> tbColleges = collegeService.selectCollegeAll();
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(tbColleges);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 新增学院
     * @param response
     * @param college
     * @throws IOException
     */
    @ApiOperation("新增学院的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "college", value = "新增的学院对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public void insertCollege(HttpServletResponse response, TbCollege college) throws IOException {
        collegeService.insertCollegeByObj(college);

        // 返回的结果集
        ResultInfo result;
        if(college.getCollegeId() != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(college);
        }else {
            result = new ResultInfo(Status.WARN); // 该学院已经存在
            result.setMessage("学院代码唯一！学院已经存在！");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",college);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }




    /**
     * 查找单个学院的信息
     * @param response
     * @param collegeId
     * @throws IOException
     */
    @ApiOperation("查询单个学院的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "collegeId", value = "学院Id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public void selectCollegeById(HttpServletResponse response,
                               Integer collegeId) throws IOException {

        TbCollege tbCollege = collegeService.selectCollegeById(collegeId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",collegeId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(tbCollege != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbCollege);
        }else{
            result = new ResultInfo(Status.WARN);
            result.setMessage("学院已不存在，请刷新页面");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改学院
     * @param response
     * @param college
     * @throws IOException
     */
    @ApiOperation("修改学院的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "college", value = "修改的学院对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updateCollege(HttpServletResponse response, TbCollege college) throws IOException {
        boolean updateResult = collegeService.updateCollegeByObj(college);

        // 返回的结果集
        ResultInfo result;
        if(updateResult){
            // success
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else{
            // 学号已经存在
            result = new ResultInfo(Status.WARN);
            result.setMessage("修改失败，新学院代码对应的学院已经存在，学院代码唯一！");
            result.setData(null);
        }
        Map<String,Object> map= new HashMap<>();
        map.put("params",college);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 删除学院(对应的班级collegeId要置null)
     * @param response
     * @param college
     * @throws IOException
     */
    @ApiOperation("删除学院的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "college", value = "删除的学院对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deleteCollege(HttpServletResponse response, TbCollege college) throws IOException {
        collegeService.deleteCollegeByObj(college);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",college);
        map.put("method",RequestMethod.DELETE);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

}
