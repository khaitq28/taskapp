package khaitq.bdd;

import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@CucumberContextConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStepDefinitions {

    @LocalServerPort
    private int port;
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private ResponseEntity<String> response;

    @Given("the application is running")
    public void theApplicationIsRunning() {
    }

    @When("I send a GET request to {string}")
    public void iCallTheHiEndpoint(String endpoint) {
        String url = "http://localhost:" + port + endpoint;
        response = restTemplate.getForEntity(url, String.class);
    }

    @Then("I should receive a response {string}")
    public void iShouldReceiveAResponse(String expectedResponse) {
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }
}
