package com.connect.ThymeleafCRUD.exception;

public class ClientsNotFoundException extends RuntimeException {
    private static final long serialVersionID = 1L;

    public ClientsNotFoundException(String message){
        super(message);
    }
}
