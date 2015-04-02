/*
package cz.muni.fi.pv168;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.sql.DataSource;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySpringTestConfig.class})
@Transactional
public class AssignmentManagerImplTest extends TestCase {

    @Autowired
    private AssignmentManager assignmentManager;


    @Test
    public void testCreateMissionWithNull() throws Exception {
        try{
            assignmentManager.createAssignment(null);
            fail("Metoda createAssignment() nevyhodila NullPointerException() při prázdném vstupu.");
        }catch(NullPointerException ex){
            //no reaction, wanted behaviour
        }

    }

    @Test
    public void testCreateAssignment() throws Exception {
        Agent agent = assignmentManager.

        //Assignment as = new Assignment()
    }

    public void testUpdateAssignment() throws Exception {

    }

    public void testDeleteAssignment() throws Exception {

    }

    public void testGetAllAssignmentsForAgent() throws Exception {

    }

    public void testGetAllAssignmentsForMission() throws Exception {

    }

    public void testGetAssignment() throws Exception {

    }

    public void testGetUnassignedMissions() throws Exception {

    }

    public void testGetAvailableAgents() throws Exception {

    }
}

*/