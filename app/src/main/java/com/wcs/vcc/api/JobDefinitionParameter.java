package com.wcs.vcc.api;

/**
 * Created by tranxuanloc on 8/27/2016.
 */
public class JobDefinitionParameter {
    private String UserName;
    private String DepartmentCategoryID;

    public JobDefinitionParameter(String userName, String departmentCategoryID) {
        UserName = userName;
        DepartmentCategoryID = departmentCategoryID;
    }
}
