package cz.muni.fi.pv168;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MissionManagerImplTest extends TestCase {
    private EmbeddedDatabase db;
    private MissionManager missionManager;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.DERBY).addScript("tableCreator.sql").build();
        missionManager = new MissionManagerImpl(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

//    @Before
//    public void setUp() throws Exception {
//        Properties myconf = new Properties();
//        myconf.load(Test.class.getResourceAsStream("/myconf.properties"));
//
//
////        BasicDataSource ds = new BasicDataSource();
////        ds.setUrl(myconf.getProperty("jdbc.url"));
////        ds.setUsername(myconf.getProperty("jdbc.user"));
////        ds.setPassword(myconf.getProperty("jdbc.password"));
//
//        BasicDataSource ds = new BasicDataSource();
//        ds.setUrl("jdbc:derby://localhost:1527/MojeDB");
//        ds.setUsername("user");
//        ds.setPassword("heslo");
//
//        missionManager = new MissionManagerImpl(ds);
//
//    }


    @Test
    public void testCreateMissionWithNull() throws Exception {
        try{
            missionManager.createMission(null);
            fail("Metoda createMission() nevyhodila NullPointerException() při prázdném vstupu.");
        }catch(NullPointerException ex){
            //no reaction, wanted behaviour
        }
    }


    public void testCreateMission() throws Exception {
        Mission mission = new Mission();
        mission.setName("Cobra");
        mission.setDescription("A venomous Snake");
        mission.setLocation("Moscow");

        assertNull(mission.getStartTime());
        assertNull(mission.getEndTime());
        assertNull(mission.getMaxEndTime());
        assertEquals("A venomous Snake", mission.getDescription());
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        missionManager.createMission(mission);

        Long missionId = missionManager.getMissionByName("Cobra").getId();
        assertNotNull("missionID je null",missionId);
        mission.setId(missionId);

        Mission managedMission = missionManager.getMissionById(missionId);

        assertEquals("mission and managedMission not equal",mission,managedMission);
        assertNotSame("mission and managetMission are the same", mission, managedMission);

    }

    public void testCreateMissionDefaultValues() throws Exception {
        Mission mission = new Mission();
        mission.setName("Cobra");

        assertNull(mission.getDescription());
        assertNull(mission.getLocation());
        assertNull(mission.getStartTime());
        assertNull(mission.getEndTime());
        assertNull(mission.getMaxEndTime());
        assertEquals(MissionStatus.WAITING, mission.getStatus());
    }

    @Test
    public void testUpdateMissionWithNull() throws Exception {
        try {
            missionManager.updateMission(null);
            fail("Metoda updateMission() nevyhodila NullPointerException() při prázdném vstupu.");

        }catch (NullPointerException ex){
            //no reaction, wanted behaviour
        }

    }

    public void testUpdateMission() throws Exception {
        Mission m1 = new Mission();
        m1.setName("Cobra");
        m1.setDescription("A venomous Snake");
        m1.setLocation("Moscow");

        Mission m2 = new Mission();
        m2.setName("Karel");
        m2.setDescription("My friend's imaginary friend.");
        m2.setLocation("Brno");

        missionManager.createMission(m1);
        missionManager.createMission(m2);

        Long id1 = m1.getId();

        //check updating each updatable attribute individually
        //location
        Mission mission = missionManager.getMissionById(id1);
        mission.setLocation("Kyjev");
        missionManager.updateMission(mission);

        assertEquals(id1,mission.getId());
        assertEquals("Cobra", mission.getName());
        assertEquals("Kyjev", mission.getLocation());
        assertEquals(null, mission.getStartTime());
        assertEquals(null, mission.getEndTime());
        assertEquals(null, mission.getMaxEndTime());
        assertEquals("A venomous Snake", mission.getDescription());
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        //startTime
        mission = missionManager.getMissionById(id1);
        mission.setStartTime(LocalDate.of(2015, 4, 13));
        missionManager.updateMission(mission);

        assertEquals(id1,mission.getId());
        assertEquals("Cobra", mission.getName());
        assertEquals("Kyjev", mission.getLocation());
        assertEquals(LocalDate.of(2015, 4, 13), mission.getStartTime());
        assertEquals(null, mission.getEndTime());
        assertEquals(null, mission.getMaxEndTime());
        assertEquals("A venomous Snake", mission.getDescription());
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        //endTime
        mission = missionManager.getMissionById(id1);
        mission.setEndTime(LocalDate.of(2015, 4, 20));
        missionManager.updateMission(mission);

        assertEquals(id1,mission.getId());
        assertEquals("Cobra", mission.getName());
        assertEquals("Kyjev", mission.getLocation());
        assertEquals(LocalDate.of(2015, 4, 13), mission.getStartTime());
        assertEquals(LocalDate.of(2015, 4, 20), mission.getEndTime());
        assertEquals(null, mission.getMaxEndTime());
        assertEquals("A venomous Snake", mission.getDescription());
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        //maxEndTime
        mission = missionManager.getMissionById(id1);
        mission.setMaxEndTime(LocalDate.of(2015, 4, 25));
        missionManager.updateMission(mission);

        assertEquals(id1,mission.getId());
        assertEquals("Cobra", mission.getName());
        assertEquals("Kyjev", mission.getLocation());
        assertEquals(LocalDate.of(2015, 4, 13), mission.getStartTime());
        assertEquals(LocalDate.of(2015, 4, 20), mission.getEndTime());
        assertEquals(LocalDate.of(2015, 4, 25), mission.getMaxEndTime());
        assertEquals("A venomous Snake", mission.getDescription());
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        //description
        mission = missionManager.getMissionById(id1);
        mission.setDescription("Hello");
        missionManager.updateMission(mission);

        assertEquals(id1,mission.getId());
        assertEquals("Cobra", mission.getName());
        assertEquals("Kyjev", mission.getLocation());
        assertEquals(LocalDate.of(2015, 4, 13), mission.getStartTime());
        assertEquals(LocalDate.of(2015, 4, 20), mission.getEndTime());
        assertEquals(LocalDate.of(2015, 4, 25), mission.getMaxEndTime());
        assertEquals("Hello", mission.getDescription());
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        //status
        mission = missionManager.getMissionById(id1);
        mission.setStatus(MissionStatus.ONGOING);
        missionManager.updateMission(mission);

        assertEquals(id1,mission.getId());
        assertEquals("Cobra", mission.getName());
        assertEquals("Kyjev", mission.getLocation());
        assertEquals(LocalDate.of(2015, 4, 13), mission.getStartTime());
        assertEquals(LocalDate.of(2015, 4, 20), mission.getEndTime());
        assertEquals(LocalDate.of(2015, 4, 25), mission.getMaxEndTime());
        assertEquals("Hello", mission.getDescription());
        assertEquals(MissionStatus.ONGOING, mission.getStatus());

        //check if it did not change the other mission
        assertEquals(m2, missionManager.getMissionById(m2.getId()));

    }

    @Test
    public void testUpdateMissionSetName() throws Exception {

        Mission m1 = new Mission();
        m1.setName("Cobra");
        missionManager.createMission(m1);

        Mission mission1 = missionManager.getMissionByName("Cobra");
        Long mission1Id = mission1.getId();

        Mission m2 = missionManager.getMissionById(mission1Id);
        m2.setName("Mamba");
        missionManager.updateMission(m2);

        String resultName = missionManager.getMissionById(mission1Id).getName();

        assertTrue(resultName.equals("Cobra"));

    }

    @Test
    public void testDeleteMissionWithNull() throws Exception {
        try {
            missionManager.deleteMission(null);
            fail("Metoda deleteMission() nevyhodila NullPointerException() při prázdném vstupu.");

        }catch (NullPointerException ex){
            //no reaction, wanted behaviour
        }
    }

    public void testDeleteMission() throws Exception {
        Mission mission1 = new Mission();
        mission1.setName("Cobra");
        mission1.setDescription("A venomous Snake.");
        mission1.setLocation("Moscow");

        Mission mission2 = new Mission();
        mission2.setName("Raging Bull");
        mission2.setDescription("Whatever you do, do not drink their rum!");
        mission2.setLocation("Havana");

        missionManager.createMission(mission1);
        missionManager.createMission(mission2);

        Long id1 = mission1.getId();
        Long id2 = mission2.getId();

        assertNotNull(missionManager.getMissionById(id1));
        assertNotNull(missionManager.getMissionById(id2));

        missionManager.deleteMission(mission1);

        assertNull(missionManager.getMissionById(id1));
        assertNotNull(missionManager.getMissionById(id2));
    }

    @Test
    public void testGetAllMissionsNoMissions() throws SecretAgencyException {
        List<Mission> list = missionManager.getAllMissions();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testGetAllMissions() throws SQLException, SecretAgencyException {

        Mission mission1 = new Mission();
        mission1.setName("Cobra");

        Mission mission2 = new Mission();
        mission2.setName("Raging Bull");

        missionManager.createMission(mission1);
        missionManager.createMission(mission2);

        Long mission1ID = missionManager.getMissionByName("Cobra").getId();
        Long mission2ID = missionManager.getMissionByName("Raging Bull").getId();

        mission1.setId(mission1ID);
        mission2.setId(mission2ID);

        List<Mission> expected = Arrays.asList(mission1, mission2);
        List<Mission> result = missionManager.getAllMissions();

        Collections.sort(expected);
        Collections.sort(result);

        assertTrue(expected.equals(result));

    }

    @Test
    public void testGetAllMissionsWithStatus() throws Exception {
        //WAITING, ONGOING, FINISHED, FAILED
        Mission waiting = new Mission();
        Mission ongoing = new Mission();
        Mission finished = new Mission();
        Mission failed = new Mission();

        waiting.setName("Cobra");
        waiting.setStatus(MissionStatus.WAITING);
        ongoing.setName("Mangus");
        ongoing.setStatus(MissionStatus.ONGOING);
        finished.setName("Hydra");
        finished.setStatus(MissionStatus.FINISHED);
        failed.setName("Lama");
        failed.setStatus(MissionStatus.FAILED);

        assertTrue(missionManager.getAllMissionsWithStatus(MissionStatus.WAITING).isEmpty());
        assertTrue(missionManager.getAllMissionsWithStatus(MissionStatus.ONGOING).isEmpty());
        assertTrue(missionManager.getAllMissionsWithStatus(MissionStatus.FINISHED).isEmpty());
        assertTrue(missionManager.getAllMissionsWithStatus(MissionStatus.FAILED).isEmpty());

        missionManager.createMission(waiting);
        missionManager.createMission(ongoing);
        missionManager.createMission(finished);
        missionManager.createMission(failed);

        List<Mission> expectedWaiting = Arrays.asList(waiting);
        List<Mission> expectedOngoing = Arrays.asList(ongoing);
        List<Mission> expectedFinished = Arrays.asList(finished);
        List<Mission> expectedFailed = Arrays.asList(failed);

        List<Mission> actualWaiting = missionManager.getAllMissionsWithStatus(MissionStatus.WAITING);
        List<Mission> actualOngoing = missionManager.getAllMissionsWithStatus(MissionStatus.ONGOING);
        List<Mission> actualFinished = missionManager.getAllMissionsWithStatus(MissionStatus.FINISHED);
        List<Mission> actualFailed = missionManager.getAllMissionsWithStatus(MissionStatus.FAILED);

//        assertTrue(expectedWaiting.size() == 1);
//        assertTrue(actualWaiting.size() == 1);
//        assertEquals(expectedWaiting.get(0), actualWaiting.get(0));
        assertThat(actualWaiting,is(expectedWaiting));
        assertThat(actualOngoing,is(expectedOngoing));
        assertTrue(expectedFinished.equals(actualFinished));
        assertTrue(expectedFailed.equals(actualFailed));



//        assertEquals(expectedWaiting,  missionManager.getAllMissionsWithStatus(MissionStatus.WAITING));
//        assertEquals(expectedOngoing,  missionManager.getAllMissionsWithStatus(MissionStatus.ONGOING));
//        assertEquals(expectedFinished, missionManager.getAllMissionsWithStatus(MissionStatus.FINISHED));
//        assertEquals(expectedFailed,   missionManager.getAllMissionsWithStatus(MissionStatus.FAILED));

    }




}