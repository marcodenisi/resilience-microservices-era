package dev.marcodenisi.resilience.controller;

import dev.marcodenisi.resilience.model.RemoteCallResult;
import dev.marcodenisi.resilience.service.ResilientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResilientController {

    @Autowired private ResilientService resilientService;

    @GetMapping(path = "/remote", produces = "application/json")
    public RemoteCallResult getRemote() {
        return resilientService.getRemote();
    }

}
