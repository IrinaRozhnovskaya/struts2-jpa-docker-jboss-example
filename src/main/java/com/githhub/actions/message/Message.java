package com.githhub.actions.message;

import javax.persistence.*;
import java.util.Date;

import static com.githhub.actions.message.Message.FIND_ALL;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "messages")
@NamedQueries({
        @NamedQuery(name = FIND_ALL, query = "SELECT m FROM Message m ORDER BY m.createdAt DESC"),
})
public class Message implements Comparable<Message> {

    public static final String FIND_ALL = "Message.findAll";

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "sender")
    private String from;
    private String body;

    @Temporal(TIMESTAMP)
    private Date createdAt;

    protected Message() { }

    public Message(final String from, final String body) {
        this.from = from;
        this.body = body;
    }

    public Message(final String from, final String body, Date createdAt) {
        this.from = from;
        this.body = body;
        this.createdAt = createdAt;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(final Message other) {
        return -this.createdAt.compareTo(other.getCreatedAt());
    }

    @Override
    public String toString() {
        return "Message{" +
                ", from='" + from + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @PrePersist
    public void onSave() {
        createdAt = new Date();
    }
}
