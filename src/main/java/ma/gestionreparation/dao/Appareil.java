package ma.gestionreparation.dao;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import ma.gestionreparation.dao.Client;

@Entity
@Table(name = "appareils")
public class Appareil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // téléphone, pc, tablette…

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = true)
    private String ram;

    @Column(nullable = true)
    private String stockage;

    @OneToMany(mappedBy = "appareil", fetch = FetchType.LAZY)
    private List<LigneReparation> lignes = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // getters/setters
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public Appareil() {}

    public Appareil(String type, String marque, String modele, String ram, String stockage) {
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.ram = ram;
        this.stockage = stockage;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }
    public String getStockage() { return stockage; }
    public void setStockage(String stockage) { this.stockage = stockage; }
    public List<LigneReparation> getLignes() { return lignes; }
}
