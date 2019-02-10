package com.optima.nisp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.optima.nisp.constanta.CommonCons;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.FIELD})
public @interface NISPEntity {
	
	String dbSchema() default CommonCons.DB_SCHEMA;
	String tableName() default "";
	String columnName() default "";
	boolean create() default false;
	boolean update() default false;
}
