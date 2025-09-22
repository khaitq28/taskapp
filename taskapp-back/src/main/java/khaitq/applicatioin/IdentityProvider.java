package khaitq.applicatioin;


import khaitq.domain.Identity;
import org.springframework.stereotype.Service;

@Service
public class IdentityProvider {

    public Identity authenticate(String email, String password) {

        return Identity.builder().email("user1@example.com").userId("1234").role("USER").build();
    }
}
