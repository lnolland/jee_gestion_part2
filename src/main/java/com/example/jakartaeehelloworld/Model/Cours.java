package com.example.jakartaeehelloworld.Model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cours")
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", referencedColumnName = "id", nullable = true, 
                foreignKey = @ForeignKey(name = "FKfyonv6ewc19w0y7yu5kmleulg"))
    private Enseignant enseignant;


    @ManyToOne
    @JoinColumn(name = "matiere_id", referencedColumnName = "id")
    private Matiere matiere;

    // Ajoutez cette relation pour gérer les inscriptions liées
    @OneToMany(mappedBy = "cours", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Inscription> inscriptions;

    // No-argument constructor
    public Cours() {}

    // Argument constructor
    public Cours(Date dateCours, Enseignant enseignant, Matiere matiere) {
        this.dateCours = dateCours;
        this.enseignant = enseignant;
        this.matiere = matiere;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCours() {
        return dateCours;
    }

    public void setDateCours(Date dateCours) {
        this.dateCours = dateCours;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
}
