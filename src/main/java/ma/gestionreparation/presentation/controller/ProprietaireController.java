package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.Boutique;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.metier.interfaces.IProprietaireMetier;

import java.util.List;
import java.time.LocalDate;
import ma.gestionreparation.metier.dto.StatsProprietaireDTO;
import ma.gestionreparation.metier.dto.TopReparateurDTO;

public class ProprietaireController {

    private final IProprietaireMetier metier;

    
  //* Gestion propriétaires
    public ProprietaireController(IProprietaireMetier metier) {
        this.metier = metier;
    }

    public List<Boutique> listerBoutiques(Long proprietaireId) {
        return metier.listerBoutiquesDuProprietaire(proprietaireId);
    }

    public Boutique ajouterBoutique(String nom, String adresse, String patente, User proprietaire) {
        return metier.creerBoutique(nom, adresse, patente, proprietaire);
    }

    public void supprimerBoutique(Long boutiqueId) {
        metier.supprimerBoutique(boutiqueId);
    }
    
    //* Gestion réparateurs
    public List<User> listerReparateursParBoutique(Long boutiqueId) {
        return metier.listerReparateursParBoutique(boutiqueId);
    }

    public User ajouterReparateur(String email, String motDePasse, String nom, String prenom, String telephone,
                                 Double pourcentage, Long boutiqueId) {
        return metier.creerReparateur(email, motDePasse, nom, prenom, telephone, pourcentage, boutiqueId);
    }

    public void supprimerReparateur(Long reparateurId) {
        metier.supprimerReparateur(reparateurId);
    }

    
    public StatsProprietaireDTO stats(Long proprietaireId, LocalDate d1, LocalDate d2) {
        return metier.statsProprietaire(proprietaireId, d1, d2);
    }
    
    public List<TopReparateurDTO> topReparateurs(Long proprietaireId, LocalDate d1, LocalDate d2) {
        return metier.topReparateurs(proprietaireId, d1, d2);
}
    
}
