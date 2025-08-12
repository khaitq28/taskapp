package khaitq.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    Optional<User> findById(UserId userId);

    void deleteById(UserId userId);

    long count();

}
