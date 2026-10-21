package co.edu.javeriana.dm.notification;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topic/DisasterRegistered"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "notification-service"),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "disaster-registered")
})
public class DisasterEventListener implements MessageListener {

    private static final Jsonb JSONB = JsonbBuilder.create();

    @EJB
    private NotificationService notifications;

    @Override
    public void onMessage(Message message) {
        try {
            notifications.notifyAll(JSONB.fromJson(((TextMessage) message).getText(), DisasterEvent.class));
        } catch (JMSException e) {
            throw new EJBException(e);
        }
    }
}
