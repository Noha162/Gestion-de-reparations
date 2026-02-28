package ma.gestionreparation.dao;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boutiques")
public class Boutique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false, unique = true)
    private String numeroPatente;

    // Propriétaire de la boutique
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private User proprietaire;

    // Réparateurs rattachés à la boutique
    @OneToMany(mappedBy = "boutique", fetch = FetchType.LAZY)
    private List<User> reparateurs = new ArrayList<>();

    // Réparations de la boutique
    @OneToMany(mappedBy = "boutique", fetch = FetchType.LAZY)
    private List<Reparation> reparations = new ArrayList<>();

    @Override
    public String toString() {
        return nom; // UNIQUEMENT des champs simples
    }

    public Boutique() {}

    public Boutique(String nom, String adresse, String numeroPatente, User proprietaire) {
        this.nom = nom;
        this.adresse = adresse;
        this.numeroPatente = numeroPatente;
        this.proprietaire = proprietaire;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getNumeroPatente() { return numeroPatente; }
    public void setNumeroPatente(String numeroPatente) { this.numeroPatente = numeroPatente; }
    public User getProprietaire() { return proprietaire; }
    public void setProprietaire(User proprietaire) { this.proprietaire = proprietaire; }
    public List<User> getReparateurs() { return reparateurs; }
    public List<Reparation> getReparations() { return reparations; }
}
