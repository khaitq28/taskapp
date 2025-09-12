package khaitq.rest;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/")
public class MainResource {

    @GetMapping()
    public String hello() {
        return "Welcome to Task Management API";
    }

}
