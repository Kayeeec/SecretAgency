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

}
