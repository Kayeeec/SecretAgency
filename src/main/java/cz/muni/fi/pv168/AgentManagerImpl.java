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
        if (agent == null) {
            throw new IllegalArgumentException("agent is null");
        }
        if (agent.getId() != null) {
            throw new IllegalArgumentException("agent id is already set");
        }



    }

    @Override
    public void updateAgent(Agent agent) {

    }

    @Override
    public void deleteAgent(Agent agent) throws SecretAgencyException{
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement st = conn.prepareStatement("DELETE FROM agent WHERE id=?")) {
                st.setLong(1,agent.getId());
                if(st.executeUpdate()!=1) {
                    throw new SecretAgencyException("did not delete agent with id =" + agent.getId());
                }
            }
        } catch (SQLException ex) {
            log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all agents", ex);
        }
    }

    private Agent resultSetToAgent(ResultSet rs) throws SQLException {
        Agent agent = new Agent();
        agent.setId(rs.getLong("id"));
        agent.setCodeName(rs.getString("codeName"));
        agent.setContact(rs.getString("contact"));
        //agent.setStatus(rs.getAgentStatus("status"));
        agent.setNote(rs.getString("note"));
        return agent;
    }

    @Override
    public List<Agent> getAllAgents() throws SecretAgencyException{
        log.debug("finding all agents");
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id,codeName,contact,note, status FROM agent")) {
                ResultSet rs = st.executeQuery();
                List<Agent> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToAgent(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all agents", ex);
        }
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
