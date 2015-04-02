package cz.muni.fi.pv168;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public class MissionManagerImpl implements MissionManager {
    private final DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String missionStatusToString(MissionStatus status){
        if(status.equals(MissionStatus.WAITING)) return "WAITING";
        else if(status.equals(MissionStatus.ONGOING)) return "ONGOING";
        else if(status.equals(MissionStatus.FINISHED)) return "FINISHED";
        else return "FAILED";
    }

    private MissionStatus missionStatusFromString(String str){
        if(str.equals("WAITING")) return MissionStatus.WAITING;
        else if(str.equals("ONGOING")) return MissionStatus.ONGOING;
        else if(str.equals("FINISHED")) return MissionStatus.FINISHED;
        else return MissionStatus.FAILED;
    }

    @Override
    public void createMission(Mission mission) throws SQLException{
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement st = con.prepareStatement
                    ("INSERT INTO missions (missionname,location,startTime,endTime,maxEndTime,description,status) VALUES (?,?,?,?,?,?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                st.setString(1, mission.getName());
                st.setString(2, mission.getLocation());

                LocalDate sdate = mission.getStartTime();
                LocalDate edate = mission.getEndTime();
                LocalDate mdate = mission.getMaxEndTime();
                st.setObject(3, sdate == null ? null : sdate.toString(), Types.DATE);
                st.setObject(4, edate == null ? null : edate.toString(), Types.DATE);
                st.setObject(5, mdate == null ? null : mdate.toString(), Types.DATE);

                st.setString(6, mission.getDescription());
                st.setString(7, missionStatusToString(mission.getStatus()));
                st.executeUpdate();

                try (ResultSet keys = st.getGeneratedKeys()) {
                    if (keys.next()) {
                        Long id = keys.getLong(1);
                        mission.setId(id);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new SQLException("Method createMission failed to insert mission into the database.", e);
        }

    }

    @Override
    public void updateMission(Mission mission) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement st = con.prepareStatement("update missions set LOCATION=?, STARTTIME=?, ENDTIME=?, MAXENDTIME=?, DESCRIPTION=?, STATUS=? where id=?")) {
                //st.setString(1, mission.getName()); -- update does not change name
                st.setString(1, mission.getLocation());

                LocalDate sdate = mission.getStartTime();
                LocalDate edate = mission.getEndTime();
                LocalDate mdate = mission.getMaxEndTime();
                st.setObject(2, sdate == null ? null : sdate.toString(), Types.DATE);
                st.setObject(3, edate == null ? null : edate.toString(), Types.DATE);
                st.setObject(4, mdate == null ? null : mdate.toString(), Types.DATE);

                st.setString(5, mission.getDescription());
                st.setString(6, missionStatusToString(mission.getStatus()));
                st.setLong(7, mission.getId());

                int n = st.executeUpdate();
                if (n != 1) {
                    throw new SQLException("Method updateMission() could not update mission with id " + mission.getId());
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Method updateMission() failed to update the database.", e);
        }

    }

    @Override
    public void deleteMission(Mission mission) throws SecretAgencyException {
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement st = conn.prepareStatement("DELETE FROM missions WHERE id=?")) {
                st.setLong(1,mission.getId());
                if(st.executeUpdate()!=1) {
                    throw new SecretAgencyException("did not delete mission with id =" + mission.getId());
                }
            }
        } catch (SQLException ex) {
            //log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }

//    public static final DateTimeZone jodaTzUTC = DateTimeZone.forID("UTC");
//
//    public static LocalDate dateToLocalDate(java.sql.Date d) {
//        if(d==null) return null;
//        return new LocalDate(d.getTime(), jodaTzUTC);
//    }
//
//
//    public static java.sql.Date localdateToDate(LocalDate ld) {
//        if(ld==null) return null;
//        return new java.sql.Date(
//                ld.toDateTimeAtStartOfDay(jodaTzUTC).getMillis());
//    }

    private Mission resultSetToMission(ResultSet rs) throws SQLException {
        Mission mission = new Mission();
        mission.setId(rs.getLong("id"));
        mission.setName(rs.getString("missionname"));
        mission.setLocation(rs.getString("location"));

        Date sdate = rs.getDate("startTime");
        Date edate = rs.getDate("endTime");
        Date mdate = rs.getDate("maxEndTime");
        LocalDate sDate = sdate == null ? null : sdate.toLocalDate();
        LocalDate eDate = edate == null ? null : edate.toLocalDate();
        LocalDate mDate = mdate == null ? null : mdate.toLocalDate();

        mission.setStartTime(sDate);
        mission.setEndTime(eDate);
        mission.setMaxEndTime(mDate);

//        mission.setStartTime(dateToLocalDate(rs.getDate("startTime")));
//        mission.setEndTime(dateToLocalDate(rs.getDate("note")));
//        mission.setMaxEndTime(dateToLocalDate(rs.getDate("maxEndTime")));

        return mission;
    }

    @Override
    public List<Mission> getAllMissions() throws SecretAgencyException{
       // log.debug("finding all missions");
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id, MISSIONNAME, location, startTime, endTime, maxEndTime, description, status FROM missions")) {
                ResultSet rs = st.executeQuery();
                List<Mission> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToMission(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            //log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }

    @Override
    public List<Mission> getAllMissionsWithStatus(MissionStatus status) throws SecretAgencyException{
        //log.debug("finding all missions");
        String stringStatus = status.toString();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id, MISSIONNAME, location, startTime, endTime, maxEndTime, description, status FROM missions WHERE status = ?")) {
                st.setString(1, stringStatus);
                ResultSet rs = st.executeQuery();
                List<Mission> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToMission(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            //log.error("db connection problem", ex);
            throw new SecretAgencyException("Error when retrieving all missions", ex);
        }
    }


    @Override
    public Mission getMissionById(Long id) throws SecretAgencyException {

        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement st = con.prepareStatement("SELECT * FROM missions WHERE id = ?")) {
                st.setLong(1, id);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        Date sdate = rs.getDate("startTime");
                        Date edate = rs.getDate("endTime");
                        Date mdate = rs.getDate("maxEndTime");
                        LocalDate sDate = sdate == null ? null : sdate.toLocalDate();
                        LocalDate eDate = edate == null ? null : edate.toLocalDate();
                        LocalDate mDate = mdate == null ? null : mdate.toLocalDate();

                        return new Mission(rs.getLong("id"), rs.getString("missionname"), rs.getString("location"),
                                sDate, eDate, mDate, rs.getString("description"),
                                missionStatusFromString(rs.getString("status")));
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SecretAgencyException("Method getMissionById(): failed select from database.", e);
        }
    }

    @Override
    public Mission getMissionByName(String name) throws SQLException{
        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement st = con.prepareStatement("SELECT * FROM missions WHERE missionname = ?")) {
                st.setString(1, name);
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        Date sdate = rs.getDate("startTime");
                        Date edate = rs.getDate("endTime");
                        Date mdate = rs.getDate("maxEndTime");
                        LocalDate sDate = sdate == null ? null : sdate.toLocalDate();
                        LocalDate eDate = edate == null ? null : edate.toLocalDate();
                        LocalDate mDate = mdate == null ? null : mdate.toLocalDate();

                        return new Mission(rs.getLong("id"), rs.getString("missionname"), rs.getString("location"),
                                sDate, eDate, mDate, rs.getString("description"),
                                missionStatusFromString(rs.getString("status")));
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Method getMissionByName(): failed select from database.", e);
        }
    }
}
