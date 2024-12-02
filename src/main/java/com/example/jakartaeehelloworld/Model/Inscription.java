package com.example.jakartaeehelloworld.Model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "inscriptions")
public class Inscription {

    @EmbeddedId
    private InscriptionId id;

    @ManyToOne
    @MapsId("etudiantId")
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @MapsId("coursId")
    @JoinColumn(name = "cours_id")
    private Cours cours;
    
    // No-argument constructor
    public Inscription() {}

    // Argument constructor
    public Inscription(Etudiant etudiant, Cours cours) {
        this.etudiant = etudiant;
        this.cours = cours;
        this.id = new InscriptionId(etudiant.getId(), cours.getId());
    }

    // Getters et Setters
    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public InscriptionId getId() {
        return id;
    }

    public void setId(InscriptionId id) {
        this.id = id;
    }
}

@Embeddable
class InscriptionId implements Serializable {
    private Long etudiantId;
    private Long coursId;

    // No-argument constructor
    public InscriptionId() {}

    // Argument constructor
    public InscriptionId(Long etudiantId, Long coursId) {
        this.etudiantId = etudiantId;
        this.coursId = coursId;
    }

    // Getters et Setters
    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coursId, etudiantId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InscriptionId other = (InscriptionId) obj;
        return Objects.equals(coursId, other.coursId) && Objects.equals(etudiantId, other.etudiantId);
    }

    @Override
    public String toString() {
        return "InscriptionId [etudiantId=" + etudiantId + ", coursId=" + coursId + "]";
    }
}
