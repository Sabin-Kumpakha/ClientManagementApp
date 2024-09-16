package com.connect.ThymeleafCRUD.controller;

import com.connect.ThymeleafCRUD.model.Client;
import com.connect.ThymeleafCRUD.model.ClientDto;
import com.connect.ThymeleafCRUD.service.ClientsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientsController {

    @Autowired
    private ClientsService clientService;

    @GetMapping({"", "/"})
    public String getClients(Model model) {
        var clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "clients/index";
    }

    @GetMapping("/create")
    public String createClient(Model model) {
        model.addAttribute("clientDto", new ClientDto());
        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute ClientDto clientDto, BindingResult result) {
        if (clientService.isEmailTaken(clientDto.getEmail())) {
            result.addError(new FieldError("clientDto", "email", clientDto.getEmail(), false, null, null, "Email address is already used"));
        }

        if (result.hasErrors()) {
            return "clients/create";
        }

        clientService.saveClient(clientDto);
        return "redirect:/clients";
    }

    @GetMapping("/edit")
    public String editClient(Model model, @RequestParam int id) {
        Optional<Client> clientOptional = clientService.getClientById(id);
        if (clientOptional.isEmpty()) {
            return "redirect:/clients";
        }

        Client client = clientOptional.get();
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhone(client.getPhone());
        clientDto.setAddress(client.getAddress());
        clientDto.setStatus(client.getStatus());

        model.addAttribute("client", client);
        model.addAttribute("clientDto", clientDto);

        return "clients/edit";
    }

    @PostMapping("/edit")
    public String editClient(@RequestParam int id, @Valid @ModelAttribute ClientDto clientDto, BindingResult result) {
        Optional<Client> clientOptional = clientService.getClientById(id);
        if (clientOptional.isEmpty()) {
            return "redirect:/clients";
        }

        if (result.hasErrors()) {
            return "clients/edit";
        }

        try {
            clientService.updateClient(id, clientDto, result);
        } catch (RuntimeException ex) {
            return "clients/edit";
        }

        return "redirect:/clients";
    }

    @GetMapping("/delete")
    public String deleteClient(@RequestParam int id) {
        try {
            clientService.deleteClient(id);
        } catch (RuntimeException ex) {
            // handle exception if needed
        }
        return "redirect:/clients";
    }
}
