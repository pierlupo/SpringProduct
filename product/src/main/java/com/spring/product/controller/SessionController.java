package com.spring.product.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Arrays;
import java.util.List;

public class SessionController {

    @Autowired
    private HttpSession _httpsession;


    @GetMapping("ecrire")
    public String write() {
        _httpsession.setMaxInactiveInterval(60);
        _httpsession.setAttribute("name", "mohamed");
        return "OK";

    }


    @GetMapping("ecrire-list")
    public String writeList() {
        List<String> liste = Arrays.asList("Karim", "Paul", "Pierre");
        _httpsession.setMaxInactiveInterval(30);
        _httpsession.setAttribute("liste", liste);
        return "OK";

    }


    @GetMapping("lire")
    public String read() {
        return _httpsession.getAttribute("name").toString();
    }


    @GetMapping("lire-list")
    public List<String> readList() {
        return (List<String>) _httpsession.getAttribute("liste");
    }



    @GetMapping("remove")
    public String remove(){
        _httpsession.removeAttribute("name");
        return "Supression Ok";
    }
}
