package com.ipaylinks.config.starter.initializer;

import com.google.common.base.Strings;
import com.ipaylinks.config.starter.utils.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public class IpayLinksInitializer implements ApplicationContextInitializer {
    private static final String ROOTPATH = "/ipaylinks/env";
    private static final String COMMONPATH = ROOTPATH + "/common" ;
    private static final String CENTERRESOURCE = "com.ipaylinks.center.resource";
    private static final String centerPATH = "file:/opt/env.properties";
    private Logger logger = LoggerFactory.getLogger(IpayLinksInitializer.class);
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        String appName = env.getProperty("spring.application.name");
        String appZKPath = ROOTPATH + "/" + appName;
        String centerResource = env.getProperty(CENTERRESOURCE);
        System.setProperty("spring.application.name",appName);
        if(Strings.isNullOrEmpty(centerResource)) {
            centerResource = centerPATH;
        }
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        PropertySourcesLoader propertySourcesLoader = new PropertySourcesLoader();
        if(!StringUtils.isEmpty(appName)){
            CuratorFramework client = null;
            try{
                Resource[] resources = resourcePatternResolver.getResources(centerResource);
                Arrays.stream(resources).forEach(resource -> {
                            try {
                                PropertySource propertySource = propertySourcesLoader.load( resource,"com.ipaylinks.center", null);
                                env.getPropertySources().addLast(propertySource);
                            }catch (Exception e){
                                logger.error("加载资源文件错误",e);
                            }
                        });
                String zookeeperAddress = env.getProperty("zookeeper.address");
                if(Strings.isNullOrEmpty(zookeeperAddress)){
                    logger.warn("zookeeper地址为空,项目[{}]启动参数加载不完全",appName);
                }else{
                    client = CuratorUtils.getClient(zookeeperAddress);
                    MapPropertySource appZKProperties = CuratorUtils.getFromPath(client,appZKPath);
                    if(appZKProperties != null) {
                        env.getPropertySources().addLast(appZKProperties);
                    }

                    MapPropertySource commonZKProperties = CuratorUtils.getFromPath(client,COMMONPATH);
                    if(commonZKProperties != null) {
                        env.getPropertySources().addLast(commonZKProperties);
                    }
                    logger.debug("初始化项目[{}]环境变量完毕",appName);
                }
            }catch (Exception e){
                logger.error("初始化项目["+ appName+ "]环境变量失败.",e);
            }/*finally {这里不能close，close会有问题
                if(client != null){
                    client.close();
                }
            }*/
        }else{
            logger.warn("请为在application.yml或者application.properties配置参数spring.application.name");
        }
    }
}
