package config;

import domain.risks.IPRisk;
import domain.risks.NightRisk;

import domain.risks.Risk;
import domain.risks.RiskAnalyzer;
import domain.time.DateTimeSource;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"config", "controller", "dao", "domain"})
@PropertySource("mysql.properties")
//@PropertySource("h2.properties")
public class JavaConfig {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        System.out.println(environment.getProperty("application.initText"));
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(restDataSource());
        sessionFactory.setPackagesToScan(new String[]{"domain"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource restDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.user"));
        dataSource.setPassword(environment.getProperty("jdbc.pass"));
        return dataSource;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public Risk nightRiskBean() {
        return new NightRisk();
    }

    @Bean
    public Risk ipRiskBean() {
        return new IPRisk();
    }

    @Bean
    public RiskAnalyzer riskAnalyzerBean() {
//        System.out.println("Initializing RiskAnalyzerBean...");
        RiskAnalyzer riskAnalyzer = new RiskAnalyzer();
//        riskAnalyzer.add(new NightRisk());
//        riskAnalyzer.add(new IPRisk());
        return riskAnalyzer;
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
                setProperty("hibernate.globally_quoted_identifiers", environment.getProperty("hibernate.show_sql"));
            }
        };
    }
}