package cz.muni.fi.pv168;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public class AgentManagerImpl implements AgentManager {

    final static Logger log = LoggerFactory.getLogger(AgentManagerImpl.class);

    private final DataSource dataSource;

    public AgentManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public void createAgent(Agent agent) {

    }

    @Override
    public void updateAgent(Agent agent) {

    }

    @Override
    public void deleteAgent(Agent agent) throws SecretAgencyException{

    }

    @Override
    public List<Agent> getAllAgents() throws SecretAgencyException{
       return null;
    }

    @Override
    public List<Agent> getAllAgentsWithStatus(AgentStatus status) {
        return null;
    }

    @Override
    public Agent getAgentByCodeName(String name) {
        return null;
    }

    @Override
    public Agent getAgentById(Long id) {
        return null;
    }
}
