package com.board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@SuppressWarnings("unused")
@PropertySource("classpath:/application.properties")   // 인자로 지정된 "src/main/resources 디렉터리의 application.properties 파일을 DBConfiguration 클래스에서 사용할 설정 파일로 지정하겠다."라는 의미
@EnableTransactionManagement // 스프링에서 제공하는 애너테이션 기반 트랜잭션을 활성화
@Configuration  // 프링은 해당 애너테이션이 붙은 클래스를 자바 기반의 설정 파일
public class DBConfiguration {
	
	@Autowired  // 빈(Bean)으로 등록된 인스턴스(이하 객체)를 클래스에 주입
	private ApplicationContext applicationContext; // 컨테이너는 빈(Bean)의 생성과 사용, 관계, 생명주기 등을 관리
	
	@Bean   //컨테이너에 의해 관리되는 빈(Bean)으로 등록
	@ConfigurationProperties(prefix="spring.datasource.hikari")  //pplication.properties 파일에서 spring.datasource.hikari로 시작하는 설정들을 모두 읽어 들여서 해당 메서드에 바인딩한다
	public HikariConfig hikariConfig() {
		return new HikariConfig(); // 히카리CP 객체를 생성합니다. 히카리CP는 커넥션 풀(Connection Pool) 라이브러리 중 하나
	}
	
	@Bean
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig()); // 커넥션 풀은 커넥션 객체를 생성해놓고 데이터베이스에 접근하는 사용자에게 미리 생성되어 있던 커넥션을 제공하고 다시 돌려받는 방법
		
	}
	
	/**
	 * 	SqlSessionFactory 객체를 생성
	 * 	SqlSessionFactory는 데이터베이스의 커넥션과 SQL 실행에 대한 모든 것을 갖는 정말 중요한 역할
	 * 	SqlSessionFactoryBean은 마이바티스(MyBatis)와 스프링의 연동 모듈로 사용, MyBatis XML 파일의 위치, MyBatis XML 설정 파일 위치 등을 지정하고, 
	 * 	SqlSessionFactoryBean 자체가 아닌 getObject() 메서드를 호출한 결과에 해당하는 SqlSessionFactory를 생성하기 위해 사용
	 * @return SqlSessionFactory
	 * @throws Exception
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		// XML Mapper 파일들을 인식
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:mappers/**/*.xml"));
		// 해당 메서드를 사용해서 패키지 경로를 제외하여 호출 가능
		factoryBean.setTypeAliasesPackage("com.board.domain");
		factoryBean.setConfiguration(mybatisConfg());
		return factoryBean.getObject();
	}
	
	/**
	 * SqlSessionTemplate은 마이바티스 스프링 연동 모듈의 핵심이다. 
	 * SqlSessionTemplate은 SqlSession을 구현하고 코드에서 SqlSession을 대체하는 역할을 한다. 
	 * SqlSessionTemplate은 쓰레드에 안전하고 여러 개의 DAO나 매퍼에서 공유할 수 있다.
	 *  필요한 시점에 세션을 닫고, 커밋 또는 롤백하는 것을 포함한 세션의 생명주기를 관리한다
	 * @return SqlSessionTemplate
	 * @throws Exception
	 */
	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory());
	}
	
	/**
	 * application.properties에서 mybatis.configuration으로 시작하는 모든 설정을 읽어 들여서 빈(Bean)으로 등록
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}
	
	@Bean
	public PlatformTransactionManager transactionManger() {
		return new DataSourceTransactionManager(dataSource());  // 스프링에서 제공하는 트랜잭션 매니저를 빈(Bean)으로 등록
	}
}
