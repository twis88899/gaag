package com.twis.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)   
@Retention(RetentionPolicy.RUNTIME)    
@Documented
public @interface Column {

	//数据库列名
	public String name();
	
	//是否是ID
	public String isId() default "false";
	
	//字段类型
//	public ColumnType type() default ColumnType.STRING;

	//错误代码
	public int errorCode() default 0;
	
	//错误信息
	public String errorMsg() default "";
	
	//正则表达式
	public String regex() default "";
	
	//是否必填
	public String required() default "false";
	
	//是否允许空值 true允许,false不能为空null或空字符串
	public boolean enableNull() default true; //add by ben
	
	//是否允许更新字段
	public boolean enableUpdate() default true; //add by ben
	
	/**
	 * @author xukai
	 * 是否按此字段分区
	 * */
	public boolean is_table_partitioning() default false;
}
