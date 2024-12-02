package com.example.jakartaeehelloworld.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "enseignants")
public class Enseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToMany
    @JoinTable(
        name = "enseignants_matieres",
        joinColumns = @JoinColumn(name = "enseignant_id"),
        inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    private List<Matiere> matieres;
    
    private String prenom;
    
    private String contact;

    // No-argument constructor
    public Enseignant() {}

    // Argument constructor
    public Enseignant(String nom, String prenom, List<Matiere> matieres, String contact) {
        this.nom = nom;
        this.prenom = prenom;
        this.matieres = matieres;
        this.contact = contact;
    }
    
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
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public List<Matiere> getMatieres() {
		return matieres;
	}

	public void setMatieres(List<Matiere> matieres) {
		this.matieres = matieres;
	}

}
