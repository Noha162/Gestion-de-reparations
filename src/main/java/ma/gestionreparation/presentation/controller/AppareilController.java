package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.Appareil;
import ma.gestionreparation.metier.interfaces.IAppareilMetier;

import java.util.List;

public class AppareilController {

    private final IAppareilMetier metier;

    public AppareilController(IAppareilMetier metier) {
        this.metier = metier;
    }

    public List<Appareil> lister() {
        return metier.lister();
    }

    public Appareil findById(Long id) {
        return metier.findById(id);
    }

    public void creerPourClient(
            Long clientId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    ) {
        metier.creerPourClient(clientId, type, marque, modele, ram, stockage);
    }

    public void modifier(
            Long appareilId,
            String type,
            String marque,
            String modele,
            String ram,
            String stockage
    ) {
        metier.modifier(appareilId, type, marque, modele, ram, stockage);
    }

    public void supprimer(Long appareilId) {
        metier.supprimer(appareilId);
    }

	public void creer(Long id, String text, String text2, String text3, String text4, String text5) {
		// TODO Auto-generated method stub
		
	}
}
