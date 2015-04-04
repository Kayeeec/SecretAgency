package cz.muni.fi.pv168;

//import java.util.Date;
import java.time.LocalDate;


/**
 * Created by sachmet on 11.3.15.
 */
public class Mission implements Comparable<Mission> {
    private Long id;
    private String name;
    private String location = null;
    private LocalDate startTime = null;
    private LocalDate endTime = null;
    private LocalDate maxEndTime = null;
    private String description = null;
    private MissionStatus status = MissionStatus.WAITING;

    public Mission() {
        //sets id
    }

    public Mission(Long id, String name, String location, LocalDate startTime,
                   LocalDate endTime, LocalDate maxEndTime, String description, MissionStatus status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxEndTime = maxEndTime;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public LocalDate getMaxEndTime() {
        return maxEndTime;
    }

    public void setMaxEndTime(LocalDate maxEndTime) {
        this.maxEndTime = maxEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public int compareTo(Mission that){
        Long a = this.getId() - that.getId();
        return a.intValue();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mission mission = (Mission) o;

        if (description != null ? !description.equals(mission.description) : mission.description != null) return false;
        if (endTime != null ? !endTime.equals(mission.endTime) : mission.endTime != null) return false;
        if (id != null ? !id.equals(mission.id) : mission.id != null) return false;
        if (location != null ? !location.equals(mission.location) : mission.location != null) return false;
        if (maxEndTime != null ? !maxEndTime.equals(mission.maxEndTime) : mission.maxEndTime != null) return false;
        if (name != null ? !name.equals(mission.name) : mission.name != null) return false;
        if (startTime != null ? !startTime.equals(mission.startTime) : mission.startTime != null) return false;
        if (status != mission.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (maxEndTime != null ? maxEndTime.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", maxEndTime=" + maxEndTime +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
