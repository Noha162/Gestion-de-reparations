package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.Boutique;
import ma.gestionreparation.dao.User;
import java.time.LocalDate;
import ma.gestionreparation.metier.dto.StatsProprietaireDTO;
import ma.gestionreparation.metier.dto.TopReparateurDTO;
import java.util.List;

public interface IProprietaireMetier {
	
	//* Gestion boutiques
    Boutique creerBoutique(String nom, String adresse, String numeroPatente, User proprietaire);

    List<Boutique> listerBoutiquesDuProprietaire(Long proprietaireId);

    void supprimerBoutique(Long boutiqueId);

    double profitProprietaire(double coutTotal, double pourcentageReparateur);
    
    //* Gestion réparateurs
    java.util.List<User> listerReparateursParBoutique(Long boutiqueId);

    User creerReparateur(String email, String motDePasse, String nom, String prenom, String telephone,
                        Double pourcentage, Long boutiqueId);

    void supprimerReparateur(Long reparateurId);

    //* Statistiques
    StatsProprietaireDTO statsProprietaire(Long proprietaireId, LocalDate dateDebut, LocalDate dateFin);
    List<TopReparateurDTO> topReparateurs(Long proprietaireId, LocalDate d1, LocalDate d2);

}
