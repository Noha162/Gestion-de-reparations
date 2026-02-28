package ma.gestionreparation.metier.interfaces;

import java.util.List;

import ma.gestionreparation.dao.Reparation;

public interface IReparationMetier {

    Reparation creerReparationComplete(
            Long reparateurId,
            Long boutiqueId,
            String clientNom,
            String clientPrenom,
            String clientTel,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage,
            String descriptionPanne,
            double cout,
            String commentaire
    );
    List<Reparation> listerPourReparateur(Long reparateurId);
}
