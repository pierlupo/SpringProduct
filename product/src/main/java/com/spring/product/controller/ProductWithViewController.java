package com.spring.product.controller;

import com.spring.product.entity.Produit;
import com.spring.product.service.impl.LoginService;
import com.spring.product.service.impl.ProduitService;
import com.spring.product.service.IProduitService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductWithViewController {

    private String location = "upload-dir";

    @Autowired
    private ProduitService produitService;

    @Autowired
    private LoginService _loginService;

    @Autowired
    private HttpServletResponse _response;

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
    @GetMapping("/formupload")
    public ModelAndView form(){
        ModelAndView vm = new ModelAndView("form-upload");
        return vm;
    }

    @GetMapping("files")
    @ResponseBody
    public List<String> getFiles() throws IOException {
        List<String> liste = new ArrayList<>();
        Files.walk(Paths.get(this.location)).forEach(path -> {
            liste.add(path.getFileName().toString());
        });
        return liste;
    }

    @PostMapping("submitForm")
    public String submitForm(@RequestParam("image") MultipartFile image) throws IOException {
        Path destinationFile = Paths.get(location).resolve(Paths.get(image.getOriginalFilename())).toAbsolutePath();
        InputStream stream = image.getInputStream();
        Files.copy(stream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return "redirect:/product/formupload";
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @PostMapping("submit")
    public ModelAndView submitLogin(@RequestParam String login, @RequestParam String password) throws IOException {
        if(_loginService.login(login, password)) {
            _response.sendRedirect("protected");
        }
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

    @GetMapping("protected")
    public String protectedPage() throws IOException {
        if(!_loginService.isLogged()){
            _response.sendRedirect("login");
        }
        return "redirect:/product/view";
    }
}

