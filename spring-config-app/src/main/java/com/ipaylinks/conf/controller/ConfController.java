package com.ipaylinks.conf.controller;

import com.ipaylinks.conf.model.*;
import com.ipaylinks.conf.service.ProjectManagerService;
import com.ipaylinks.conf.service.ProjectManagerServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
public class ConfController {

    @Autowired
    private ProjectManagerService projectManagerService;
    @RequestMapping("/login.htm")
    public String login(){
        return "login";
    }

    @RequestMapping("/index.htm")
    public String index(ModelMap modelMap,
                        @RequestParam(value = "appName",required = false)String appName){
        List<Project> projects = projectManagerService.getAllProjects();
        modelMap.put("projects", projects);
        String selectAppName = appName;
        if(Strings.isBlank(appName)){
            selectAppName = "common";
        }
        StringBuilder stringBuilder = new StringBuilder(selectAppName);
        Optional<Project> projectOptional =
                projects.stream()
                        .filter(project -> project.getSpringApplicationName().equals(stringBuilder.toString())).findFirst();
        if(projectOptional!= null && projectOptional.isPresent()){
            modelMap.put("selectProject", projectOptional.get());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelMap.put("name", auth.getName());
        modelMap.put("selectAppName", selectAppName);
        return "index";
    }

    @RequestMapping("/createProject.json")
    @ResponseBody
    public OperatorResponse createProject(Project project){
        log.info("创建项目请求参数[{}]",project);
        OperatorResponse operatorResponse = new OperatorResponse();
        String appName = project.getSpringApplicationName();
        if(Strings.isNotBlank(appName) && appName.indexOf("/") == -1){
            String path = ProjectManagerServiceImpl.ROOTPATH + "/" + appName;
            project.setPath(path);
            operatorResponse.setSuccess(projectManagerService.createProject(project));
        }
        log.info("创建项目响应参数[{}]",operatorResponse);
        return operatorResponse;
    }

    @RequestMapping("/showProperties.json")
    @ResponseBody
    public DataTablesResponse showProperties(@RequestParam("path")String projectPath,
                                    DataTablesRequest dataTablesRequest,
                                    @RequestParam(value = "search[value]",required = false)String searchValue){
        log.info("在项目下[{}]查询参数为[{}]搜索参数为[{}]",projectPath,dataTablesRequest,searchValue);
        DataTablesResponse dataTablesResponse =  new DataTablesResponse();
        int total = projectManagerService.getCount(projectPath);
        dataTablesResponse.setData(projectManagerService.getProjectByCondition(projectPath,dataTablesRequest,searchValue));
        if(Strings.isNotBlank(searchValue) && null != dataTablesResponse.getData()){
            dataTablesResponse.setRecordsFiltered(dataTablesResponse.getData().size());
        }else{
            dataTablesResponse.setRecordsFiltered(total);
        }
        dataTablesResponse.setRecordsTotal(total);
        log.info("查询结果为[{}]",dataTablesResponse);
        return dataTablesResponse;
    }

    @RequestMapping("/addProperty.json")
    @ResponseBody
    public OperatorResponse addProperty(@RequestParam("path") String path, Prop prop){
        log.info("创建属性请求项目路径[{}],属性参数[{}]",path,prop);
        OperatorResponse operatorResponse = new OperatorResponse();
        operatorResponse.setSuccess(projectManagerService.addProp(path,prop));
        log.info("创建属性响应报文[{}]",operatorResponse);
        return operatorResponse;
    }

    @RequestMapping("/editProperty.json")
    @ResponseBody
    public OperatorResponse editProperty(@RequestParam("path") String path, Prop prop){
        log.info("更改属性请求项目路径[{}],属性参数[{}]",path,prop);
        OperatorResponse operatorResponse = new OperatorResponse();
        operatorResponse.setSuccess(projectManagerService.editProp(path,prop));
        log.info("更改属性响应报文[{}]",operatorResponse);
        return operatorResponse;
    }

    @RequestMapping("/deleteProperty.json")
    @ResponseBody
    public OperatorResponse deleteProperty(@RequestParam("path") String path, Prop prop){
        log.info("删除属性请求项目路径[{}],属性参数[{}]",path,prop);
        OperatorResponse operatorResponse = new OperatorResponse();
        operatorResponse.setSuccess(projectManagerService.deleteProp(path,prop.getName()));
        log.info("删除属性响应报文[{}]",operatorResponse);
        return operatorResponse;
    }
}
