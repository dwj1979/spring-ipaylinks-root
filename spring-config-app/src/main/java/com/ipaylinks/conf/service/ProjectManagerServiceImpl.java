package com.ipaylinks.conf.service;

import com.google.gson.Gson;
import com.ipaylinks.conf.model.DataTablesRequest;
import com.ipaylinks.conf.model.Project;
import com.ipaylinks.conf.model.Prop;
import lombok.extern.log4j.Log4j2;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.util.Strings;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    private CuratorFramework curatorClient;

    public static final String ROOTPATH ="/ipaylinks/env";

    private static final String ROOTCOMMONPATH ="/ipaylinks/env/common";

    //thread safe
    private Gson gson = new Gson();

    @Override
    public List<Project> getAllProjects() {
        try{
            Stat stat = curatorClient.checkExists().forPath(ROOTPATH);
            if(stat != null){
                List<Project> all = curatorClient.getChildren().forPath(ROOTPATH).stream().map(string->{
                    String path = ROOTPATH + "/" + string;
                    try{
                        byte[] bytes = curatorClient.getData().forPath(path);
                        Project project = gson.fromJson(new String(bytes,"utf-8"),Project.class);
                        return project;
                    }catch (Exception e){
                        log.error("得到项目[" + path + "]属性出错",e);
                    }
                    return null;
                }).collect(Collectors.toList());
                return all;
            }
        }catch (Exception e){
            log.error("查询所有项目出错",e);
        }
        return null;
    }

    @Override
    public boolean createProject(Project project) {
        if(null != project && Strings.isNotBlank(project.getPath())){
            String path = project.getPath();
            try{
                Stat stat = curatorClient.checkExists().forPath(path);
                if(stat == null){
                    String jsonstr = gson.toJson(project);
                    curatorClient.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath(path,jsonstr.getBytes("utf-8"));
                    return true;
                }
            }catch (Exception e){
                log.error("创建项目[" + path + "]出错",e);
            }

        }
        return false;
    }

    @Override
    public List<Prop> getProjectByCondition(String path, DataTablesRequest request, String searchValue) {
        try{
            List<Prop> props = curatorClient.getChildren().forPath(path).stream().filter(string->{
                if(Strings.isNotBlank(searchValue)){
                    return string.indexOf(searchValue) != -1;
                }else{
                    return true;
                }
            }).map(string->{
                String nodePath = path + "/" + string;
                try {
                    byte[] bytes = curatorClient.getData().forPath(nodePath);
                    Prop prop = gson.fromJson(new String(bytes, "utf-8"),Prop.class);
                    return prop;
                } catch (Exception e) {
                    log.error("得到节点[" + nodePath + "]错误", e);
                }
                return null;
            }).collect(Collectors.toList());

            if(props != null && props.size() > 0){
                int pageSize = 20;
                int start = 0;
                if(request != null){
                    pageSize = request.getLength();
                    start = request.getStart();
                }
                if(pageSize != -1){
                    List<Prop> props1 = new ArrayList<>();
                    for (int i = 0; i < props.size(); i++) {
                        if(i>= start && i < start + pageSize){
                            props1.add(props.get(i));
                        }
                    }
                    return props1;
                }else{
                    return props;
                }
            }

        }catch (Exception e){
            log.error("查询节点["+path + "]出错",e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteProp(String path, String propName) {
        if(Strings.isNotBlank(path) && Strings.isNotBlank(propName)){
            String nodePath = path + "/" + propName;
            try{
                Stat stat = curatorClient.checkExists().forPath(nodePath);
                if(stat != null){
                    curatorClient.delete()
                            .forPath(nodePath);
                    return true;
                }
            }catch (Exception e){
                log.error("删除节点[" + nodePath + "]出错",e);
            }

        }
        return false;
    }

    @Override
    public boolean editProp(String path,Prop prop) {
        if(Strings.isNotBlank(path) && null != prop && Strings.isNotBlank(prop.getName())){
            String nodePath = path + "/" + prop.getName();
            try{
                Stat stat = curatorClient.checkExists().forPath(nodePath);
                if(stat != null){
                    String jsonstr = gson.toJson(prop);
                    curatorClient.setData()
                            .forPath(nodePath,jsonstr.getBytes("utf-8"));
                    return true;
                }
            }catch (Exception e){
                log.error("更新节点[" + nodePath + "]出错",e);
            }

        }
        return false;
    }

    public boolean addProp(String path, Prop prop){
        if(Strings.isNotBlank(path) && null != prop && Strings.isNotBlank(prop.getName())){
            String nodePath = path + "/" + prop.getName();
            try{
                Stat stat = curatorClient.checkExists().forPath(nodePath);
                if(stat == null){
                    String jsonstr = gson.toJson(prop);
                    curatorClient.create()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath(nodePath,jsonstr.getBytes("utf-8"));
                    return true;
                }
            }catch (Exception e){
                log.error("创建节点[" + nodePath + "]出错",e);
            }

        }
        return false;
    }

    @Override
    public int getCount(String path) {
        try {
            if(Strings.isNotBlank(path)){
                Stat stat = curatorClient.checkExists().forPath(path);
                if(stat != null){
                    List<String> properties = curatorClient.getChildren().forPath(path);
                    if(properties != null){
                        return properties.size();
                    }
                }
            }
        }catch (Exception e){
            log.error("得到总数出错");
        }
        return 0;
    }

    @PostConstruct
    public void init(){
        if(!StringUtils.isEmpty(zookeeperAddress)){
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            curatorClient = CuratorFrameworkFactory.builder()
                    .connectString(zookeeperAddress)
                    .retryPolicy(retryPolicy)
                    .sessionTimeoutMs(6000)
                    .connectionTimeoutMs(3000)
                    .build();
            curatorClient.start();
            try{
                Stat stat = curatorClient.checkExists().forPath(ROOTCOMMONPATH);
                if(stat == null){
                    Project project = new Project();
                    project.setPath(ROOTCOMMONPATH);
                    project.setName("公共配置");
                    project.setSpringApplicationName("common");
                    project.setDesc("所有项目公共的配置,在spring项目的配置顺序是先读取system->application.yml->zk项目根节点下的配置->公共配置");
                    this.createProject(project);
                }
            }catch (Exception e){
                log.error(e);
            }
        }else{
            log.error("zookeeper地址不能为空");
        }
    }

    @PreDestroy
    public void destroy(){
        if(curatorClient != null){
            curatorClient.close();
        }
    }
}
