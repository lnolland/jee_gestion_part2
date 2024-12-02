package com.example.jakartaeehelloworld.Model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "matieres")
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToMany(mappedBy = "matieres")
    private List<Enseignant> enseignants;

    // No-argument constructor
    public Matiere() {}

    // Argument constructor
    public Matiere(String nom) {
        this.nom = nom;
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

    public List<Enseignant> getEnseignants() {
        return enseignants;
    }

    public void setEnseignants(List<Enseignant> enseignants) {
        this.enseignants = enseignants;
    }
}
