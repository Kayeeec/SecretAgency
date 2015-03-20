package cz.muni.fi.pv168;

import java.util.List;

/**
 * Created by sachmet on 11.3.15.
 */
public interface MissionManager {
    public void createMission(Mission mission);

    public void updateMission(Mission mission);

    public void deleteMission(Mission mission);

    public List<Mission> getAllMissions();

    public List<Mission> getAllMissionsWithStatus(MissionStatus status);

    public String getMissionDuration(Mission mission);

    public Mission getMissionById(Long id);

    public Mission getMissionByName(String name);
}
