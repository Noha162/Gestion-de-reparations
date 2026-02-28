package ma.gestionreparation.metier.dto;

public class TopReparateurDTO {
    private Long reparateurId;
    private String nom;
    private String prenom;
    private double chiffreAffaires;

    public TopReparateurDTO(Long reparateurId, String nom, String prenom, double chiffreAffaires) {
        this.reparateurId = reparateurId;
        this.nom = nom;
        this.prenom = prenom;
        this.chiffreAffaires = chiffreAffaires;
    }

    public Long getReparateurId() { return reparateurId; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public double getChiffreAffaires() { return chiffreAffaires; }
}
