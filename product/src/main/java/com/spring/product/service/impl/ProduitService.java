package com.spring.product.service.impl;

import com.spring.product.entity.Produit;
import com.spring.product.service.IProduitService;
import com.spring.product.util.ServiceHibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService implements IProduitService {

    @Autowired
    private ServiceHibernate serviceHibernate;

    private Session session;

    public ProduitService(ServiceHibernate serviceHibernate){

        this.serviceHibernate =serviceHibernate;
        session = this.serviceHibernate.getSession();
    }


    @Override
    public boolean create(Produit p) {
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean update(Produit p) {
        session.beginTransaction();
        session.update(p);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean delete(Produit p) {
        session.beginTransaction();
        session.delete(p);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public Produit findById(int id) {
        Produit produit = null;
        produit = (Produit) session.get(Produit.class, id);
        return produit;
    }

    @Override
    public List<Produit> findAll() {
        Query<Produit> produitQuery = session.createQuery("from Produit");
        return produitQuery.list();
    }
}
