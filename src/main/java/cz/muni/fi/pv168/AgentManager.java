package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface AgentManager {
    void createAgent(Agent agent) throws SecretAgencyException;

    void updateAgent(Agent agent) throws SecretAgencyException;

    void deleteAgent(Agent agent) throws SecretAgencyException;

    List<Agent> getAllAgents();

    List<Agent> getAllAgentsWithStatus(AgentStatus status);

    Agent getAgentById(Long id) ;

    Agent getAgentByCodeName(String codeName);
}
