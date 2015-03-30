package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public class AssignmentManagerImpl implements AssignmentManager {
    @Override
    public void createAssignment(Assignment assignment) {

    }

    @Override
    public void updateAssignment(Assignment assignment) {

    }

    @Override
    public void deleteAssignment(Assignment assignment) {

    }

    @Override
    public List<Assignment> getAllAssignmentsForAgent(Agent agent) {
        return null;
    }

    @Override
    public List<Assignment> getAllAssignmentsForMission(Mission mission) {
        return null;
    }

    @Override
    public Assignment getAssignment(Agent agent, Mission mission) {
        return null;
    }

    @Override
    public List<Mission> getUnassignedMissions() {
        return null;
    }

    @Override
    public List<Agent> getAvailableAgents(Mission mission) {
        return null;
    }

    @Override
    public String getAssignmentDuration(Assignment assignment) {
        return null;
    }
}
