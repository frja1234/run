package com.scy.running.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scy.running.conf.ResultInfo;
import com.scy.running.conf.Status;
import com.scy.running.model.TbPermission;
import com.scy.running.model.TbPunchClock;
import com.scy.running.model.TbTask;
import com.scy.running.service.ITbPunchClockService;
import com.scy.running.service.ITbTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/api/tb-punch-clock")
@Api(tags = "学生打卡管理的接口")
public class TbPunchClockController {

    @Autowired
    private ITbPunchClockService punchClockService;

    /**
     * 根据userId查找当天打卡任务信息
     * @param response
     * @param userId
     * @throws IOException
     */
    @ApiOperation("查询用户当天打卡任务的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", defaultValue = "", required = true)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/select_punch_clock_app")
    public void selectPunchClockByUserIdApp(HttpServletResponse response,
                                        @RequestParam(required = true) Integer userId) throws IOException {

        TbPunchClock tbPunchClock = punchClockService.selectPunchClockByUserIdApp(userId);
        Map<String,Object> map= new HashMap<>();
        map.put("params",userId);
        map.put("method",RequestMethod.GET);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(tbPunchClock);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改打卡记录
     * @param response
     * @param punchClock
     * @throws IOException
     */
    @ApiOperation("修改打卡记录的接口")
    @ApiImplicitParams({
            // @ApiImplicitParam(name = "punchClock", value = "修改的打卡记录对象", defaultValue = "null")
            @ApiImplicitParam(name = "params", value = "params"),
            @ApiImplicitParam(name = "punchClockId", value = "打卡id"),
            @ApiImplicitParam(name = "runTotalLength", value = "累积总路程"),
            @ApiImplicitParam(name = "punchClockState", value = "打卡状态"),
            @ApiImplicitParam(name = "runTotalTime", value = "累积总时长")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public void updatePunchClock(HttpServletResponse response,
                                 String params,
                                 String  punchClockId, String runTotalLength,
                                 String punchClockState, String runTotalTime) throws IOException {
        TbPunchClock punchClock = new TbPunchClock();
        punchClock.setUpdateTime(new Date());
        punchClockService.updatePunchClockByObj(punchClock);

        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",punchClock);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }


    /**
     * 修改打卡记录APP
     * @param response
     * @param params
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/update_app", produces = "application/json;charset=UTF-8")
    public void updatePunchClockAPP(HttpServletResponse response,
                                    @RequestBody JSONObject params) throws IOException {

        TbPunchClock tbPunchClock = getPunchClockFromJsonObj(params);
        tbPunchClock.setUpdateTime(new Date());
        punchClockService.updatePunchClockByObj(tbPunchClock);
        System.out.println(params);
        // 返回的结果集
        ResultInfo result;
        result = new ResultInfo(Status.SUCCESS);
        result.setData(null);

        Map<String,Object> map= new HashMap<>();
        map.put("params",params);
        map.put("method",RequestMethod.POST);
        map.put("result", result);
        response.setContentType("text/javascript; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
    }

    private TbPunchClock getPunchClockFromJsonObj(JSONObject params){
        TbPunchClock tbPunchClock = new TbPunchClock();
        String isStart = (String) params.get("isStart");
        if(isStart.equals("1")){
            tbPunchClock.setPunchClockStartTime(new Date());
        }
        Double punchClockIdDouble = Double.valueOf((String) params.get("punchClockId"));
        Integer punchClockId = Integer.valueOf(punchClockIdDouble.intValue());
        Integer runTotalLength = Integer.valueOf((String) params.get("runTotalLength"));
        Integer runTotalTime = Integer.valueOf((String) params.get("runTotalTime"));
        Integer punchClockState = Integer.valueOf((String) params.get("punchClockState"));
        if(punchClockState != null && punchClockState.intValue() == 1){
            tbPunchClock.setPunchClockEndTime(new Date());
        }
        tbPunchClock.setPunchClockId(punchClockId);
        tbPunchClock.setRunTotalLength(runTotalLength);
        tbPunchClock.setRunTotalTime(runTotalTime);
        tbPunchClock.setPunchClockState(punchClockState);
        return  tbPunchClock;
    }

}
