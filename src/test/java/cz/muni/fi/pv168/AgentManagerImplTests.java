package cz.muni.fi.pv168;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.nio.channels.IllegalSelectorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.*;

//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertNotSame;
//import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.*;

import static org.hamcrest.CoreMatchers.*;


//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.assertTrue;

/**
 * Created by alexandra on 22.3.15.
 */
public class AgentManagerImplTests {

    private EmbeddedDatabase db;
    private AgentManager agentManager;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).addScript("tableCreator.sql").build();
        agentManager = new AgentManagerImpl(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    public void testCreateAgentWithNull() throws Exception{
        try {
            agentManager.createAgent(null);
            fail("Did not throw NullPointerException for null input.");
        }
        catch (Exception ex) {}
    }

    @Test
    public void testUpdateAgentWithNull() throws Exception{
        try {
            agentManager.updateAgent(null);
            fail("Did not throw NullPointerException for null input.");
        }
        catch (Exception ex) {}
    }

    @Test
    public void testGetAgentByIdWithNull() throws Exception{
        try {
            agentManager.getAgentById(null);
            fail("Did not throw NullPointerException for null input.");
        }
        catch (Exception ex) {}
    }

    @Test
    public void getAgentByCodeNameWithNull() throws Exception{
        try {
            agentManager.getAgentByCodeName(null);
            fail("Did not throw NullPointerException for null input.");
        }
        catch (Exception ex) {}
    }

    @Test
    public void testCreateAgent() throws Exception {
        Agent agent = new Agent();
        agent.setCodeName("Agent000");
        agent.setContact("000@secret_mail.org");

        assertNull(agent.getNote());
        assertEquals("Agent000", agent.getCodeName());
        assertEquals(AgentStatus.ACTIVE, agent.getStatus());
        agentManager.createAgent(agent);
        assertNotNull(agent.getId());
        Agent createdAgent = agentManager.getAgentById(agent.getId());
        assertEquals(agent, createdAgent);
        assertNotSame(agent, createdAgent);
    }

    @Test
    public void testUpdateAgent() throws Exception {
        Agent agent1 = new Agent();
        agent1.setCodeName("Agent001");
        agent1.setNote("something interesting");
        agent1.setContact("001@secret_mail.org");

        Agent agent2 = new Agent();
        agent2.setCodeName("Agent002");
        agent2.setNote("something uninteresting");
        agent2.setContact("002@secret_mail.org");

        agentManager.createAgent(agent1);
        agentManager.createAgent(agent2);

        Long agent1id = agent1.getId();

        //update note
        Agent agent = agentManager.getAgentById(agent1id);
        agent.setNote("something more interesting");
        agentManager.updateAgent(agent);

        assertEquals(agent1id, agent.getId());
        assertEquals("Agent001", agent.getCodeName());
        assertEquals("001@secret_mail.org", agent.getContact());
        assertEquals("something more interesting", agent.getNote());
        assertEquals(AgentStatus.ACTIVE, agent.getStatus());

        //update contact
        agent = agentManager.getAgentById(agent1id);
        agent.setContact("Agent001@secret_mail.org");
        agentManager.updateAgent(agent);

        assertEquals(agent1id, agent.getId());
        assertEquals("Agent001", agent.getCodeName());
        assertEquals("Agent001@secret_mail.org", agent.getContact());
        assertEquals("something more interesting", agent.getNote());
        assertEquals(AgentStatus.ACTIVE, agent.getStatus());

        //update status
        agent = agentManager.getAgentById(agent1id);
        agent.setStatus(AgentStatus.DECEASED);
        agentManager.updateAgent(agent);

        assertEquals(agent1id, agent.getId());
        assertEquals("Agent001", agent.getCodeName());
        assertEquals("Agent001@secret_mail.org", agent.getContact());
        assertEquals("something more interesting", agent.getNote());
        assertEquals(AgentStatus.DECEASED, agent.getStatus());

        assertEquals(agent2, agentManager.getAgentById(agent2.getId()));
    }

    @Test
    public void testSettingNameForSecondTime() throws Exception{
        try{
            Agent agent = new Agent();
            agent.setCodeName("Agent111");
            agent.setContact("111@secret_mail.org");
            agent.setNote("nothing interesting");

            agentManager.createAgent(agent);

            Agent agentForUpdate = agentManager.getAgentById(agent.getId());
            agentForUpdate.setCodeName("Agent222");
            agentManager.updateAgent(agentForUpdate);
            fail("successfully updated agents code name");
        }
        catch(Exception ex){
        }
    }

    @Test
    public void testTwoAgentsWithTheSameCodeName() throws Exception{
        try {
            Agent agent = new Agent();
            agent.setCodeName("Agent000");
            agent.setContact("0908 762 565");
            agent.setNote("nothing interesting");
            agent.setStatus(AgentStatus.ACTIVE);

            Agent agent1 = new Agent();
            agent1.setCodeName("Agent001");
            agent1.setContact("0905 762 565");
            agent1.setNote("something interesting");
            agent1.setStatus(AgentStatus.ACTIVE);

            agent.setCodeName("Agent001");
            agentManager.createAgent(agent);
            agentManager.createAgent(agent1);
            fail("Successfully created two agents with the same code name.");
        }
        catch (Exception ex) {}

    }

//    @Test public void testGetAllAgentsWithStatus() throws Exception{
//        Agent agent1 = new Agent();
//        agent1.setCodeName("Agent001");
//        agent1.setContact("0905 762 565");
//        agent1.setNote("something interesting");
//        agent1.setStatus(AgentStatus.ACTIVE);
//
//        Agent agent2 = new Agent();
//        agent2.setCodeName("Agent002");
//        agent2.setContact("0905 768 965");
//        agent2.setNote("something interesting");
//        agent2.setStatus(AgentStatus.INACTIVE);
//
//        Agent agent3 = new Agent();
//        agent3.setCodeName("Agent003");
//        agent3.setContact("0905 738 965");
//        agent3.setNote("something interesting");
//        agent3.setStatus(AgentStatus.DECEASED);
//
//        agentManager.createAgent(agent1);
//        agentManager.createAgent(agent2);
//        agentManager.createAgent(agent3);
//        assertThat(agentManager.getAllAgentsWithStatus(AgentStatus.ACTIVE), hasItem(agent1));
//        assertThat(agentManager.getAllAgentsWithStatus(AgentStatus.INACTIVE), hasItem(agent2));
//        assertThat(agentManager.getAllAgentsWithStatus(AgentStatus.DECEASED), hasItem(agent3));
//    }

    @Test
    public void testGetAgent() throws SecretAgencyException{
        Agent agent = new Agent();
        agent.setCodeName("Agent000");
        agent.setContact("0908 762 565");
        agent.setNote("nothing interesting");
        agent.setStatus(AgentStatus.ACTIVE);

        agentManager.createAgent(agent);
        Long agentId = agent.getId();
        assertNotNull(agentId);
        Agent result = agentManager.getAgentById(agentId);
        assertEquals(agent, result);
    }

    @Test
    public void testGetAllAgents() throws Exception{
        Agent agent1 = new Agent();
        agent1.setCodeName("Agent001");
        agent1.setContact("0905 762 565");
        agent1.setNote("something interesting");
        agent1.setStatus(AgentStatus.ACTIVE);

        Agent agent2 = new Agent();
        agent2.setCodeName("Agent002");
        agent2.setContact("0905 768 965");
        agent2.setNote("something interesting");
        agent2.setStatus(AgentStatus.INACTIVE);

        Agent agent3 = new Agent();
        agent3.setCodeName("Agent003");
        agent3.setContact("0905 738 965");
        agent3.setNote("something interesting");
        agent3.setStatus(AgentStatus.DECEASED);



        assertTrue(agentManager.getAllAgents().isEmpty());

        agentManager.createAgent(agent1);
        agentManager.createAgent(agent2);
        agentManager.createAgent(agent3);

        List<Agent> expected = Arrays.asList(agent1, agent2, agent3);
        List<Agent> actual = agentManager.getAllAgents();

        sort(expected);
        sort(actual);

        assertEquals(expected,actual);
    }
}
