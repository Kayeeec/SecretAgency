package cz.muni.fi.pv168;

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
    private JdbcTemplate jdbc;

    public AgentManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public void createAgent(Agent agent) {
        SimpleJdbcInsert insertAgent = new SimpleJdbcInsert(jdbc)
                .withTableName("agents").usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("codename", agent.getCodeName())
                .addValue("contact", agent.getContact())
                .addValue("note", agent.getNote())
                .addValue("status", agent.getStatus());

        Number id = insertAgent.executeAndReturnKey(parameters);
        agent.setId(id.longValue());

    }

    @Override
    public void updateAgent(Agent agent) {
        jdbc.update("UPDATE agents set codename=?,contact=?,note=?,status=? where id=?",
                agent.getCodeName(),
                agent.getContact(),
                agent.getNote(),
                agent.getStatus(),
                agent.getId()
        );
    }

    @Override
    public void deleteAgent(Agent agent) {

        jdbc.update("DELETE FROM agents WHERE id=?", agent.getId());
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

    private RowMapper<Agent> agentMapper = (rs, rowNum) ->
            new Agent(rs.getLong("id"), rs.getString("codename"),
                    rs.getString("contact"), rs.getString("note"),
                    agentStatusFromString(rs.getString("status")));


    @Override
    public List<Agent> getAllAgents() {

        return jdbc.query("SELECT * FROM agents", agentMapper);
    }

    @Override
    public List<Agent> getAllAgentsWithStatus(AgentStatus status) {
        return jdbc.query("SELECT * FROM agents WHERE status=?", agentMapper,
                agentStatusToString(status));
    }
}






































