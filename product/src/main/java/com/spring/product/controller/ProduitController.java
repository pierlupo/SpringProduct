package com.spring.product.controller;


import com.spring.product.entity.Produit;
import com.spring.product.service.ProduitService;
import com.spring.product.service.impl.IProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/produit")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    public ProduitController(IProduitService produitService) {
        this.produitService = (ProduitService) produitService;
    }

    @GetMapping("/all")
    public String getAllProduits(){
        return produitService.findAll().toString();
    }

    @DeleteMapping("/delete/{id}")
        public void  getProduit(@PathVariable Integer id){
        Produit p = produitService.findById(id);
        produitService.delete(p);
    }

    @GetMapping("/{id}")
    public Produit getProduitById(@PathVariable Integer id){
        List<Produit> produits = produitService.findAll();
        Produit produit = new Produit();
        for(Produit p:produits){
            if(p.getId()==id){
                produit = p;
            }
        }
        return produit;
    }

    @PostMapping("/post")
    public Produit postOneProduit(@RequestBody Produit produit){
        produitService.create(produit);
        return produit;
    }

    @PostMapping("/update/{id}")
    private void updateProduit(@PathVariable("id") Integer id, @RequestBody Produit produit){
        Produit p = produitService.findById(id);
        if(p!=null){
            p.setMarque(produit.getMarque());
            p.setReference((produit.getReference()));
            p.setDateAchat((p.getDateAchat()));
            p.setPrix(p.getPrix());
            p.setStock(p.getStock());
        }
        produitService.update(p);
    }

}
