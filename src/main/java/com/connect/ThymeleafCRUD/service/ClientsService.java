package com.connect.ThymeleafCRUD.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.connect.ThymeleafCRUD.entity.Client;
import com.connect.ThymeleafCRUD.entity.ClientDto;
import com.connect.ThymeleafCRUD.dao.ClientRepository;

@Service
public class ClientsService {

    @Autowired
    private ClientRepository clientRepo;

    public Iterable<Client> getAllClients() {
        return clientRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Optional<Client> getClientById(int id) {
        return clientRepo.findById(id);
    }

    public boolean isEmailTaken(String email) {
        return clientRepo.findByEmail(email) != null;
    }

    public void saveClient(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());
        client.setCreatedAt(new Date());

        clientRepo.save(client);
    }

    public void updateClient(int id, ClientDto clientDto, BindingResult result) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));

        if (isEmailTaken(clientDto.getEmail()) && !client.getEmail().equals(clientDto.getEmail())) {
            result.addError(new FieldError("clientDto", "email", clientDto.getEmail(), false, null, null, "Email address is already used"));
            throw new RuntimeException("Email address is already used");
        }

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());

        clientRepo.save(client);
    }

    public void deleteClient(int id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepo.delete(client);
    }
}
