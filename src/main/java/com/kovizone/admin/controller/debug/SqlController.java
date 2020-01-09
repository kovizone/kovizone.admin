package com.kovizone.admin.controller.debug;

import com.kovizone.admin.vo.GeneralData;
import com.kovizone.admin.util.GeneralUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.*;

/**
 * 数据库控制台控制
 * <p/>
 * URL字典
 * <TR>
 * <TD>/sql/view.do</TD>
 * <TD>跳转到数据库控制台页</TD>
 * </TR>
 * <TR>
 * <TD>/sql/executeQuery.do</TD>
 * <TD>select查询</TD>
 * </TR>
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-16 KoviChen 新建类
 */
@Controller
@RequestMapping("/sql")
public class SqlController {

    private DataSource dataSource;

    @Autowired
    public SqlController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping("/view.do")
    public ModelAndView view() {
        return new ModelAndView("debug/sql");
    }

    @PostMapping("/executeQuery.do")
    @ResponseBody
    public GeneralData executeQuery(HttpServletRequest request) {
        String sql = request.getParameter("sql");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            JSONArray jsonArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject jsonObject = new JSONObject(16, true);
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    int index = i + 1;
                    String columnName = resultSetMetaData.getColumnLabel(index);
                    jsonObject.put(columnName, resultSet.getObject(index));
                }
                jsonArray.add(jsonObject);
            }
            GeneralData generalData = new GeneralData(true, "");
            generalData.setList(jsonArray);
            return generalData;
        } catch (SQLException e) {
            return new GeneralData(false, e.getMessage());
        } finally {
            GeneralUtils.close(resultSet, preparedStatement, connection);
        }
    }
}
