package cn.explink.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.explink.enumutil.FlowOrderTypeEnum;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderFlowOperation {
	FlowOrderTypeEnum value();
}
