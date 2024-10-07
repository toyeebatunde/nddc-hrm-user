package co.payrail.attendance_srv.auth.repository;

import co.payrail.attendance_srv.auth.entity.Roles;
import co.payrail.attendance_srv.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findUserByUserName(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByFirstNameOrLastNameOrEmail(String firstName, String lastName, String email);

    Optional<User> findFirstByUserNameIgnoreCaseOrEmailIgnoreCase (String  userName, String email);

    List<User> findByFirstNameOrLastName(String firstname, String lastname);

    Optional<User> findByRole(Roles role);

    Page<User> findByParent(Long parent, Pageable pageable);
}
