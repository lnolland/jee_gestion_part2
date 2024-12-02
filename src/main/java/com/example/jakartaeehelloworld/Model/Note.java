package com.example.jakartaeehelloworld.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "resultats")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private Matiere matiere;

    @Column(name = "note")
    private Float note;

    // No-argument constructor
    public Note() {}

    // Argument constructor
    public Note(Etudiant etudiant, Matiere matiere, Float note) {
        this.etudiant = etudiant;
        this.matiere = matiere;
        this.note = note;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float noteValeur) {
        this.note = noteValeur;
    }
}
