package khaitq.bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainStepDefinitions {

    @LocalServerPort
    private int port;
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private ResponseEntity<String> response;

    @Given("I call the {string} endpoint")
    public void iCallTheMainEndpoint(String endpoint) {
        String url = "http://localhost:" + port + endpoint;
        response = restTemplate.getForEntity(url, String.class);
    }

    @Then("I should receive a response main {string}")
    public void iShouldReceiveAMainResponse(String expectedResponse) {
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }
}
