package com.readdb.app.mybatis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.readdb.app.mybatis.Helper.*;
import static com.readdb.app.mybatis.MybatisConfig.*;

public class MybatisGenerator {

    private static Scanner sc = new Scanner(System.in);
    private static String schema = "";
    private static String WORKING_DIRECTORY = System.getProperty("user.dir") + WORKING_FOLDER;

    public static void main(String[] args) {
//        makeDir();
        program();
    }

    public static void program() {
        try {
            Connection conn = getConnection(DB_URL, USER_NAME, PASSWORD);

            // get DatabaseMetaData object
            DatabaseMetaData dbmd = conn.getMetaData();

            System.out.println("Connect to [" + schema + "] successfully!");
            System.out.println("+++ PLEASE CHOOSE FUNC +++");
            System.out.println("1. Show info DB");
            System.out.println("2. Generate table");
            System.out.println("0. Exit");

            String input = "";
            while (input.trim().isEmpty()) {
                System.out.println("Your choose: ");
                input = sc.nextLine();
                if(!input.trim().isEmpty()) {
                    try {
                        int chooseFunc = Integer.parseInt(input);
                        switch (chooseFunc) {
                            case 1:
                                System.out.println("SHOW INFO DB");
                                showInfoDB(dbmd);
                                break;
                            case 2:
                                System.out.println("GENERATE TABLE");
                                generateTable(dbmd);
                                break;
                            case 0:
                                System.out.println("=> Exit!");
                                return;
                            default:
                                System.err.println("[ERROR]: Your choose is a number [0, 1, 2]!");
                                input = "";
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("[ERROR]: Your choose is a number [0, 1, 2]!");
                        input = "";
                    }
                } else {
                    System.err.println("[ERROR]: Your choose is valid!");
                }
            }
            conn.close();
        } catch (Exception ex) {
            System.err.println("[ERROR]: Connect to [" + schema + "] failure!");
            ex.printStackTrace();
        }
    }

    public static Connection getConnection(String urlDB, String userName, String password) {
        Connection conn = null;
        while(schema.trim().isEmpty() || conn == null) {
            System.out.println("Please enter schema: ");
            schema = sc.nextLine();
            if(!schema.trim().isEmpty()) {
                try {
                    // connect to database
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection(urlDB + "/" + schema, userName, password);
                    // get DatabaseMetaData object
                } catch (SQLException e) {
                    System.err.println("[ERROR]: " + e.getMessage());
                    conn = null;
                } catch (ClassNotFoundException e) {
                    System.err.println("[ERROR]: " + e.getMessage());
                    conn = null;
                }
            } else {
                System.err.println("[ERROR]: Scheme is valid!");
            }
        }
        return conn;
    }

    public static void showInfoDB(DatabaseMetaData dbmd) throws SQLException {
        System.out.println("Driver Name: " + dbmd.getDriverName());
        System.out.println("Driver Version: " + dbmd.getDriverVersion());
        System.out.println("UserName: " + dbmd.getUserName());
        System.out.println("Database Product Name: " + dbmd.getDatabaseProductName());
        System.out.println("Database Product Version: " + dbmd.getDatabaseProductVersion());
    }

    public static void generateTable(DatabaseMetaData dbmd) throws SQLException {
        Table tableInfo;
        Map<String, Field> fieldsInfo;
        Map<String, Table> tablesInfo = new HashMap<>();

        String table[] = { "TABLE" };
        ResultSet tables = dbmd.getTables(schema, null, null, table);

        // writeBaseDao
        writeFile(WORKING_DIRECTORY + "\\dao", "BaseDao.java", generateBaseDao(PACKAGE));

        // writePagination
        writeFile(WORKING_DIRECTORY + "\\util", "Pagination.java", generatePagination(PACKAGE));

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            System.out.println("Start generate table: " + tableName);

            ResultSet primaryKeys = dbmd.getPrimaryKeys(null, null, tableName);

            String primaryKeyColumnName = "";

            while(primaryKeys.next()){
                primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                String primaryKeyName = primaryKeys.getString("PK_NAME");
            }

            ResultSet columns = dbmd.getColumns(schema, null, tableName, null);

            Field field;
            fieldsInfo = new HashMap<>();
            while(columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnSize = columns.getString("COLUMN_SIZE");
                String datatypeName = columns.getString("TYPE_NAME");
                String datatype = columns.getString("DATA_TYPE");
                String isNullable = columns.getString("IS_NULLABLE");
                String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                Boolean isPrimaryKey = columnName.equals(primaryKeyColumnName);
                field = new Field(columnName, columnSize, datatypeName, datatype, isNullable, isAutoIncrement, isPrimaryKey);
//                System.out.println(field.toString());
                fieldsInfo.put(columnName, field);

            }

            tableInfo = new Table(tableName, primaryKeyColumnName, fieldsInfo);
//            System.out.println(tableInfo.toString());
//            System.out.println(tableInfo.generatePo(PACKAGE));
//            System.out.println(tableInfo.generateMapper(PACKAGE));
            // writePo
            writeFile(WORKING_DIRECTORY + "\\po", tableInfo.getClassName() + "Po.java", tableInfo.generatePo(PACKAGE));
            // wirteMapper.xml
            writeFile(WORKING_DIRECTORY + "\\mapper", tableInfo.getClassName() + "Mapper.xml", tableInfo.generateMapper(PACKAGE));
            // writeDao
            writeFile(WORKING_DIRECTORY + "\\dao", tableInfo.getClassName() + "Dao.java", tableInfo.generateDao(PACKAGE));

            tablesInfo.put(tableName, tableInfo);
        }
    }

}
