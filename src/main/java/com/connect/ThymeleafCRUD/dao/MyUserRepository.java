package com.connect.ThymeleafCRUD.dao;

import com.connect.ThymeleafCRUD.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);

}
