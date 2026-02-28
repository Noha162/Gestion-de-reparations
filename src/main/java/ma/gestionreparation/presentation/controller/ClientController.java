package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.Client;
import ma.gestionreparation.metier.interfaces.IClientMetier;

import java.util.List;

public class ClientController {

    private final IClientMetier metier;

    public ClientController(IClientMetier metier) {
        this.metier = metier;
    }

    public List<Client> lister() {
        return metier.listerClients();
    }

    public Client ajouter(String nom, String prenom, String telephone) {
        return metier.creerClient(nom, prenom, telephone);
    }

    public void supprimer(Long id) {
        metier.supprimerClient(id);
    }
}
