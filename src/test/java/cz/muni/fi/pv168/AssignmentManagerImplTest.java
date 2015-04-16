package cz.muni.fi.pv168;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.time.LocalDate;
import java.util.List;

public class AssignmentManagerImplTest extends TestCase {
    private EmbeddedDatabase db;
    private AssignmentManager assignmentManager;
    private AgentManager agentManager;
    private MissionManager missionManager;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).addScript("tableCreator.sql").addScript("assignmentTestData.sql").build();
        assignmentManager = new AssignmentManagerImpl(db);
        agentManager = new AgentManagerImpl(db);
        missionManager = new MissionManagerImpl(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    public void testCreateAssignmentWithNull() throws Exception {
        try{
            assignmentManager.createAssignment(null);
            fail("Metoda createAssignment nevyhodila výjimku při nulovém parametru.");
        }catch (Exception ex){
            //ok
        }
    }

    //date = rrrr-mm-dd
    private LocalDate date(String date) {
        return LocalDate.parse(date);
    }

    @Test
    public void testCreateAssignment() throws Exception {
        //prepare agent and mission
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");

        Double pay = 250000d;
        LocalDate start = mission.getStartTime();
        LocalDate end = null;
        Assignment assignment = new Assignment(agent, mission, pay, start, end);

        assignmentManager.createAssignment(assignment);
        Assignment addedAssignment = assignmentManager.getAssignment(agent, mission);



        assertNotNull(addedAssignment.getId());
        assertEquals(assignment,addedAssignment);
        assertNotSame(assignment,addedAssignment);

    }

    @Test
    public void testUpdateAssignmentWithNull() throws Exception {
        try{
            assignmentManager.updateAssignment(null);
            fail("Metoda updateAssignment nevyhodila výjimku při nulovém parametru.");
        }catch (Exception ex){
            //ok
        }
    }

    @Test
    public void testUpdateAssignment() throws Exception {

        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");
        LocalDate start = mission.getStartTime();
        LocalDate end = null;

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");
        LocalDate start2 = mission2.getStartTime();

        Assignment ass1 = new Assignment(agent, mission, 250000d, start, end);
        Assignment ass2 = new Assignment(agent2, mission2, 200000d, start2, end);

        assignmentManager.createAssignment(ass1);
        assignmentManager.createAssignment(ass2);

        Assignment assignment = assignmentManager.getAssignment(agent, mission);
        Assignment unchangedAss1 = assignmentManager.getAssignment(agent2, mission2);

        Long assignmentId = assignment.getId();
        Long unchangedAssID = unchangedAss1.getId();

        //payment, startDate, endDate are updatable

        //payment
        assignment.setPayment(300000d);
        assignmentManager.updateAssignment(assignment);

        assertEquals(assignmentId, assignment.getId());
        assertEquals(agent, assignment.getAgent());
        assertEquals(mission, assignment.getMission());
        assertEquals(300000d, assignment.getPayment());
        assertEquals(start, assignment.getStartDate());
        assertEquals(end, assignment.getEndDate());

        //startDate
        assignment.setStartDate(date("2015-04-26"));
        assignmentManager.updateAssignment(assignment);

        assertEquals(assignmentId, assignment.getId());
        assertEquals(agent, assignment.getAgent());
        assertEquals(mission, assignment.getMission());
        assertEquals(300000d, assignment.getPayment());
        assertEquals(date("2015-04-26"), assignment.getStartDate());
        assertEquals(end, assignment.getEndDate());

        //endDate
        assignment.setEndDate(date("2015-05-26"));
        assignmentManager.updateAssignment(assignment);

        assertEquals(assignmentId, assignment.getId());
        assertEquals(agent, assignment.getAgent());
        assertEquals(mission, assignment.getMission());
        assertEquals(300000d, assignment.getPayment());
        assertEquals(date("2015-04-26"), assignment.getStartDate());
        assertEquals(date("2015-05-26"), assignment.getEndDate());

        //the other assignment not changed
        Assignment unchangedAss = assignmentManager.getAssignment(agent2, mission2);

        assertEquals(unchangedAssID, unchangedAss.getId());
        assertEquals(agent2, unchangedAss.getAgent());
        assertEquals(mission2, unchangedAss.getMission());
        assertEquals(200000d, unchangedAss.getPayment());
        assertEquals(start2, unchangedAss.getStartDate());
        assertEquals(end, unchangedAss.getEndDate());
    }

//    public void testUpdateAssignmentId() throws Exception {
//        //does not throw exception just does nothing
//        Agent agent = agentManager.getAgentByCodeName("Bond");
//        Mission mission = missionManager.getMissionByName("Cobra");
//        Double pay = 250000d;
//        LocalDate start = mission.getStartTime();
//        LocalDate end = null;
//        Assignment ass1 = new Assignment(agent, mission, pay, start, end);
//        assignmentManager.createAssignment(ass1);
//
//        Assignment assignment = assignmentManager.getAssignment(agent, mission);
//        Long expectedID = assignment.getId();
//        assignment.setId(expectedID + 1);
//        assignmentManager.updateAssignment(assignment);
//
//        Assignment updated = assignmentManager.getAssignment(agent, mission);
//        assertEquals(expectedID, updated.getId());
//    }

//    public void testUpdateAssignmentAgent() throws Exception {
//
//
//    }
//
//    public void testUpdateAssignmentMission() throws Exception {
//
//
//    }

    public void testDeleteAssignmentWithNull() throws Exception {
        try{
            assignmentManager.deleteAssignment(null);
            fail("Metoda deleteAssignment nevyhodila výjimku při nulovém parametru.");
        }catch (Exception ex){
            //ok
        }

    }

    public void testDeleteAssignment() throws Exception {
        //create two assignments
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");

        Assignment ass1 = new Assignment(agent, mission, 250000d, mission.getStartTime(), mission.getEndTime());
        Assignment ass2 = new Assignment(agent2, mission2, 200000d, mission2.getStartTime(), mission2.getEndTime());

        assignmentManager.createAssignment(ass1);
        assignmentManager.createAssignment(ass2);

        Assignment assignment = assignmentManager.getAssignment(agent, mission);

        assignmentManager.deleteAssignment(assignment);

        assertNull(assignmentManager.getAssignment(agent, mission));
        assertNotNull(assignmentManager.getAssignment(agent2, mission2));

    }

    public void testGetAllAssignmentsForAgent() throws Exception {
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");

        Assignment ass1 = new Assignment(agent, mission, 250000d, mission.getStartTime(), mission.getEndTime());
        Assignment ass2 = new Assignment(agent2, mission2, 400000d, mission2.getStartTime(), mission2.getEndTime());
        Assignment ass3 = new Assignment(agent, mission2, 400000d, mission2.getStartTime(), mission2.getEndTime());

        assignmentManager.createAssignment(ass1);
        assignmentManager.createAssignment(ass2);
        assignmentManager.createAssignment(ass3);

        List<Assignment> assmentsForAgent = assignmentManager.getAllAssignmentsForAgent(agent);
        List<Assignment> assmentsForAgent2 = assignmentManager.getAllAssignmentsForAgent(agent2);

        assertEquals(2, assmentsForAgent.size());
        assertEquals(1, assmentsForAgent2.size());

    }

    public void testGetAllAssignmentsForMission() throws Exception {
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");

        Assignment ass1 = new Assignment(agent, mission, 250000d, mission.getStartTime(), mission.getEndTime());
        Assignment ass2 = new Assignment(agent2, mission2, 400000d, mission2.getStartTime(), mission2.getEndTime());
        Assignment ass3 = new Assignment(agent, mission2, 400000d, mission2.getStartTime(), mission2.getEndTime());

        assignmentManager.createAssignment(ass1);
        assignmentManager.createAssignment(ass2);
        assignmentManager.createAssignment(ass3);

        List<Assignment> assmentsForMission = assignmentManager.getAllAssignmentsForMission(mission);
        List<Assignment> assmentsForMission2 = assignmentManager.getAllAssignmentsForMission(mission2);

        assertEquals(1, assmentsForMission.size());
        assertEquals(2, assmentsForMission2.size());

    }

    public void testGetAssignment() throws Exception {
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");
        Double pay = 250000d;
        LocalDate start = mission.getStartTime();
        LocalDate end = null;

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");
        Double pay2 = 400000d;
        LocalDate start2 = mission2.getStartTime();
        LocalDate end2 = null;

        Assignment ass1 = new Assignment(agent, mission, pay, start, end);
        Assignment ass2 = new Assignment(agent2, mission2, pay2, start2, end2);

        assignmentManager.createAssignment(ass1);
        assignmentManager.createAssignment(ass2);

        Assignment assignment1 = assignmentManager.getAssignment(agent, mission);
        Assignment assignment2 = assignmentManager.getAssignment(agent2, mission2);

        ass1.setId(assignment1.getId());
        ass2.setId(assignment2.getId());

//        assertEquals(ass1, assignment1);
//        assertEquals(ass2, assignment2);
        assertTrue(ass1.equals(assignment1));
        assertTrue(ass2.equals(assignment2));
    }

    public void testGetUnassignedMissions() throws Exception {
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");
        Double pay = 250000d;
        LocalDate start = mission.getStartTime();
        LocalDate end = null;
        Assignment ass1 = new Assignment(agent, mission, pay, start, end);

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");
        Double pay2 = 400000d;
        LocalDate start2 = mission2.getStartTime();
        LocalDate end2 = null;
        Assignment ass2 = new Assignment(agent2, mission2, pay2, start2, end2);

        //no assignments
        List<Mission> list1 = assignmentManager.getUnassignedMissions();
        assertEquals(4, list1.size());

        //assign Cobra
        assignmentManager.createAssignment(ass1);
        List<Mission> list2 = assignmentManager.getUnassignedMissions();
        assertEquals(3, list2.size());

        //assign Mangus
        assignmentManager.createAssignment(ass2);
        List<Mission> list3 = assignmentManager.getUnassignedMissions();
        assertEquals(2, list3.size());
    }

    public void testGetAvailableAgents() throws Exception { //no ongoing or waiting missions
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");

        Mission mission = missionManager.getMissionByName("Cobra"); //Waiting
        Mission mission2 = missionManager.getMissionByName("Mangus"); //Ongoing
        Mission finishedMission = missionManager.getMissionByName("Hydra");
        Mission failedMission = missionManager.getMissionByName("Lama");

        Assignment ass1 = new Assignment(agent, mission, 250000d, mission.getStartTime(), mission.getEndTime());
        Assignment ass2 = new Assignment(agent2, mission2, 400000d, mission2.getStartTime(), mission2.getEndTime());
        Assignment ass3 = new Assignment(agent, finishedMission, 100000d, finishedMission.getStartTime(), finishedMission.getMaxEndTime());
        Assignment ass4 = new Assignment(agent2, failedMission, 100000d, failedMission.getStartTime(), failedMission.getMaxEndTime());

        //no assignments
        List<Agent> list1 = assignmentManager.getAvailableAgents();
        assertEquals("no assignments - both agents should be available",2, list1.size());

        //assign to Hydra - finished
        assignmentManager.createAssignment(ass3);
        List<Agent> list4 = assignmentManager.getAvailableAgents();
        assertEquals("agent should be available if assigned to FINISHED mission",2, list4.size());

        //assign to Lama - failed
        assignmentManager.createAssignment(ass4);
        List<Agent> list5 = assignmentManager.getAvailableAgents();
        assertEquals("agent should be available if assigned to FAILED mission",2, list5.size());

        //assign co Cobra - waiting
        assignmentManager.createAssignment(ass1);
        List<Agent> list2 = assignmentManager.getAvailableAgents();
        assertEquals("agent should NOT be available if has WAITING mission",1, list2.size());

        //assign to Mangus - ongoing
        assignmentManager.createAssignment(ass2);
        List<Agent> list3 = assignmentManager.getAvailableAgents();
        assertTrue("agent should NOT be available if has ONGOING mission",list3.isEmpty());
    }

    public void testGetAllAssignments() throws Exception {
        Agent agent = agentManager.getAgentByCodeName("Bond");
        Mission mission = missionManager.getMissionByName("Cobra");
        Double pay = 250000d;
        LocalDate start = mission.getStartTime();
        LocalDate end = null;
        Assignment ass1 = new Assignment(agent, mission, pay, start, end);

        Agent agent2 = agentManager.getAgentByCodeName("BlackWidow");
        Mission mission2 = missionManager.getMissionByName("Mangus");
        Double pay2 = 400000d;
        LocalDate start2 = mission2.getStartTime();
        LocalDate end2 = null;
        Assignment ass2 = new Assignment(agent2, mission2, pay2, start2, end2);

        //no assignments
        List<Assignment> list1 = assignmentManager.getAllAssignments();
        assertTrue(list1.isEmpty());

        //one assignment
        assignmentManager.createAssignment(ass1);
        List<Assignment> list2 = assignmentManager.getAllAssignments();
        assertEquals(1, list2.size());

        //two assignments
        assignmentManager.createAssignment(ass2);
        List<Assignment> list3 = assignmentManager.getAllAssignments();
        assertEquals(2,list3.size());

    }
}