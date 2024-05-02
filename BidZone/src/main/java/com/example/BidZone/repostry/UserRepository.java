package com.example.BidZone.repostry;

import com.example.BidZone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String name);
    Boolean existsByUsername(String username);
}
