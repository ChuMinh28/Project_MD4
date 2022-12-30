package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Users;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByUserName(String userName);
    Users getUsersByUserId(int userID);
    List<Users> searchByUserNameContaining(String name);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}