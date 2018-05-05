package com.twis.common.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;

/**
 * SQL参数.
 * 
 * @author yxm
 */
public class StatementParameter {

	private List<Object> list = new ArrayList<Object>();

	public void setDate(Date value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value);
	}

	public void setString(String value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value);
	}

	public void setBool(Boolean value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value ? 1 : 0);
	}

	public void setInt(Integer value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value);
	}

	public void setLong(Long value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value);
	}

	public void setDouble(Double value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value);
	}

	public void setFloat(Float value) {
		if (value == null) {
			throw new RuntimeException("参数值[" + list.size() + "]不能为空.");
		}
		list.add(value);
	}

	public Object[] getArgs() {
		return list.toArray();
	}

	public Date getDate(int index) {
		Object value = this.getObject(index);
		return (Date) value;
	}

	public String getString(int index) {
		Object value = this.getObject(index);
		return (String) value;
	}

	public int getInt(int index) {
		Object value = this.getObject(index);
		return (Integer) value;
	}

	public float getFloat(int index) {
		Object value = this.getObject(index);
		return (Float) value;
	}

	public long getLong(int index) {
		Object value = this.getObject(index);
		return (Long) value;
	}

	public double getDouble(int index) {
		Object value = this.getObject(index);
		return (Double) value;
	}

	public Object getObject(int index) {
		return list.get(index);
	}

	public int getTypes(int index) {
		Object value = list.get(index);
		return this.getTypes(value);
	}

	public void setObject(Object value) throws SQLException {

		if (value instanceof String) {
			list.add((String) value);
		} else if (value instanceof Date) {
			list.add(getTime((Date) value));
		} else if (value instanceof Integer) {
			list.add((Integer) value);
		} else if (value instanceof Long) {
			list.add((Long) value);
		} else if (value instanceof Double) {
			list.add((Double) value);
		} else if (value instanceof Float) {
			list.add((Float) value);
		} else {
			throw new RuntimeException("未知参数类型[" + value + "].");
		}
	}
	
	private int getTypes(Object value) {
		if (value instanceof String) {
			return Types.VARCHAR;
		} else if (value instanceof Date) {
			return Types.DATE;
		} else if (value instanceof Integer) {
			return Types.INTEGER;
		} else if (value instanceof Long) {
			return Types.BIGINT;
		} else if (value instanceof Double) {
			return Types.DOUBLE;
		} else if (value instanceof Float) {
			return Types.FLOAT;
		} else {
			throw new RuntimeException("未知参数类型[" + value + "].");
		}
	}

	public int[] getArgTypes() {
		// { Types.CHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR };
		int[] valuesTypes = new int[list.size()];
		int i = 0;
		for (Object value : list) {
			valuesTypes[i] = this.getTypes(value);
			i++;
		}
		return valuesTypes;
	}

	public int size() {
		return list.size();
	}

	public PreparedStatementSetter getParameters() {
		if (list.size() == 0) {
			return null;
		}
		PreparedStatementSetter param = new PreparedStatementSetter() {
			public void setValues(PreparedStatement pstmt) {
				try {
					this.setValues2(pstmt);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}

			public void setValues2(PreparedStatement pstmt) throws SQLException {
				int i = 1;
				for (Object value : list) {
					if (value instanceof String) {
						pstmt.setString(i, (String) value);
					} else if (value instanceof Date) {
						pstmt.setString(i, getTime((Date) value));
					} else if (value instanceof Integer) {
						pstmt.setInt(i, (Integer) value);
					} else if (value instanceof Long) {
						pstmt.setLong(i, (Long) value);
					} else if (value instanceof Double) {
						pstmt.setDouble(i, (Double) value);
					} else if (value instanceof Float) {
						pstmt.setFloat(i, (Float) value);
					} else {
						throw new RuntimeException("未知参数类型[" + i + "." + value + "].");
					}
					i++;
				}
			}
		};
		return param;
	}

	private String getTime(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
}
