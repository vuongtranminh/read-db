package com.readdb.app.mybatis;

import static com.readdb.app.mybatis.Helper.*;
import static com.readdb.app.mybatis.MySQLDataType.LONGTEXT;

public class Field {
    private String columnName;
    private String columnNameWithCamel;
    private String columnNameWithClassName;
    private String javaType;
    private String javaImport;
    private String columnSize;
    private String datatypeName;
    private String datatype;
    private String isNullable;
    private String isAutoIncrement;

    private Boolean isPrimaryKey;

    public Field(String columnName, String columnSize, String datatypeName, String datatype, String isNullable, String isAutoIncrement, Boolean isPrimaryKey) {
        this.columnName = columnName;
        this.columnNameWithCamel = snakeToCamel(columnName);
        this.columnNameWithClassName = className(columnName);
        JavaType jT = convertJavaType(datatype);
        this.javaType = jT.getJavaType();
        this.javaImport = jT.getJavaImport();
        this.columnSize = columnSize;
        this.datatypeName = (LONGTEXT).equals(datatype) ? "LONGVARCHAR" : datatypeName;
        this.datatype = datatype;
        this.isNullable = isNullable;
        this.isAutoIncrement = isAutoIncrement;
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameWithCamel() {
        return columnNameWithCamel;
    }

    public void setColumnNameWithCamel(String columnNameWithCamel) {
        this.columnNameWithCamel = columnNameWithCamel;
    }

    public String getColumnNameWithClassName() {
        return columnNameWithClassName;
    }

    public void setColumnNameWithClassName(String columnNameWithClassName) {
        this.columnNameWithClassName = columnNameWithClassName;
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

    public String getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(String columnSize) {
        this.columnSize = columnSize;
    }

    public String getDatatypeName() {
        return datatypeName;
    }

    public void setDatatypeName(String datatypeName) {
        this.datatypeName = datatypeName;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(String isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" {");
        sb.append("Hash = ").append(hashCode());
        sb.append(", columnName = ").append(columnName);
        sb.append(", columnNameWithCamel = ").append(columnNameWithCamel);
        sb.append(", columnNameWithClassName = ").append(columnNameWithClassName);
        sb.append(", javaType = ").append(javaType);
        sb.append(", javaImport = ").append(javaImport);
        sb.append(", columnSize = ").append(columnSize);
        sb.append(", datatypeName = ").append(datatypeName);
        sb.append(", datatype = ").append(datatype);
        sb.append(", isNullable = ").append(isNullable);
        sb.append(", isAutoIncrement = ").append(isAutoIncrement);
        sb.append(", isPrimaryKey = ").append(isPrimaryKey);
        sb.append("}");
        return sb.toString();
    }

    public String generateImport() {
        return javaImport == null ? "" : "import " + javaImport + ";\n";
    }

    public String generateInstance() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tprivate " + javaType + " " + columnNameWithCamel + ";\n");
        return sb.toString();
    }

    public String generateAccessor() {
        StringBuilder sb = new StringBuilder();
        // getter
        sb.append("\tpublic " + javaType + " get" + columnNameWithClassName + "() {\n\t\treturn " + columnNameWithCamel + ";\n\t}\n");
        // setter
        if ("String".equals(javaType)) {
            sb.append("\tpublic void set" + columnNameWithClassName + "(String " + columnNameWithCamel
                    + ") {\n\t\tthis." + columnNameWithCamel + " = " + columnNameWithCamel + " == null ? null : "
                    + columnNameWithCamel + ".trim();\n\t}\n");
        } else {
            sb.append("\tpublic void set" + columnNameWithClassName + "(" + javaType + " " + columnNameWithCamel + ") {\n\t\tthis."
                    + columnNameWithCamel + " = " + columnNameWithCamel + ";\n\t}\n");
        }
        return sb.toString();
    }

    public String generateResult() {
        StringBuilder sb = new StringBuilder();
        if(isPrimaryKey) {
            sb.append("\t\t<id column=\"" + columnName + "\" jdbcType=\"" + datatypeName + "\" property=\"" + columnNameWithCamel + "\" />\n");
        } else {
            sb.append("\t\t<result column=\"" + columnName + "\" jdbcType=\"" + datatypeName + "\" property=\"" + columnNameWithCamel + "\" />\n");
        }
        return sb.toString();
    }

    public String generateColumnType() {
        StringBuilder sb = new StringBuilder();
        sb.append("#{").append(columnNameWithCamel).append(", ").append("jdbcType=").append(datatypeName).append("}");
        return sb.toString();
    }
}
