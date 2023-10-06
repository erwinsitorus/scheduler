package com.tech.scheduler.configuration;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
@Controller
public @interface LogInterceptor {
	@AliasFor(annotation = RequestMapping.class, attribute = "path")
	String[] value() default {};
}
