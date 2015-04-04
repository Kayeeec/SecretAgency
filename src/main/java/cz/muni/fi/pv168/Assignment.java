package cz.muni.fi.pv168;


import java.time.LocalDate;

/**
 * Created by sachmet on 11.3.15.
 */
public class Assignment {

    private Long id;
    private Agent agent; //non null
    private Mission mission; //non null
    private Double payment; //non null
    private LocalDate startDate; //non null
    private LocalDate endDate; //non null

    public Assignment(Agent agent, Mission mission, Double payment, LocalDate startDate, LocalDate endDate) {
        this.agent = agent;
        this.mission = mission;
        this.payment = payment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Assignment(Long id, Agent agent, Mission mission, Double payment, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.agent = agent;
        this.mission = mission;
        this.payment = payment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", agent=" + agent +
                ", mission=" + mission +
                ", payment=" + payment +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if (agent != null ? !agent.equals(that.agent) : that.agent != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (mission != null ? !mission.equals(that.mission) : that.mission != null) return false;
        if (payment != null ? !payment.equals(that.payment) : that.payment != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (agent != null ? agent.hashCode() : 0);
        result = 31 * result + (mission != null ? mission.hashCode() : 0);
        result = 31 * result + (payment != null ? payment.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
