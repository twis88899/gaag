package com.twis.common.dao;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlUtil {
	public static String getSQL(String sql, StatementParameter param) {
		int i = 0;
		while (sql.indexOf('?') > -1) {
			if (param == null) {
				throw new RuntimeException("没有设置参数.");
			}
			if (i >= param.size()) {
				return sql;
			}
			String value;
			int type = param.getTypes(i);

			if (type == Types.VARCHAR) {
				value = "'" + param.getString(i) + "'";
			} else if (type == Types.DATE) {
				value = "'" + getTime(param.getDate(i)) + "'";
			} else if (type == Types.INTEGER) {
				value = Integer.toString(param.getInt(i));
			} else if (type == Types.FLOAT) {
				value = Float.toString(param.getFloat(i));
			} else if (type == Types.DOUBLE) {
				value = Double.toString(param.getDouble(i));
			} else if (type == Types.BIGINT) {
				value = Long.toString(param.getLong(i));
			} else {
				throw new RuntimeException("未知数据类型[" + type + "]");
			}

			sql = sql.substring(0, sql.indexOf('?')) + value + sql.substring(sql.indexOf('?') + 1, sql.length());
			i++;
		}
		return sql;//
	}

	private static String getTime(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}
}
