package com.bookstore.common.util;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class StringToListHandler implements TypeHandler<List<String>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String str = "";
        for (int i1 = 0; i1 < parameter.size(); i1++) {
            str+=parameter.get(i1);
            if(i1!=parameter.size()-1){
                str+="-";
            }
        }
        ps.setString(i,str);
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        return Arrays.asList(str.split("-"));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        return Arrays.asList(str.split("-"));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
