package com.fincom.prestamos.spring;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.web.client.RestTemplate;



@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.**.controller", "com.**.service.impl", "com.**.dao.impl", "com.**.model.persistence", "com.**.soap.client", "com.**.api.client"})
@MapperScan("com.**.model.persistence")
@Import({SwaggerConfig.class})
public class AppConfig {
	
	@Autowired
    private Environment env;

	@Bean
	public TomcatServletWebServerFactory tomcatFactory() {
		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatWebServer(tomcat);
			}

			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();

				resource.setType(DataSource.class.getName());
				resource.setName(env.getProperty("spring.datasource.jndiName"));
				resource.setProperty("factory", "org.apache.tomcat.jdbc.pool.DataSourceFactory");
				resource.setProperty("driverClassName", env.getProperty("spring.datasource.driverClassName"));
				resource.setProperty("url", env.getProperty("spring.datasource.url"));
				resource.setProperty("username", env.getProperty("spring.datasource.username"));
				resource.setProperty("password", env.getProperty("spring.datasource.password"));
				resource.setProperty("initialSize", "1");
				resource.setProperty("maxTotal", "1");
				resource.setProperty("minIdle", "0");
				resource.setProperty("maxIdle", "0");
				resource.setProperty("maxWaitMillis", "600000");
				resource.setProperty("testOnBorrow", "true");
				resource.setProperty("validationInterval", "30000");
				resource.setProperty("validationQuery", "select 1 from dual");
				resource.setProperty("removeAbandoned", "true");
				resource.setProperty("removeAbandonedTimeout", "300");

				context.getNamingResources().addResource(resource);
			}
		};
	}

	@Bean(destroyMethod = "")
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		// bean.setJndiName("java:comp/env/" + env.getProperty("jndiName"));
		bean.setJndiName("java:comp/env/" + env.getProperty("spring.datasource.jndiName"));
		bean.setProxyInterface(DataSource.class);
		// bean.setResourceRef(true);
		bean.setLookupOnStartup(false);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}
	
	@Bean(name = "transactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager() throws Exception {
		return new DataSourceTransactionManager(jndiDataSource());
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeHandlersPackage("com.**.model.handler");
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:base/com/**/model/persistence/*.xml"));
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setMapUnderscoreToCamelCase(true);
		sessionFactory.setConfiguration(configuration);
		return sessionFactory.getObject();
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(jndiDataSource());
		factoryBean.setConfiguration(configuration);
		return factoryBean.getObject();
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
	    restTemplate.setRequestFactory(clientHttpRequestFactory());
	    return restTemplate;
	}
	 
	@Bean
	@ConditionalOnMissingBean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        //clientBuilder.setProxy(new HttpHost(proxyUrl, port));
        //clientBuilder.setDefaultCredentialsProvider(credsProvider);
        //clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());        
        CloseableHttpClient client = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);
        return factory;
	}
}
