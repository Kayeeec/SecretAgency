package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface AssignmentManager {

    public void createAssignment(Assignment assignment) throws SecretAgencyException;

    public void updateAssignment(Assignment assignment) throws SecretAgencyException;

    public void deleteAssignment(Assignment assignment) throws SecretAgencyException;

    public List<Assignment> getAllAssignments() throws SecretAgencyException;

    public List<Assignment> getAllAssignmentsForAgent(Agent agent) throws SecretAgencyException;

    public List<Assignment> getAllAssignmentsForMission(Mission mission) throws SecretAgencyException;

    public Assignment getAssignment(Agent agent, Mission mission) throws SecretAgencyException;

    public List<Mission> getUnassignedMissions() throws SecretAgencyException;

    public List<Agent> getAvailableAgents(Mission mission) throws SecretAgencyException;

    public String getAssignmentDuration(Assignment assignment) throws SecretAgencyException;
}
