package cn.wxxwwx98.mytestapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBService {

    String driver = "com.mysql.jdbc.Driver" ;// 驱动名称
    String url = "jdbc:mysql://rm-uf6k5d9yj8dt4h0z3lo.mysql.rds.aliyuncs.com:3306/test" ;// 连接字符串
    String username = "root" ;// 用户名
    String password = "Wx1262148122" ;// 密码

    Connection con = null ;// 连接对象
    PreparedStatement pstmt = null ;// 语句对象
    ResultSet rs = null ;// 结果集对象

    /**
     * 获得连接对象
     *
     * @return 连接对象
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection() throws ClassNotFoundException,SQLException {
        Class.forName(driver);
        con = DriverManager.getConnection( url , username , password );
        return con ;
    }


    public void close(ResultSet rs, PreparedStatement pstmt, Connection con) {

        try {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (con != null)
                con.close();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 执行更新
     *
     * @param sql
     *            传入的预设的 sql语句
     * @param params
     *            问号参数列表
     * @return 影响行数
     */
    public int execUpdate(String sql, Object[] params) {
        try {
            this .getConnection();// 获得连接对象
            this .pstmt = this. con .prepareStatement(sql);// 获得预设语句对象

            if (params != null) {
                // 设置参数列表
                for (int i = 0; i < params. length; i++) {
                    // 因为问号参数的索引是从1开始，所以是i+1，将所有值都转为字符串形式，好让setObject成功运行
                    this .pstmt .setObject(i + 1, params[i] + "" );
                }
            }

            return this .pstmt .executeUpdate(); // 执行更新，并返回影响行数

        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this .close( this. rs, this. pstmt , this .con );
        }
        return 0;
    }

    /**
     * 执行查询
     *
     * @param sql
     *            传入的预设的 sql语句
     * @param params
     *            问号参数列表
     * @return 查询后的结果
     */
    public List<Map<String, Object>> execQuery(String sql, Object[] params) {

        try {
            this .getConnection();// 获得连接对象
            this .pstmt = this. con .prepareStatement(sql);// 获得预设语句对象

            if (params != null) {
                // 设置参数列表
                for (int i = 0; i < params. length; i++) {
                    // 因为问号参数的索引是从1开始，所以是i+1，将所有值都转为字符串形式，好让setObject成功运行
                    this .pstmt .setObject(i + 1, params[i] + "" );
                }
            }

            // 执行查询
            ResultSet rs = pstmt .executeQuery();

            List<Map<String, Object>> al = new ArrayList<Map<String, Object>>();

            // 获得结果集元数据（元数据就是描述数据的数据，比如把表的列类型列名等作为数据）
            ResultSetMetaData rsmd = rs.getMetaData();

            // 获得列的总数
            int columnCount = rsmd.getColumnCount();

            // 遍历结果集
            while (rs.next()) {
                Map<String, Object> hm = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    // 根据列索引取得每一列的列名,索引从1开始
                    String columnName = rsmd.getColumnName(i + 1);
                    // 根据列名获得列值
                    Object columnValue = rs.getObject(columnName);
                    // 将列名作为key，列值作为值，放入 hm中，每个 hm相当于一条记录
                    hm.put(columnName, columnValue);
                }
                // 将每个 hm添加到al中, al相当于是整个表，每个 hm是里面的一条记录
                al.add(hm);
            }

            return al;

        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            this .close( this. rs, this. pstmt , this .con );
        }
        return null ;
    }
}