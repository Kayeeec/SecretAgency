package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface AgentManager {
    public void createAgent(Agent agent) throws SecretAgencyException;

    public void updateAgent(Agent agent) throws SecretAgencyException;

    public void deleteAgent(Agent agent);

    public List<Agent> getAllAgents();

    public List<Agent> getAllAgentsWithStatus(AgentStatus status);

    public Agent getAgentById(Long id) ;

    public Agent getAgentByCodeName(String codeName);
}
