package cz.muni.fi.pv168;


import java.time.LocalDate;

/**
 * Created by sachmet on 11.3.15.
 */
public class Assignment {
    private Agent agent;
    private Mission mission;
    private Double payment;
    private LocalDate startDate;
    private LocalDate endDate;

    public Assignment() {
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
}
