package cz.muni.fi.pv168;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public class AssignmentManagerImpl implements AssignmentManager {

    final static Logger log = LoggerFactory.getLogger(AssignmentManagerImpl.class);
    private final JdbcTemplate jdbc;

    private AgentManager agentManager;
    private MissionManager missionManager;

    public AssignmentManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.agentManager = new AgentManagerImpl(dataSource);
        this.missionManager = new MissionManagerImpl(dataSource);
    }

    private Date toSQLDate(LocalDate localDate) {
        if (localDate == null) return null;
        return new Date(ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public void createAssignment(Assignment assignment) throws SecretAgencyException {
        try{
            SimpleJdbcInsert insertAssignment = new SimpleJdbcInsert(jdbc).withTableName("assignments").usingGeneratedKeyColumns("assignmentid");
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("agentId", assignment.getAgent().getId())
                    .addValue("missionId", assignment.getMission().getId())
                    .addValue("startDate", toSQLDate(assignment.getStartDate()))
                    .addValue("endDate", toSQLDate(assignment.getEndDate()))
                    .addValue("payment", assignment.getPayment());
            Number id = insertAssignment.executeAndReturnKey(parameters);
            assignment.setId(id.longValue());
        }
        catch (Exception ex){
            throw new SecretAgencyException("did not create assignment");
        }
    }

    @Override
    public void updateAssignment(Assignment assignment) throws SecretAgencyException{
        log.debug("updateAssignment({})", assignment);
        if (assignment.getId() == null) throw new SecretAgencyException("assignment id is null");
        int n = jdbc.update("UPDATE ASSIGNMENTS SET agentId = ?, missionId = ?, payment = ?, startDate = ? , endDate = ? WHERE assignmentId = ?",
                assignment.getAgent().getId(),
                assignment.getMission().getId(),
                assignment.getPayment(),
                assignment.getStartDate() == null ? null : toSQLDate(assignment.getStartDate()),
                assignment.getEndDate() == null ? null : toSQLDate(assignment.getEndDate()),
                assignment.getId());
        if (n != 1) throw new SecretAgencyException("assignment " + assignment + " not updated");
    }

    @Override
    public void deleteAssignment(Assignment assignment) throws SecretAgencyException{
        log.debug("deleteAssignment({})", assignment);
        if (assignment == null) throw new SecretAgencyException("assignment is null");
        if (assignment.getId() == null) throw new SecretAgencyException("assignment id is null");
        int n = jdbc.update("DELETE FROM assignments WHERE assignmentId = ?", assignment.getId());
        if (n != 1) throw new SecretAgencyException("assignment " + assignment + " not deleted");
    }

    @Override
    public List<Assignment> getAllAssignmentsForAgent(Agent agent) {
        return jdbc.query("SELECT * FROM assignments WHERE agentId=?",
                (rs, rowNum) -> {
                    long missionId = rs.getLong("missionId");
                    Mission mission = null;
                    try {
                        mission = missionManager.getMissionById(missionId);
                    } catch (SecretAgencyException ex) {
                        log.error("cannot find agent", ex);
                    }
                    Date startDate1 = rs.getDate("startDate");
                    LocalDate startDate = startDate1 == null ? null: startDate1.toLocalDate();
                    Date endDate1 = rs.getDate("endDate");
                    LocalDate endDate = endDate1 == null ? null: endDate1.toLocalDate();
                    return new Assignment(rs.getLong("assignmentId"), agent, mission, rs.getDouble("payment"), startDate, endDate);
                },
                agent.getId());
    }

    public  RowMapper<Assignment> assignmentMapper = (rs, rowNum) -> {
        Agent agent = agentManager.getAgentById(rs.getLong("agentId"));
        Mission mission = null;
        try {
            mission = missionManager.getMissionById(rs.getLong("missionId"));
        } catch (SecretAgencyException e) {
            e.printStackTrace();
        }
        String startDate = rs.getString("startDate");
        String endDate = rs.getString("endDate");
        return new Assignment(
                rs.getLong("assignmentId"),
                agent,
                mission,
                rs.getDouble("payment"),
                startDate == null ? null : LocalDate.parse(startDate),
                endDate == null ? null : LocalDate.parse(endDate));
    };

    @Override
    public List<Assignment> getAllAssignmentsForMission(Mission mission) throws SecretAgencyException{
        log.debug("getAllAssignmentsForMission({})", mission);
        if (mission == null) throw new SecretAgencyException("mission is null");
        if (mission.getId() == null) throw new SecretAgencyException("mission id is null");
        return jdbc.query(
                "SELECT * FROM assignments WHERE missionId = ?",assignmentMapper, mission.getId());
    }

    @Override
    public Assignment getAssignment(Agent agent, Mission mission) throws SecretAgencyException{
        log.debug("getAssignment({})", agent, mission);
        if (agent == null || mission == null) throw new SecretAgencyException("id is null");
        List<Assignment> list = jdbc.query("SELECT * FROM assignments WHERE missionId = ? AND agentId=?", assignmentMapper, mission.getId(), agent.getId());
        return list.isEmpty() ? null : list.get(0);
    }

    private MissionStatus missionStatusFromString(String str){
        if(str.equals("WAITING")) return MissionStatus.WAITING;
        if(str.equals("ONGOING")) return MissionStatus.ONGOING;
        if(str.equals("FINISHED")) return MissionStatus.FINISHED;
        return MissionStatus.FAILED;
    }

    public RowMapper<Mission> missionMapper = (rs, rowNum) -> {
        String startTime = rs.getString("startTime");
        String endTime = rs.getString("endTime");
        String maxEndTime = rs.getString("maxEndTime");
        return new Mission(
                rs.getLong("id"),
                rs.getString("missionname"),
                rs.getString("location"),
                startTime == null ? null : LocalDate.parse(startTime),
                endTime == null ? null : LocalDate.parse(endTime),
                maxEndTime == null ? null : LocalDate.parse(maxEndTime),
                rs.getString("description"),
                missionStatusFromString(rs.getString("status")));
    };

    @Override
    public List<Mission> getUnassignedMissions() {
        log.debug("getAllAgents({})");
        return jdbc.query("SELECT * FROM missions WHERE id NOT IN (SELECT missionId FROM assignments)", missionMapper);
    }

    private AgentStatus agentStatusFromString(String str){
        if (str.equals("DECEASED")){
            return AgentStatus.DECEASED;
        }
        if(str.equals("ACTIVE")){
            return AgentStatus.ACTIVE;
        }
        return AgentStatus.INACTIVE;
    }

    public RowMapper<Agent> agentMapper = (rs, rowNum) ->
            new Agent(rs.getLong("id"), rs.getString("codename"),
                    rs.getString("contact"), rs.getString("note"),
                    agentStatusFromString(rs.getString("status")));

    @Override
    public List<Agent> getAvailableAgents() {
        log.debug("getAvailableAgents({})");
        return jdbc.query("SELECT * FROM agents WHERE id NOT IN (SELECT agentId FROM (SELECT * FROM assignments WHERE missionId IN (SELECT id FROM missions WHERE status = 'ONGOING')))", agentMapper);
    }

    @Override
    public List<Assignment> getAllAssignments() throws SecretAgencyException {
        log.debug("getAllAgents({})");
        return jdbc.query("SELECT * FROM assignments", assignmentMapper);
    }
}
