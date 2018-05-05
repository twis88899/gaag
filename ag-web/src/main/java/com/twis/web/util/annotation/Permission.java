package com.twis.web.util.annotation;


import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
	/** 
     * 功能ID，该功能ID，对应数据库中的功能ID 
     * @return 
     * @version V1.0.0 
     * @date Jan 13, 2014 4:59:35 PM 
     */  
    String value();  
}
