package co.edu.javeriana.dm.registry;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/DisasterReceived"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class DisasterListener implements MessageListener {

    @EJB
    private DisasterRegistry registry;

    @Override
    public void onMessage(Message message) {
        try {
            registry.register(MessageJson.fromJson(((TextMessage) message).getText()));
        } catch (JMSException e) {
            throw new EJBException(e);
        }
    }
}
