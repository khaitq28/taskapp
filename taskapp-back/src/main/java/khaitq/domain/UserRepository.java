package khaitq.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    Optional<User> findById(long id);

    long count();

    void deleteById(long id);

}
