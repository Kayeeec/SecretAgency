package cz.muni.fi.pv168;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.LocalDate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object.*;


/**
 * Created by sachmet on 11.3.15.
 */
public class MissionManagerImpl implements MissionManager {

    final static Logger log = LoggerFactory.getLogger(MissionManagerImpl.class);

    private final DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createMission(Mission mission) {

    }

    @Override
    public void updateMission(Mission mission) {

    }

    @Override
    public void deleteMission(Mission mission) throws SecretAgencyException {
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement st = conn.prepareStatement("DELETE FROM grave WHERE id=?")) {
                st.setLong(1,mission.getId());
                if(st.executeUpdate()!=1) {
                    throw new SecretAgencyException("did not delete mission with id =" + mission.getId());
                }
            }
        } catch (SQLException ex) {
            log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }

    public static final DateTimeZone jodaTzUTC = DateTimeZone.forID("UTC");

    public static LocalDate dateToLocalDate(java.sql.Date d) {
        if(d==null) return null;
        return new LocalDate(d.getTime(), jodaTzUTC);
    }


    public static java.sql.Date localdateToDate(LocalDate ld) {
        if(ld==null) return null;
        return new java.sql.Date(
                ld.toDateTimeAtStartOfDay(jodaTzUTC).getMillis());
    }

    private Mission resultSetToMission(ResultSet rs) throws SQLException {
        Mission mission = new Mission();
        mission.setId(rs.getLong("id"));
        mission.setName(rs.getString("name"));
        mission.setLocation(rs.getString("location"));
        mission.setStartTime(dateToLocalDate(rs.getDate("startTime")));
        mission.setEndTime(dateToLocalDate(rs.getDate("note")));
        mission.setMaxEndTime(dateToLocalDate(rs.getDate("maxEndTime")));
        return mission;
    }

    @Override
    public List<Mission> getAllMissions() throws SecretAgencyException{
        log.debug("finding all missions");
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id, \name, location, startTime, endTime, maxEndTime, description, status FROM missions")) {
                ResultSet rs = st.executeQuery();
                List<Mission> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToMission(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }

    @Override
    public List<Mission> getAllMissionsWithStatus(MissionStatus status) throws SecretAgencyException{
        log.debug("finding all missions");
        String stringStatus = status.toString();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id, \name, location, startTime, endTime, maxEndTime, description, status FROM missions WHERE status = ?")) {
                st.setString(1, stringStatus);
                ResultSet rs = st.executeQuery();
                List<Mission> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToMission(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }

    @Override
    public String getMissionDuration(Mission mission) throws SecretAgencyException{
        log.debug("finding all missions");
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT startTime, endTime, maxEndTime FROM missions WHERE id = ?")) {
                st.setLong(1, mission.getId());
                ResultSet rs = st.executeQuery();
                return "starts: " + rs.getString("startTime") + " ends: " + rs.getString("endTime") + " must end until:" + rs.getString("maxEndTime");
            }
        } catch (SQLException ex) {
            log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }

    @Override
    public Mission getMissionById(Long id) {
        return null;
    }

    @Override
    public Mission getMissionByName(String name) {
        return null;
    }
}
