package com.readdb.app.mybatis;

public class JavaType {

    private String javaType;
    private String javaImport;

    public JavaType(String javaType, String javaImport) {
        this.javaType = javaType;
        this.javaImport = javaImport;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaImport() {
        return javaImport;
    }

    public void setJavaImport(String javaImport) {
        this.javaImport = javaImport;
    }
}
