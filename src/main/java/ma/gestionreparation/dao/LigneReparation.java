package ma.gestionreparation.dao;

import jakarta.persistence.*;
import ma.gestionreparation.dao.enums.EtatAppareil;

@Entity
@Table(name = "ligne_reparation")
public class LigneReparation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reparation_id")
    private Reparation reparation;

    @ManyToOne
    private Appareil appareil;

    @Enumerated(EnumType.STRING)
    private EtatAppareil etatAppareil;

    private double coutAppareil;

    private String commentaire;

    // ===== CONSTRUCTEURS =====
    public LigneReparation() {}

    public LigneReparation(Appareil appareil, EtatAppareil etat) {
        this.appareil = appareil;
        this.etatAppareil = etat;
        this.coutAppareil = 0.0;
    }

    // ===== GETTERS / SETTERS =====
    public Long getId() { return id; }

    public Reparation getReparation() { return reparation; }
    public void setReparation(Reparation reparation) { this.reparation = reparation; }

    public Appareil getAppareil() { return appareil; }
    public void setAppareil(Appareil appareil) { this.appareil = appareil; }

    public EtatAppareil getEtatAppareil() { return etatAppareil; }
    public void setEtatAppareil(EtatAppareil etatAppareil) { this.etatAppareil = etatAppareil; }

    public double getCoutAppareil() { return coutAppareil; }
    public void setCoutAppareil(double coutAppareil) { this.coutAppareil = coutAppareil; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
