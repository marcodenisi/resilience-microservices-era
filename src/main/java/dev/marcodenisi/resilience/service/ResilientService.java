package dev.marcodenisi.resilience.service;

import dev.marcodenisi.resilience.model.RemoteCallResult;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResilientService {

    @Autowired private ExternalService externalService;
    @Autowired private CircuitBreaker circuitBreaker;

    public RemoteCallResult getRemote() {
        return Decorators.ofSupplier(externalService::remoteCall)
                .withCircuitBreaker(circuitBreaker)
                .get();
    }

}
