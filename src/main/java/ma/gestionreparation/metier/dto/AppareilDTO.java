package ma.gestionreparation.metier.dto;

public class AppareilDTO {

    private Long id;
    private String type;
    private String marque;
    private String modele;
    private String clientNom;

    public AppareilDTO(Long id, String type, String marque, String modele, String clientNom) {
        this.id = id;
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.clientNom = clientNom;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public String getClientNom() { return clientNom; }

    @Override
    public String toString() {
        return marque + " " + modele + " (" + clientNom + ")";
    }
}
