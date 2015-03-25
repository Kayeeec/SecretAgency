package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface AgentManager {

    public void createAgent(Agent agent) throws SecretAgencyException;

    public void updateAgent(Agent agent) throws SecretAgencyException;

    public void deleteAgent(Agent agent) throws SecretAgencyException;

    public List<Agent> getAllAgents() throws SecretAgencyException;

    public List<Agent> getAllAgentsWithStatus(AgentStatus status) throws SecretAgencyException;

    public Agent getAgentByCodeName(String name) throws SecretAgencyException;

    public Agent getAgentById(Long id)throws SecretAgencyException;
}
