package com.connect.ThymeleafCRUD.repo;

import com.connect.ThymeleafCRUD.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    public Client findByEmail(String email);
}
