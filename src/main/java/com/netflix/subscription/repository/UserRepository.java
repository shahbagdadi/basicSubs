package com.netflix.subscription.repository;

import com.netflix.subscription.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{

}
