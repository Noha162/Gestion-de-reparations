package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.metier.interfaces.IReparationMetier;

import java.util.List;

public class ReparationController {

    private final IReparationMetier metier;

    public ReparationController(IReparationMetier metier) {
        this.metier = metier;
    }

    public List<Reparation> listerPourReparateur(Long reparateurId) {
        return metier.listerPourReparateur(reparateurId);
    }

	public Reparation creerReparationComplete(Long id, Long id2, String text, String text2, String text3, String text4,
			String text5, String text6, String text7, String text8, String text9, double double1, String text10) {
		// TODO Auto-generated method stub
		return null;
	}

    // méthodes existantes : creerReparationComplete(), etc.
}
