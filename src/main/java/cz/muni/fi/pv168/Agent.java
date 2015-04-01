package cz.muni.fi.pv168;

/**
 * Created by sachmet on 11.3.15.
 */
public class Agent implements Comparable<Agent>{
    private Long id;
    private String codeName;
    private String contact;
    private String note;
    private AgentStatus status;

    public Agent(){
    }

    public Agent(Long id, String codeName, String contact, String note, AgentStatus status) {
        this.id = id;
        this.codeName = codeName;
        this.contact = contact;
        this.note = note;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (codeName != null ? !codeName.equals(agent.codeName) : agent.codeName != null) return false;
        if (contact != null ? !contact.equals(agent.contact) : agent.contact != null) return false;
        if (id != null ? !id.equals(agent.id) : agent.id != null) return false;
        if (note != null ? !note.equals(agent.note) : agent.note != null) return false;
        if (status != agent.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (codeName != null ? codeName.hashCode() : 0);
        result = 31 * result + (contact != null ? contact.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Agent that) {
        return this.getCodeName().compareTo(that.getCodeName());
    }
}
