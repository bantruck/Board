package com.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Around("execution(* comboard..controller.*Controller.*(..)) or execution(* com.board..service.*Impl.*(..)) or execution(* com.board..mapper.*Mapper.*(..))")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
		
		String type = "";
		StringBuilder sb = new StringBuilder();
		String name = joinPoint.getSignature().getDeclaringTypeName();
		
		if(name.contains("Controller") == true) {
			type = "Controller =====>";
		} else if (name.contains("Service") == true) {
			type = "Service =====>";
		} else if (name.contains("Mapper") == true) {
			type = "Mapper =====>";
		}
		
		sb.append(type);
		sb.append(name);
		sb.append(".");
		sb.append(joinPoint.getSignature().getName());
		sb.append("()");
		
		logger.debug(sb.toString());
		return joinPoint.proceed();
	}
}
