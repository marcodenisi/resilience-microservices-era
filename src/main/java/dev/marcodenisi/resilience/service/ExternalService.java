package dev.marcodenisi.resilience.service;

import dev.marcodenisi.resilience.exception.RemoteCallException;
import dev.marcodenisi.resilience.model.RemoteCallResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class ExternalService {

    private Logger logger = LogManager.getLogger(ExternalService.class);

    public RemoteCallResult remoteCall() {
        SecureRandom random = new SecureRandom();
        if (random.nextBoolean()) {
            logger.error("Ooops, call failed");
            throw new RemoteCallException();
        }

        logger.info("External call made!");
        var uuid = UUID.randomUUID().toString();
        return new RemoteCallResult(uuid, String.format("Message with id %s", uuid));
    }

}
