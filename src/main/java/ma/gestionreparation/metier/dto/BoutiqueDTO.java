package ma.gestionreparation.metier.dto;

public class BoutiqueDTO {

    private final Long id;
    private final String nom;

    public BoutiqueDTO(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom; // Swing-safe
    }
}
