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

    public Assignment() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if (!agent.equals(that.agent)) return false;
        if (!endDate.equals(that.endDate)) return false;
        if (!mission.equals(that.mission)) return false;
        if (!payment.equals(that.payment)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = agent.hashCode();
        result = 31 * result + mission.hashCode();
        result = 31 * result + payment.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }
}
