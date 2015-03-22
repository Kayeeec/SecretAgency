package cz.muni.fi.pv168;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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
        if (agent == null) {
            throw new IllegalArgumentException("grave is null");
        }
        if (agent.getId() != null) {
            throw new IllegalArgumentException("grave id is already set");
        }



    }

    @Override
    public void updateAgent(Agent agent) {

    }

    @Override
    public void deleteAgent(Agent agent) {

    }

    @Override
    public List<Agent> getAllAgents() {
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
