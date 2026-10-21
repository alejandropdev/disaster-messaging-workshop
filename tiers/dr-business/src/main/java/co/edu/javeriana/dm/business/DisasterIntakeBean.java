package co.edu.javeriana.dm.business;

import co.edu.javeriana.dm.domain.model.DisasterMessage;
import co.edu.javeriana.dm.domain.service.DisasterIntake;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Stateless
public class DisasterIntakeBean implements DisasterIntake {

    @Inject
    @JMSConnectionFactory("java:/JmsXA")
    private JMSContext jms;

    @Resource(lookup = "java:/jms/queue/DisasterReceived")
    private Queue queue;

    @Override
    public void publish(DisasterMessage message) {
        jms.createProducer().send(queue, MessageJson.toJson(message));
    }
}
