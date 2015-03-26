package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface MissionManager {
    public void createMission(Mission mission)throws SecretAgencyException;

    public void updateMission(Mission mission)throws SecretAgencyException;

    public void deleteMission(Mission mission) throws SecretAgencyException;

    public List<Mission> getAllMissions() throws SecretAgencyException;

    public List<Mission> getAllMissionsWithStatus(MissionStatus status) throws SecretAgencyException;

    public String getMissionDuration(Mission mission) throws SecretAgencyException;

    public Mission getMissionById(Long id) throws SecretAgencyException;

    public Mission getMissionByName(String name) throws SecretAgencyException;
}
