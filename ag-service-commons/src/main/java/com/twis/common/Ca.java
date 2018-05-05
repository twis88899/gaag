package com.twis.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)   
@Retention(RetentionPolicy.RUNTIME)    
@Documented
public @interface Ca {
	public String url() default "";
	public String logName();
	public String saveLog() default "true";
	public String saveResult() default "false";
}
