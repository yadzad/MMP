package com.qi.demo.controller;

import com.qi.demo.DTO.ProjectDTO;
import com.qi.demo.VO.ResultVO;
import com.qi.demo.dataobject.Project;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.service.impl.ProjectServiceImpl;
import com.qi.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/nr/project")
@Slf4j
public class ProjectController {
    @Autowired
    ProjectServiceImpl projectService;

    @PostMapping("/create")
    public ResultVO create(@RequestParam(value = "name") String name) throws Exception {
        //记录开始时间
        long startTime = System.currentTimeMillis();

        //检查该工程名是否已存在
        if (projectService.findOneByName(name) != null){
            return ResultVOUtil.error(ResultEnum.PROJECT_NAME_EXISTED);
        }
        ProjectDTO result = projectService.create(name);
        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(result, runTime);
    }

    @PostMapping("/find/id")
    public ResultVO findById(@RequestParam(value = "id") String id){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        //检查id是否符合规范
        if(id.length() != 19){
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        ProjectDTO result = projectService.findOneById(id);
        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(result, runTime);
    }

    @PostMapping("find/name")
    private ResultVO findByName(@RequestParam(value = "name") String name){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        //检查该工程名是否已存在
        if (projectService.findOneByName(name) == null){
            return ResultVOUtil.error(ResultEnum.PROJECT_NAME_NOT_EXISTED);
        }
        ProjectDTO result = projectService.findOneByName(name);
        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(result,runTime);
    }

    @PostMapping("update")
    private ResultVO update(@RequestParam(value = "id") String id) throws Exception{
        //记录开始时间
        long startTime = System.currentTimeMillis();

        //检查id是否符合规范
        if(id.length() != 19){
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }

        //检查该工程ID是否存在
        if(projectService.findOneById(id) == null){
            return ResultVOUtil.error(ResultEnum.PROJECT_ID_NOT_EXISTED);
        }
        ProjectDTO result = projectService.updateProjectUpdateTime(id);
        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return  ResultVOUtil.successWithRunTIme(result, runTime);
    }

    @PostMapping("find/list")
    private ResultVO findByList(){
        //记录开始时间
        long startTime = System.currentTimeMillis();
        List<ProjectDTO> result = projectService.findAllPorjects();

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        return ResultVOUtil.successWithRunTIme(result,runTime);
    }

    @PostMapping("delete")
    private ResultVO delete(@RequestParam(value = "id") String id) throws Exception{
        //记录开始时间
        long startTime = System.currentTimeMillis();
        Boolean result = projectService.deleteProject(id);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        //检查该工程ID是否存在
        if(projectService.findOneById(id) == null){
            return ResultVOUtil.error(ResultEnum.PROJECT_ID_NOT_EXISTED);
        }
        return ResultVOUtil.successWithRunTIme(result, runTime);
    }

}
