package sql;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: youxingyang
 * @date: 2018/5/17 9:27
 */
public class SQLCommand {
    private String sql;//SQL命令

    private List<SQLParameter> params = new ArrayList<SQLParameter>();//参数列表

    public SQLCommand(){}

    public SQLCommand(String sql, List<SQLParameter> params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<SQLParameter> getParams() {
        return params;
    }

    public void setParams(List<SQLParameter> params) {
        this.params = params;
    }
}
