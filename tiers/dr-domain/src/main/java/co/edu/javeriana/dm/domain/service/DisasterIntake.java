package co.edu.javeriana.dm.domain.service;

import co.edu.javeriana.dm.domain.model.DisasterMessage;

public interface DisasterIntake {

    void publish(DisasterMessage message);
}
