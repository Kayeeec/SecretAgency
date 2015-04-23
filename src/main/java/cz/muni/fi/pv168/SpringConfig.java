package cz.muni.fi.pv168;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by alexandra on 22.4.15.
 */

@Configuration
@EnableTransactionManagement
public class SpringConfig {

    @Bean
    public DataSource dataSource() {
        //Apache DBCP connection pooling DataSource
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
        bds.setUrl("jdbc:derby://localhost:1527/MojeDB");
        bds.setUsername("sasa");
        bds.setPassword("sasa");
        return bds;
    }

    /*@Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(DERBY)
                .addScript("classpath:schema-javadb.sql")
                .addScript("classpath:test-data.sql")
                .build();
    }*/

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public AgentManager agentManager() {
        return new AgentManagerImpl(dataSource());
    }

    @Bean
    public MissionManager missionManager() {
        return new MissionManagerImpl(new TransactionAwareDataSourceProxy(dataSource()));
    }

    @Bean
    public AssignmentManager assignmentManager() {
        AssignmentManagerImpl assignmentManager = new AssignmentManagerImpl(dataSource());
        assignmentManager.setAgentManager(agentManager());
        assignmentManager.setMissionManager(missionManager());
        return assignmentManager;
    }


}
