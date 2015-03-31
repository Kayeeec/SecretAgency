package cz.muni.fi.pv168;

/**
 * Created by sachmet on 11.3.15.
 */
public class Agent {
    private Long id;
    private String codeName;
    private String contact;
    private String note;
    private AgentStatus status;

    public Agent() {
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
}
