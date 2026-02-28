package ma.gestionreparation.dao;

import jakarta.persistence.*;
import ma.gestionreparation.dao.enums.Role;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Nullable si PROPRIETAIRE, obligatoire si REPARATEUR (règle métier)
    @Column(nullable = true)
    private Double pourcentage;

    // Boutique : nullable si PROPRIETAIRE, obligatoire si REPARATEUR
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boutique_id")
    private Boutique boutique;

    // Si PROPRIETAIRE : il possède des boutiques
    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.LAZY)
    private List<Boutique> boutiquesPossedees = new ArrayList<>();

    // Si REPARATEUR : ses réparations
    @OneToMany(mappedBy = "reparateur", fetch = FetchType.LAZY)
    private List<Reparation> reparations = new ArrayList<>();

    // Si REPARATEUR : ses transactions
    @OneToMany(mappedBy = "reparateur", fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public User() {}

    public User(String email, String motDePasse, String nom, String prenom, String telephone, Role role) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Double getPourcentage() { return pourcentage; }
    public void setPourcentage(Double pourcentage) { this.pourcentage = pourcentage; }
    public Boutique getBoutique() { return boutique; }
    public void setBoutique(Boutique boutique) { this.boutique = boutique; }

    public List<Boutique> getBoutiquesPossedees() { return boutiquesPossedees; }
    public List<Reparation> getReparations() { return reparations; }
    public List<Transaction> getTransactions() { return transactions; }
}
