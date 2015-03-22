package cz.muni.fi.pv168;

/**
 * Created by sachmet on 11.3.15.
 */
public class Agent implements Comparable{
    private Long id;
    private String codeName;
    private String contact;
    private String note;
    private AgentStatus status;

    public Agent() {
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
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (!codeName.equals(agent.codeName)) return false;
        if (!contact.equals(agent.contact)) return false;
        if (!id.equals(agent.id)) return false;
        if (note != null ? !note.equals(agent.note) : agent.note != null) return false;
        if (status != agent.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + codeName.hashCode();
        result = 31 * result + contact.hashCode();
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + status.hashCode();
        return result;
    }
}
