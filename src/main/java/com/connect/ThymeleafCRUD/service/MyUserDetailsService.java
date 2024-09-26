package com.connect.ThymeleafCRUD.service;

import com.connect.ThymeleafCRUD.dao.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// custom Myuserdetails implemntation with userdetails
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<MyUser> user = repository.findByUsername(username);        // find by username
        if(user.isPresent()){
            var userObj = user.get();
           return User.builder()                                     // custom User
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();

        } else{
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(MyUser user){
        if(user.getRole() == null){                     // if role is null return USER if not ADMIN, USER
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
