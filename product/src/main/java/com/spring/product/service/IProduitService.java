package com.spring.product.service;

import com.spring.product.entity.Produit;

import java.util.List;

public interface IProduitService {

    boolean create(Produit p);
    boolean update(Produit p);
    boolean delete(Produit p);

    Produit findById(int id);

    List<Produit> findAll();


}
