package ma.gestionreparation.dao;

import jakarta.persistence.*;
import ma.gestionreparation.dao.enums.TypeCaisse;
import ma.gestionreparation.dao.enums.TypeOperation;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Column(length = 1000)
    private String description;

    private double montant;

    @Enumerated(EnumType.STRING)
    private TypeCaisse typeCaisse;

    @Enumerated(EnumType.STRING)
    private TypeOperation typeOperation;

    @ManyToOne
    @JoinColumn(name = "reparateur_id")
    private User reparateur;

    @ManyToOne
    @JoinColumn(name = "reparation_id")
    private Reparation reparation;

    public Transaction() {
        // constructeur vide obligatoire JPA
    }

    // ================== GETTERS / SETTERS ==================

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public TypeCaisse getTypeCaisse() {
        return typeCaisse;
    }

    public void setTypeCaisse(TypeCaisse typeCaisse) {
        this.typeCaisse = typeCaisse;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public User getReparateur() {
        return reparateur;
    }

    public void setReparateur(User reparateur) {
        this.reparateur = reparateur;
    }

    public Reparation getReparation() {
        return reparation;
    }

    public void setReparation(Reparation reparation) {
        this.reparation = reparation;
    }
}
