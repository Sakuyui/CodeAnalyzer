package com.akb.codeanlyzer.service;

import com.akb.codeanlyzer.pojo.JavaProject;
import com.akb.codeanlyzer.pojo.User;

import java.util.List;

public interface IProjectService {
    List<String> getIndividualProjects(String id);
    JavaProject getProjectEntity(String id, String pName, String oName);
}
