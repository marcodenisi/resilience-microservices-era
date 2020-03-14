package dev.marcodenisi.resilience.service;

import dev.marcodenisi.resilience.model.RemoteCallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResilientService {

    @Autowired private ExternalService externalService;

    public RemoteCallResult getRemote() {
        return externalService.remoteCall();
    }

}
