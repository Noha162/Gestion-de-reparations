package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.metier.interfaces.IClientSuiviMetier;

public class ClientSuiviController {

    private final IClientSuiviMetier metier;

    public ClientSuiviController(IClientSuiviMetier metier) {
        this.metier = metier;
    }

    public Reparation charger(String code) {
        return metier.findByCodeSuivi(code);
    }
}
