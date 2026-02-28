package ma.gestionreparation.dao;

import jakarta.persistence.*;
import ma.gestionreparation.dao.enums.StatutReparation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reparations")
public class Reparation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codeSuivi;

    private String descriptionPanne;

    private double coutTotal;

    @Enumerated(EnumType.STRING)
    private StatutReparation statut;

    private LocalDateTime dateCreation;

    @ManyToOne
    private User reparateur;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Boutique boutique;

    @OneToMany(
        mappedBy = "reparation",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<LigneReparation> lignes = new ArrayList<>();

    @OneToMany(
        mappedBy = "reparation",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Transaction> transactions = new ArrayList<>();

    // ===== CONSTRUCTEUR =====
    public Reparation() {}

    // ===== MÉTHODES MÉTIER =====
    public void addLigne(LigneReparation lr) {
        lignes.add(lr);
        lr.setReparation(this);
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        t.setReparation(this);
    }

    // ===== GETTERS / SETTERS =====
    public Long getId() { return id; }

    public String getCodeSuivi() { return codeSuivi; }
    public void setCodeSuivi(String codeSuivi) { this.codeSuivi = codeSuivi; }

    public String getDescriptionPanne() { return descriptionPanne; }
    public void setDescriptionPanne(String descriptionPanne) { this.descriptionPanne = descriptionPanne; }

    public double getCoutTotal() { return coutTotal; }
    public void setCoutTotal(double coutTotal) { this.coutTotal = coutTotal; }

    public StatutReparation getStatut() { return statut; }
    public void setStatut(StatutReparation statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public User getReparateur() { return reparateur; }
    public void setReparateur(User reparateur) { this.reparateur = reparateur; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Boutique getBoutique() { return boutique; }
    public void setBoutique(Boutique boutique) { this.boutique = boutique; }

    public List<LigneReparation> getLignes() { return lignes; }

    public List<Transaction> getTransactions() { return transactions; }
}
