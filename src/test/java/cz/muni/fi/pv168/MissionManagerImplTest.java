package cz.muni.fi.pv168;

import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MissionManagerImplTest extends TestCase {
    MissionManagerImpl missionManager;

    @Before
    public void setUp() throws Exception {
        Properties myconf = new Properties();
        myconf.load(Test.class.getResourceAsStream("/myconf.properties"));


//        BasicDataSource ds = new BasicDataSource();
//        ds.setUrl(myconf.getProperty("jdbc.url"));
//        ds.setUsername(myconf.getProperty("jdbc.user"));
//        ds.setPassword(myconf.getProperty("jdbc.password"));

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:derby://localhost:1527/MojeDB");
        ds.setUsername("user");
        ds.setPassword("heslo");

        missionManager = new MissionManagerImpl(ds);

    }

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

        Long missionId = mission.getId(); //create method will also set unique id

        assertNotNull(missionId);

        Mission managedMission = missionManager.getMissionById(missionId);

        assertEquals(mission,managedMission);
        assertNotSame(mission,managedMission);

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
        assertEquals(MissionStatus.WAITING, mission.getStatus());

        //check if it did not change the other mission
        assertEquals(m2, missionManager.getMissionById(m2.getId()));

    }

    @Test
    public void testUpdateMissionSetName() throws Exception {
        try {
            Mission m1 = new Mission();
            m1.setName("Cobra");
            m1.setDescription("A venomous Snake");
            m1.setLocation("Moscow");

            missionManager.createMission(m1);
            Mission m2 = missionManager.getMissionByName("Cobra");

            Long id = m2.getId();

            missionManager.createMission(m1);
            Mission mission = missionManager.getMissionById(id);

            mission.setName("Mamba");
            fail("Metoda setName() nevyhazuje IllegalStateException při pokusu o změnu názvu mise.");


        }catch(IllegalStateException ex){
            //wanted behaviour
        }
    }

    public void testUpdateMissionSetID() throws Exception {
        try {
            Mission m1 = new Mission();
            m1.setName("Cobra");
            m1.setDescription("A venomous Snake");
            m1.setLocation("Moscow");

            missionManager.createMission(m1);
            Mission m2 = missionManager.getMissionByName("Cobra");
            Long id = m2.getId();


            Mission mission = missionManager.getMissionById(id);

            mission.setId(1l);
            fail("Metoda setId() nevyhazuje IllegalStateException při pokusu o změnu id.");


        }catch(IllegalStateException ex){
            //wanted behaviour
        }

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
        mission1.setDescription("A venomous Snake.");
        mission1.setLocation("Moscow");

        Mission mission2 = new Mission();
        mission2.setName("Raging Bull");
        mission2.setDescription("Whatever you do, do not drink their rum!");
        mission2.setLocation("Havana");

        missionManager.createMission(mission1);
        missionManager.createMission(mission2);


        List<Mission> expected = Arrays.asList(mission1, mission2);
        List<Mission> result = missionManager.getAllMissions();

        Collections.sort(expected);
        Collections.sort(result);

        assertEquals(expected, result);

    }

    @Test
    public void testGetAllMissionsWithStatus() throws Exception {

    }

    @Test
    public void testGetMissionDuration() throws Exception {

    }



}