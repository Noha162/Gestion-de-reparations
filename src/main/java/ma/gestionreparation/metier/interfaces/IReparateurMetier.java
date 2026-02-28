package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.*;
import ma.gestionreparation.dao.enums.*;

public interface IReparateurMetier {

    Client creerClient(String nom, String prenom, String telephone);

    Appareil creerAppareilPourClient(
            Long clientId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    );

    Reparation creerReparation(
            String descriptionPanne,
            double coutTotal,
            User reparateur,
            Client client,
            Boutique boutique
    );

    LigneReparation ajouterLigne(
            Reparation reparation,
            Appareil appareil,
            EtatAppareil etat,
            Double cout,
            String commentaire
    );

    Transaction enregistrerTransaction(
            String description,
            double montant,
            TypeOperation op,
            TypeCaisse caisse,
            User reparateur,
            Reparation reparation
    );
}
