package co.edu.javeriana.dm.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriber")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private String contact;

    protected Subscriber() {
    }

    private Subscriber(String name, Channel channel, String contact) {
        this.name = name;
        this.channel = channel;
        this.contact = contact;
    }

    public static Subscriber register(String name, Channel channel, String contact) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name es obligatorio");
        }
        if (channel == null) {
            throw new IllegalArgumentException("channel es obligatorio");
        }
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("contact es obligatorio");
        }
        if (channel == Channel.EMAIL && !contact.contains("@")) {
            throw new IllegalArgumentException("contact no es un correo válido");
        }
        return new Subscriber(name, channel, contact);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Channel getChannel() { return channel; }
    public String getContact() { return contact; }
}
