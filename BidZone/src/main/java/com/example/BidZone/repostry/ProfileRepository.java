package com.example.BidZone.repostry;
import com.example.BidZone.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
}
