package dev.marcodenisi.resilience.controller;

import dev.marcodenisi.resilience.Application;
import dev.marcodenisi.resilience.exception.RemoteCallException;
import dev.marcodenisi.resilience.model.RemoteCallResult;
import dev.marcodenisi.resilience.service.ExternalService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
class ResilientControllerTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private CircuitBreaker circuitBreaker;

    @MockBean private ExternalService externalService;

    @BeforeEach
    public void setUp() {
        circuitBreaker.reset();
    }

    @Test
    public void shouldRemainInClosedState() {
        when(externalService.remoteCall()).thenReturn(new RemoteCallResult("id", "message"));

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
        Stream.rangeClosed(1, 20).forEach(__ ->
                assertStatusCodeAndCircuitBreakerState(HttpStatus.OK, CircuitBreaker.State.CLOSED)
        );
        verify(externalService, times(20)).remoteCall();
    }

    @Test
    public void shouldOpenAfterExceptions() throws InterruptedException {
        doThrow(RemoteCallException.class).when(externalService).remoteCall();

        // let's open the circuit breaker
        Stream.rangeClosed(1, 9).forEach(__ ->
                assertStatusCodeAndCircuitBreakerState(HttpStatus.BAD_GATEWAY, CircuitBreaker.State.CLOSED)
        );
        // 10th call will open the circuit
        assertStatusCodeAndCircuitBreakerState(HttpStatus.BAD_GATEWAY, CircuitBreaker.State.OPEN);

        // subsequent calls won't call the external service
        Stream.rangeClosed(1, 9).forEach(__ ->
                assertStatusCodeAndCircuitBreakerState(HttpStatus.SERVICE_UNAVAILABLE, CircuitBreaker.State.OPEN)
        );

        // wait for the breaker to be HALF_OPEN
        TimeUnit.SECONDS.sleep(30);
        assertStatusCodeAndCircuitBreakerState(HttpStatus.BAD_GATEWAY, CircuitBreaker.State.HALF_OPEN);

        verify(externalService, times(11)).remoteCall();
    }

    private void assertStatusCodeAndCircuitBreakerState(HttpStatus httpStatus,
                                                        CircuitBreaker.State circuitBreakerState) {
        var response = restTemplate.getForEntity("/remote", String.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(httpStatus);
        assertThat(circuitBreaker.getState()).isEqualTo(circuitBreakerState);
    }
}