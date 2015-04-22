package cz.muni.fi.pv168;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Created by alexandra on 22.4.15.
 */
public class Main {
    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SecretAgencyException {

        log.info("start");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        AgentManager agentManager = ctx.getBean("agentManager", AgentManager.class);

        List<Agent> allAgents = agentManager.getAllAgents();
        System.out.println("all agents = " + allAgents);

    }
}
