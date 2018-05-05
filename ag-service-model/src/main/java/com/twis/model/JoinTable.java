package com.twis.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})   
@Retention(RetentionPolicy.RUNTIME)    
@Documented
public @interface JoinTable {
	
	public String prefix() default "";
	
	/**当前关联表字段 （table Master  join table Exterior on Master.Column = Exterior.Column）*/
	public String joinMasterColumn() default "";
	
	/**需要关联表关联字段 （table Master  join table Exterior on Master.Column = Exterior.Column）*/
	public String joinExteriorColumn() default "";
	
	/**扩展的and条件*/
	public String joinExteriorWhere() default "";
}
