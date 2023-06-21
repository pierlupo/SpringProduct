package com.spring.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.product.entity.Produit;
import com.spring.product.service.ProduitService;
import com.spring.product.service.impl.IProduitService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Controller
@RequestMapping("/product")
public class ProductWithViewController {

    private Produit produit;

    @Autowired
    private ProduitService produitService;


    @GetMapping("/view")
    public ModelAndView getAllProduit() {
        ModelAndView modelAndView = new ModelAndView();
        if (produitService.findAll().isEmpty()) {
            modelAndView.setViewName("error");
        } else {
            modelAndView.setViewName("produits");
            modelAndView.addObject("produits", produitService.findAll());
        }
        return modelAndView;
    }

    public ProductWithViewController(IProduitService produitService) {
        this.produitService = (ProduitService) produitService;
    }

    @GetMapping("/all")
    public String getAllProduits() {
        return produitService.findAll().toString();
    }

//    @DeleteMapping("/delete/{id}")
//    public String deleteProduit(@PathVariable("id") Integer id) {
//        Produit p = produitService.findById(id);
//        if (p != null && produitService.delete(p)) {
//            return "redirect:/product/view";
//        }
//        return "Aucun produit avec cet id";
//    }

    @GetMapping("/delete/{id}")
    public String deleteProduit(@PathVariable("id") Integer id) {
        Produit p = produitService.findById(id);
        if (p != null && produitService.delete(p)) {
            return "redirect:/product/view";
        }
        return "Aucun produit avec cet id";
    }

    @GetMapping("/{id}")
    public Produit getProduitById(@PathVariable Integer id) {
        List<Produit> produits = produitService.findAll();
        Produit produit = new Produit();
        for (Produit p : produits) {
            if (p.getId() == id) {
                produit = p;
            }
        }
        return produit;
    }

    @PostMapping("/post")
    public Produit postOneProduit(@RequestBody Produit produit) {
        produitService.create(produit);
        return produit;
    }

//    @PostMapping("/update/{id}")
//    private void updateProduit(@PathVariable("id") Integer id, @RequestBody Produit produit){
//        Produit p = produitService.findById(id);
//        if(p!=null){
//            p.setMarque(produit.getMarque());
//            p.setReference((produit.getReference()));
//            p.setDateAchat((p.getDateAchat()));
//            p.setPrix(p.getPrix());
//            p.setStock(p.getStock());
//        }
//        produitService.update(p);
//    }

    @GetMapping("/search")
    public String searchProductById(@RequestParam("productId") Integer productId, Model model) {
        Produit produit = produitService.findById(productId);
        model.addAttribute("produit", produit);
        return "product-details";
    }

    @PostMapping("/create")
    public String postProduit(@ModelAttribute Produit produit) {

        System.out.println("produit " + produit);
        if (produit.getId() == null) {
            if (produitService.create(produit)) {
                return "redirect:/product/view";
            }
            return "product/error";

        } else {
            Produit existProduit = produitService.findById(produit.getId());
            if (existProduit != null) {
                existProduit.setDateAchat(produit.getDateAchat());
                existProduit.setMarque(produit.getMarque());
                existProduit.setReference(produit.getReference());
                existProduit.setStock(produit.getStock());
                existProduit.setPrix(produit.getPrix());
                if (produitService.update(existProduit)) {
                    return "redirect:/product/view";
                }
            }

            return "product/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Integer id, Model model) {
        Produit pr = produitService.findById(id);
        System.out.println("pr " + pr);
        model.addAttribute("produit", pr);


        return "formulaire";
    }

    @GetMapping("/form")
    public String afficherFormulaireCreationProduit(Model model) {
        model.addAttribute("produit", new Produit());
        return "formulaire";
    }

    @PostMapping("/update/{id}")
    public Produit updateProduit(@PathVariable("id") Integer id, @RequestBody Produit produit) {
        Produit existProduit = produitService.findById(id);
        if (existProduit != null) {
            existProduit.setDateAchat(produit.getDateAchat());
            existProduit.setMarque(produit.getMarque());
            existProduit.setReference(produit.getReference());
            existProduit.setStock(produit.getStock());
            existProduit.setPrix(produit.getPrix());
            if (produitService.update(existProduit)) {
                return existProduit;
            }
        }
        return existProduit;
    }
}

