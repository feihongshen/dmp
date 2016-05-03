package com.pjbest.splitting.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.pjbest.splitting.routing.DatabaseType;

/**
 * 
 * @author pengfei.wan
 * @date 2016-03-15
 * @用途 ： 设置类或方法使用的数据源，如果不设置默认使用主库(DatabaseType.MASTER); 优先级顺序 ： 方法注解 > 类注解 。
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataSource {

	DatabaseType value() default DatabaseType.MASTER;

}
