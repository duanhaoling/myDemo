package com.example.mydemo.annotation;

import com.google.auto.service.AutoService;

import org.reactivestreams.Processor;

/**
 * @AutoService(Processor.class)，谷歌提供的自动注册注解，为你生成注册Processor所需要的格式文件（com.google.auto相关包）。
 * init(ProcessingEnvironment env)，初始化处理器，一般在这里获取我们需要的工具类。
 * getSupportedAnnotationTypes()，指定注解处理器是注册给哪个注解的，返回指定支持的注解类集合。
 * getSupportedSourceVersion() ，指定java版本。
 * process()，处理器实际处理逻辑入口。
 * Created by ldh on 2017/9/11.
 */
@AutoService(Processor.class)
public class BindViewDemoProcessor{

}
