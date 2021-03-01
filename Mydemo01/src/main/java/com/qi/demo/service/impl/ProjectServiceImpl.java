package com.qi.demo.service.impl;

import com.qi.demo.DTO.ProjectDTO;
import com.qi.demo.dataobject.Project;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.exception.NrException;
import com.qi.demo.repository.ProjectRepository;
import com.qi.demo.service.ProjectService;
import com.qi.demo.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public ProjectDTO create(String name) throws Exception {
        /**
         * 更新项目
         * 先更新缓存 后更新数据库
         * */
        //查询缓存中是否存在该name
        ValueOperations<String, Project> operations1 = redisTemplate.opsForValue();
        String projectName = "Project_name_" + name;
        boolean hasKey = redisTemplate.hasKey(projectName); //判断缓存中是否有该KEY

        //如果缓存中存在，直接返回
        if (hasKey){
            Project project = operations1.get(projectName);
            ProjectDTO projectDTO = new ProjectDTO();
            BeanUtils.copyProperties(project, projectDTO);
            return projectDTO;
        }
        else {
         //如果不存在，查询数据库,在缓存中创建
            Project project = projectRepository.findByProjectName(name);
            if (project == null){
                //如果数据库中没有，则在数据库中创建,更新缓存
                Project newProject = new Project();
                newProject.setProjectName(name);
                String projectID = KeyUtil.genUniqueKey();
                newProject.setProjectId(projectID);
                projectRepository.save(newProject);

                String projectKeyId = "Project_id_" + projectID;
                operations1.set(projectKeyId,newProject,3, TimeUnit.HOURS);
                operations1.set(projectName, newProject, 3, TimeUnit.HOURS);    //过期时间3小时
                ProjectDTO projectDTO = new ProjectDTO();
                BeanUtils.copyProperties(newProject, projectDTO);
                return projectDTO;
            }
            else{
                //如果数据库中存在则返回数据库中的对象,同时创建缓存
                String projectKeyId = "Project_id_" + project.getProjectId();
                operations1.set(projectKeyId, project, 3, TimeUnit.HOURS);    //过期时间3小时
                operations1.set(projectName, project, 3, TimeUnit.HOURS);    //过期时间3小时
                ProjectDTO projectDTO = new ProjectDTO();
                BeanUtils.copyProperties(project, projectDTO);
                return projectDTO;
            }

        }
//        //新建Project对象，设置相关成员变量
//        Project project = new Project();
//        project.setProjectName(name);
//        project.setProjectId(KeyUtil.genUniqueKey());
//
//        //调用DAO方法
//        Project projectResult = projectRepository.save(project);
//        //查询该对象
//        ProjectDTO projectDTO = new ProjectDTO();
//        BeanUtils.copyProperties(projectResult, projectDTO);
//        //无法显示时间
//        return projectDTO;
    }

    @Override
    public ProjectDTO findOneById(String id) {
        //在缓存中找
        ValueOperations<String, Project> operations1 = redisTemplate.opsForValue();
        String projectKeyId = "Project_id_" + id;
        boolean hasKey = redisTemplate.hasKey(projectKeyId); //判断缓存中是否有该KEY
        if (hasKey) {
            //从缓存中取出数据
            Project project = operations1.get(projectKeyId);
            //将Project转成DTO返回
            ProjectDTO projectDTO = new ProjectDTO();
            if (project == null)
                return null;
            else {
                BeanUtils.copyProperties(project, projectDTO);
                return projectDTO;
            }
        } else {
            //从数据库里查找然后更新缓存
            //调用DAO层方法查找project
            Project project = projectRepository.findByProjectId(id);
            //将Project转成DTO返回
            ProjectDTO projectDTO = new ProjectDTO();
            if (project == null)
                return null;
            else {
                //更新缓存
                String projectKeyName = "Project_name_" + project.getProjectName();
                operations1.set(projectKeyId, project, 3, TimeUnit.HOURS);    //过期时间3小时
                operations1.set(projectKeyName, project, 3, TimeUnit.HOURS);    //过期时间3小时
                BeanUtils.copyProperties(project,projectDTO);
                return projectDTO;
            }
        }

    }

    @Override
    public ProjectDTO findOneByName(String name) {
        /**
         * 先在缓存中查找
         */
        //在缓存中找
        ValueOperations<String, Project> operations1 = redisTemplate.opsForValue();
        String projectKeyName = "Project_name_" + name;
        boolean hasKey = redisTemplate.hasKey(projectKeyName); //判断缓存中是否有该KEY
        if (hasKey) {
            //缓存中存在，直接返回。
            Project project = operations1.get(projectKeyName);
            ProjectDTO projectDTO = new ProjectDTO();
            BeanUtils.copyProperties(project,projectDTO);
            return projectDTO;
        } else {
            //缓存中不存在，查找数据库，更新缓存。
            //调用DAO层方法查找相应project
            Project project = projectRepository.findByProjectName(name);
            //判断查找结果是否为空
            //根据判断结果返回相应的DTO
            if (project == null)
                return null;
            else {
                ProjectDTO projectDTO = new ProjectDTO();
                BeanUtils.copyProperties(project,projectDTO);
                operations1.set(projectKeyName,project,3,TimeUnit.HOURS);
                return projectDTO;
            }
        }
    }

    @Override
    @Transactional
    public ProjectDTO updateProjectUpdateTime(String projectId) {
        /**
         * 策略：
         * 1. 查询缓存中是否存在该ID
         * 2. 存在则修改缓存中的时间、同时修改数据库
         * 3. 不存在
         * */
        //查询该项目ID
        Project project = projectRepository.findByProjectId(projectId);
        //如果查询到不为空则,清空修改时间然后重新save一次
        if(project != null) {
            project.setProjectModificationTime(null);
            Project result = projectRepository.save(project);
            ProjectDTO projectDTO = new ProjectDTO();
            BeanUtils.copyProperties(result,projectDTO);
            return projectDTO;
        }
        else
            return null;
    }

    @Override
    public List<ProjectDTO> findAllPorjects() {
        //调用DAO层查询所有的project
        List<Project> list = projectRepository.findAll();
        ArrayList<ProjectDTO> projectDTOS = new ArrayList<>();

        if (list.size() == 0){
            return null;
        }
        for (Project project: list){
            ProjectDTO projectDTO = new ProjectDTO();
            BeanUtils.copyProperties(project,projectDTO);
            projectDTOS.add(projectDTO);
        }
        return projectDTOS;
    }

    @Override
    @Transactional
    public Boolean deleteProject(String id) {
        Integer result = projectRepository.deleteProjectByProjectId(id);

        if (result !=0)
            return true;
        else
            return false;
    }
}
