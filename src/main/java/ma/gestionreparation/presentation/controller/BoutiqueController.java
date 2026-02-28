package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.metier.dto.BoutiqueDTO;
import ma.gestionreparation.metier.interfaces.IBoutiqueMetier;

import java.util.List;

public class BoutiqueController {

    private final IBoutiqueMetier metier;

    public BoutiqueController(IBoutiqueMetier metier) {
        this.metier = metier;
    }

    // EXISTANT (si utilisé ailleurs)
    public List<BoutiqueDTO> lister() {
        return metier.lister();
    }

    // ✅ NOUVEAU
    public List<BoutiqueDTO> listerPourReparateur(Long reparateurId) {
        return metier.listerPourReparateur(reparateurId);
    }
}
