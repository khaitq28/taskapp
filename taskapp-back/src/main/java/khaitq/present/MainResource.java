package khaitq.present;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/")
public class MainResource {

    @GetMapping()
    public String hello() {
        return "OK";
    }


}
