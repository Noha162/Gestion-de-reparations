package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.Reparation;

public interface IClientSuiviMetier {

    Reparation findByCodeSuivi(String code);
}
