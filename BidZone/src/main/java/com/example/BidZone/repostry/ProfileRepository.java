package com.example.BidZone.repostry;

import com.example.BidZone.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepository extends CrudRepository<UserProfile, Long> {
}
