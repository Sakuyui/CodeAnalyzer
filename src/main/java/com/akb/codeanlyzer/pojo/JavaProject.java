package com.akb.codeanlyzer.pojo;

import java.util.List;

public class JavaProject {
    private String projectName;
    private List<String> filesName;

    public List<String> getFilesName() {
        return filesName;
    }

    public void setFilesName(List<String> filesName) {
        this.filesName = filesName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
