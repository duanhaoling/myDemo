package com.example.mydemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retention说标明了注解被生命周期，对应RetentionPolicy的枚举，表示注解在何时生效：
 * SOURCE：只在源码中有效，编译时抛弃，如上面的@Override。
 * CLASS：编译class文件时生效。
 * RUNTIME：运行时才生效。
 * Created by ldh on 2017/9/11.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.PACKAGE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Nullable {
}
