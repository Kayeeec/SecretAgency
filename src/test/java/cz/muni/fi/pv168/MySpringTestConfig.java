//package cz.muni.fi.pv168;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.support.EncodedResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.test.jdbc.JdbcTestUtils;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;
//
//@Configuration
//@EnableTransactionManagement
//public class MySpringTestConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        //embedded databáze
//        return new EmbeddedDatabaseBuilder()
//                .setType(DERBY)
//                .addScript("classpath:tableCreator.sql")
//                .addScript("classpath:testData.sql")
//                .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//    @Bean
//    public AgentManager agentManager() {
//        return new AgentManagerImpl(dataSource());
//    }
//
//    @Bean
//    public MissionManager missionManager() {
//        // MissionManagerImpl nepoužívá Spring JDBC
//        return new MissionManagerImpl(new TransactionAwareDataSourceProxy(dataSource()));
//    }
//
//    @Bean
//    public AssignmentManager assignmentManager() {
//        AssignmentManagerImpl assignmentManager = new AssignmentManagerImpl(dataSource());
//        assignmentManager.setAgentManager(agentManager());
//        assignmentManager.setMissionManager(missionManager());
//        return assignmentManager;
//    }
//}
