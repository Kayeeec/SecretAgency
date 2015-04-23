package cz.muni.fi.pv168;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by sachmet on 25.3.15.
 */
public class AgentManagerImpl implements AgentManager {
    final static Logger log = LoggerFactory.getLogger(AgentManagerImpl.class);
    private JdbcTemplate jdbc;

    public AgentManagerImpl(DataSource dataSource) {
        log.info("AgentManagerIfo constructor called.");
        this.jdbc = new JdbcTemplate(dataSource);
    }

    private AgentStatus agentStatusFromString(String str){
        if (str.equals("DECEASED")){
            return AgentStatus.DECEASED;
        }
        if(str.equals("ACTIVE")){
            return AgentStatus.ACTIVE;
        }
        return AgentStatus.INACTIVE;
    }
    private String agentStatusToString(AgentStatus status){
        if(status.equals(AgentStatus.ACTIVE)){
            return "ACTIVE";
        }
        if(status.equals(AgentStatus.INACTIVE)){
            return "INACTIVE";
        }
        return "DECEASED";
    }

    @Override
    public void createAgent(Agent agent) throws SecretAgencyException {
        log.info("createAgent({})", agent);
        try{
            SimpleJdbcInsert insertAgent = new SimpleJdbcInsert(jdbc)
                    .withTableName("agents").usingGeneratedKeyColumns("id");

            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("codename", agent.getCodeName())
                    .addValue("contact", agent.getContact())
                    .addValue("note", agent.getNote())
                    .addValue("status", agentStatusToString(agent.getStatus()));

            Number id = insertAgent.executeAndReturnKey(parameters);
            agent.setId(id.longValue());

        }catch (Exception ex){
            log.error("exception {}:{} in createAgent({})", ex, ex.getMessage(), agent);
            throw new SecretAgencyException(ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public void updateAgent(Agent agent) throws SecretAgencyException {
        log.info("updateAgent({})", agent);
        try{
            Agent agentToUpdate = getAgentById(agent.getId());
            if (!agentToUpdate.getCodeName().equals(agent.getCodeName())){
                throw new SecretAgencyException("Method updateAgent() tried to change agents codeName.");
            }

            jdbc.update("UPDATE agents set codename=?,contact=?,note=?,status=? where id=?",
                    agent.getCodeName(),
                    agent.getContact(),
                    agent.getNote(),
                    agentStatusToString(agent.getStatus()),
                    agent.getId()
            );
        }catch (Exception ex){
            log.error("exception {}:{} in updateAgent({})", ex, ex.getMessage(), agent);
            throw new SecretAgencyException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void deleteAgent(Agent agent) throws SecretAgencyException {
        log.info("deleteAgent({})", agent);
        try{
            jdbc.update("DELETE FROM agents WHERE id=?", agent.getId());
        }catch(Exception ex){
            log.error("exception {}:{} in deleteAgent({})", ex, ex.getMessage(), agent);
            throw new SecretAgencyException(ex.getMessage(), ex.getCause());
        }

    }


    private RowMapper<Agent> agentMapper = (rs, rowNum) ->
            new Agent(rs.getLong("id"), rs.getString("codename"),
                    rs.getString("contact"), rs.getString("note"),
                    agentStatusFromString(rs.getString("status")));


    @Override
    public List<Agent> getAllAgents() throws SecretAgencyException{
        log.info("getAllAgents()");
        try {
            return jdbc.query("SELECT * FROM agents", agentMapper);
        }
        catch (Exception e){
            throw new SecretAgencyException("did not get all agents");
        }

    }

    @Override
    public List<Agent> getAllAgentsWithStatus(AgentStatus status) {
        log.info("getAllAgentsWithStatus({})", status);
        return jdbc.query("SELECT * FROM agents WHERE status=?", agentMapper,
                agentStatusToString(status));
    }

    public Agent getAgentById(Long id)  {
        log.debug("getAgentById({})", id);
        if (id == null) throw new IllegalArgumentException("id is null");
        List<Agent> list = jdbc.query("SELECT id, CODENAME, CONTACT, NOTE, STATUS  FROM AGENTS WHERE id = ?", agentMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Agent getAgentByCodeName(String codeName) {
        log.debug("getAgentByCodeName({})", codeName);
        if (codeName == null) throw new IllegalArgumentException("codename is null");
        List<Agent> list = jdbc.query("SELECT id, CODENAME, CONTACT, NOTE, STATUS  FROM AGENTS WHERE CODENAME = ?", agentMapper, codeName);
        return list.isEmpty() ? null : list.get(0);
    }
}






































