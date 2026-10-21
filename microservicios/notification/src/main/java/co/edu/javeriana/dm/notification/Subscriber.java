package co.edu.javeriana.dm.notification;

public record Subscriber(Long id, String name, Channel channel, String contact) {

    public static Subscriber of(Long id, String name, Channel channel, String contact) {
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
        return new Subscriber(id, name, channel, contact);
    }
}
