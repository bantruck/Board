package com.board.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {

	@Autowired
	private PlatformTransactionManager transactionManager;

	// 비즈니스 로직을 수행하는 모든 ServiceImpl 클래스의 모든 메서드
	private static final String EXPRESSION = "execution(* com.board..service.*Impl.*(..))";
	
	@SuppressWarnings("deprecation")
	@Bean
	public TransactionInterceptor transactionAdvice() {

		/**
		 * 트랜잭션에서 롤백을 수행하는 규칙(Rule)
		 *  RollbackRuleAttribute 생성자의 인자로 Exception 클래스를 지정, 자바에서 모든 예외는 Exception 클래스를 상속받기 때문에 어떠한 예외가 발생하던 무조건 롤백이 수행.
		 */
		List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName("*"); // 트랜잭션의 이름을 설정

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

		return new TransactionInterceptor(transactionManager, attributeSource);
	}
	
	@Bean
	public Advisor transactionAdvisor() {
		
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		// AOP의 포인트컷(pointcut)을 설정. EXPRESSION에 지정한 ServiceImpl 클래스의 모든 메서드를 대상.
		pointcut.setExpression(EXPRESSION);
		
		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
		
	}
}
