package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.*;
import com.scy.running.page.MyVo;
import com.scy.running.service.ITbPermissionService;
import com.scy.running.service.ITbTaskService;
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
import java.util.*;

/**
 * <p>
 * InnoDB free: 11264 kB 前端控制器
 * </p>
 *
 * @author scy
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/api/tb-task")
@Api(tags = "打卡任务管理的接口")
public class TbTaskController {

    @Autowired
    private ITbTaskService taskService;

    /**
     * 查找打卡任务
     * @param response
     * @param vo
     * @param task
     * @throws IOException
     */
    @ApiOperation("查询打卡任务的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vo", value = "分页对象", defaultValue = "null"),
            @ApiImplicitParam(name = "task", value = "查询条件接收对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select")
    public void selectTask(HttpServletResponse response
            , MyVo vo, TbTask task) throws IOException {

        IPage<TbTask> taskIPage = taskService.selectTask(new Page<>(vo.getCurrent(), vo.getSize()), task);
        Map<String,Object> map= new HashMap<>();
        map.put("params",null);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(taskIPage);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 新增任务
     * @param response
     * @param task
     * @throws IOException
     */
    @ApiOperation("新增任务的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "新增的任务对象", defaultValue = "null"),
            @ApiImplicitParam(name = "classIds", value = "班级id集合")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/insert")
    public void insertTask(HttpServletResponse response, TbTask task,
                            @RequestParam String classIds) throws IOException {
        List<Integer> classIdList = new ArrayList<>();
        // permissionIds不为null且不为空， 重新组装permissionIdList
        if(classIds != null && !classIds.isEmpty()){
            String[] classArr = classIds.split(",");
            for(String c : classArr){
                int temp = Integer.parseInt(c);
                classIdList.add(temp);
            }
        }
        task.setIsOvertime(0); // 0为未超时，1为超时
        taskService.insertTaskByObjAndClassIdList(task, classIdList);

        // 返回的结果集
        ResultInfo result;
        if(task.getTaskId() != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(null);
        }else {
            result = new ResultInfo(Status.WARN);
            result.setMessage("任务发布失败, 发生不知名错误");
            result.setData(null);
        }

        Map<String,Object> map= new HashMap<>();
        map.put("params",task);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    /**
     * 查找任务的信息
     * @param response
     * @param taskId
     * @throws IOException
     */
    @ApiOperation("查询任务的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务Id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    public void selectTaskById(HttpServletResponse response,
                               Integer taskId) throws IOException {

        TbTask tbTask = taskService.selectTaskById(taskId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",taskId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(tbTask != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbTask);
        }else{
            result = new ResultInfo(Status.WARN);
            result.setMessage("任务已不存在，请刷新页面");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改任务
     * @param response
     * @param task
     * @throws IOException
     */
    @ApiOperation("修改任务的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "修改的任务对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public void updateTask(HttpServletResponse response, TbTask task) throws IOException {
        taskService.updateTaskByObj(task);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<String, Object>();
        map.put("params",task);
        map.put("method",RequestMethod.PUT);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 删除任务（包括打卡记录表中的记录）
     * @param response
     * @param task
     * @throws IOException
     */
    @ApiOperation("删除任务的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "删除的任务对象", defaultValue = "null")
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
    public void deleteTask(HttpServletResponse response, TbTask task) throws IOException {
        taskService.deleteTaskByObj(task);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",task);
        map.put("method",RequestMethod.DELETE);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 查找任务完成进度的信息
     * @param response
     * @param taskId
     * @throws IOException
     */
    @ApiOperation("查询任务完成进度的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务Id", defaultValue = "")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/info_task_process")
    public void selectProcessByTaskId(HttpServletResponse response,
                               Integer taskId) throws IOException {

        TbTaskVo tbTaskVo = taskService.selectProcessByTaskId(taskId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",taskId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        if(tbTaskVo != null){
            result = new ResultInfo(Status.SUCCESS);
            result.setData(tbTaskVo);
        }else{
            result = new ResultInfo(Status.WARN);
            result.setMessage("任务已不存在，请刷新页面");
            result.setData(null);
        }
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

}
