package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface AssignmentManager {
    public void createAssignment(Assignment assignment);

    public void updateAssignment(Assignment assignment);

    public void deleteAssignment(Assignment assignment);

    public List<Assignment> getAllAssignmentsForAgent(Agent agent);

    public List<Assignment> getAllAssignmentsForMission(Mission mission);

    public Assignment getAssignment(Agent agent, Mission mission);

    public List<Mission> getUnassignedMissions();

    public List<Agent> getAvailableAgents(Mission mission);

    public String getAssignmentDuration(Assignment assignment);
}
