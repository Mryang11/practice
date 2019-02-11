package sql;

/**
 * @Author: youxingyang
 * @date: 2018/5/17 9:27
 */
public class SQLParameter {

    private int index;//参数索引

    private String columnName;//参数名称

    private Object value;//参数值

    public SQLParameter(){}

    public SQLParameter(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }

    public SQLParameter(int index, Object value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

