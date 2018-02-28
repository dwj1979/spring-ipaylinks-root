package com.ipaylinks.conf.service;

import com.ipaylinks.conf.model.DataTablesRequest;
import com.ipaylinks.conf.model.Project;
import com.ipaylinks.conf.model.Prop;

import java.util.List;

public interface ProjectManagerService {

    List<Project> getAllProjects();

    boolean createProject(Project project);

    List<Prop> getProjectByCondition(String path,DataTablesRequest request, String searchValue);

    boolean deleteProp(String path, String propName);

    boolean editProp(String path,Prop prop);

    boolean addProp(String path, Prop prop);

    int getCount(String path);
}
