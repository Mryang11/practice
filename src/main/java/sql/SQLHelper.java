package sql;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @Author: youxingyang
 * @date: 2018/5/17 9:26
 */
public class SQLHelper {
    private static String url;//连接地址

    private static String username;//用户

    private static String password;//密码

    private static String driverName;//驱动名称

    static {
        String filename = "/jdbc.properties";
        Properties props = new Properties();
        try {
            props.load(SQLHelper.class.getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverName = props.getProperty("driverClassName");
        url = props.getProperty("url");
        username = props.getProperty("username");
        password = props.getProperty("password");
    }

    public SQLHelper(){
        String filename = "/jdbc.properties";
        Properties props = new Properties();
        try {
            props.load(SQLHelper.class.getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverName = props.getProperty("driverClassName");
        url = props.getProperty("url");
        username = props.getProperty("username");
        password = props.getProperty("password");
    }

    public SQLHelper(Map<String,String> properties){
        String filename = "/jdbc.properties";
        Properties props = new Properties();
        try {
            props.load(SQLHelper.class.getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driverName = props.getProperty(properties.get("driverClassName"));
        url = props.getProperty(properties.get("url"));
        username = props.getProperty(properties.get("username"));
        password = props.getProperty(properties.get("password"));
    }

    /**
     * 批量操作
     * @param list
     */
    public void execute(LinkedList<SQLCommand> list){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            for(SQLCommand cmd : list){
                String sql = cmd.getSql();
                System.out.println("SQL:" + sql);
                preparedStatement = connection.prepareStatement(sql);
                if(cmd.getParams() != null && cmd.getParams().size() > 0){
                    for(int i=0;i<cmd.getParams().size();i++){
                        String value = cmd.getParams().get(i).getValue().toString();
                        int index = cmd.getParams().get(i).getIndex();
                        preparedStatement.setObject(index,value);
                    }
                }
                preparedStatement.execute();
            }
            connection.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                if(connection != null){
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }finally{
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行修改操作(insert、update、delete)
     * @param sql
     * @return
     */
    public static boolean execute(String sql){
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            return statement.execute(sql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 执行查询操作
     * @param sql
     * @return
     */
    public static List<Object[]> executeQuery(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData cmd = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            List<Object[]> list = new ArrayList<Object[]>();
            cmd = resultSet.getMetaData();
            int columnCount = cmd.getColumnCount();//结果集总列数
            while(resultSet.next()){
                Object[] row = new Object[columnCount];
                for(int columnIndex=0;columnIndex<columnCount;columnIndex++){
                    row[columnIndex] = resultSet.getObject(columnIndex+1);
                }
                list.add(row);
            }
            return list;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(resultSet != null){
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /**
     * 执行查询操作
     * @param sql
     * @return
     */
    public List<Object[]> executeQueryServlet(String sql) throws Exception{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData cmd = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            List<Object[]> list = new ArrayList<Object[]>();
            cmd = resultSet.getMetaData();
            int columnCount = cmd.getColumnCount();//结果集总列数
            while(resultSet.next()){
                Object[] row = new Object[columnCount];
                for(int columnIndex=0;columnIndex<columnCount;columnIndex++){
                    row[columnIndex] = resultSet.getObject(columnIndex+1);
                }
                list.add(row);
            }
            return list;
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new Exception(e);
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new Exception(e);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new Exception(e);
        }finally{
            try {
                if(resultSet != null){
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                //e.printStackTrace();
                throw new Exception(e);
            }
        }
    }
}

