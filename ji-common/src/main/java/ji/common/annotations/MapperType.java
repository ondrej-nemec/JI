package ji.common.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface MapperType {

	String value();
	
	String key() default "";
	
	String dateTimeFormat() default "";
	
}
