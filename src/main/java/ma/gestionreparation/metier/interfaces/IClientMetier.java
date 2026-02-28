package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.Client;

import java.util.List;

public interface IClientMetier {

    List<Client> listerClients();

    Client creerClient(String nom, String prenom, String telephone);

    void supprimerClient(Long clientId);
}
