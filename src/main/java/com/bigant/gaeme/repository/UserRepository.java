package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthId(String oauthId);

}
