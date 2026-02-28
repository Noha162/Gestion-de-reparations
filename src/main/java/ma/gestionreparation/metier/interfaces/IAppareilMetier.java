package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.Appareil;

import java.util.List;

public interface IAppareilMetier {

    List<Appareil> lister();

    Appareil findById(Long id);

    void creerPourClient(
            Long clientId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    );

    void modifier(
            Long appareilId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    );

    void supprimer(Long appareilId);
}
