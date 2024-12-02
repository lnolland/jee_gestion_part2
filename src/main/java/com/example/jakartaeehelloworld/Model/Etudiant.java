package com.example.jakartaeehelloworld.Model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "etudiants")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    private String contact;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> resultats;

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions;

    // No-argument constructor
    public Etudiant() {}

    // Argument constructor
    public Etudiant(String nom, String prenom, Date dateNaissance, String contact, List<Inscription> inscriptions, List<Note> resultats) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.contact = contact;
        this.inscriptions = inscriptions;
        this.resultats = resultats;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public List<Note> getResultats() {
        return resultats;
    }

    public void setResultats(List<Note> resultats) {
        this.resultats = resultats;
    }

    public List<Cours> getCours() {
        return inscriptions.stream()
                           .map(Inscription::getCours)
                           .collect(Collectors.toList());
    }
}
