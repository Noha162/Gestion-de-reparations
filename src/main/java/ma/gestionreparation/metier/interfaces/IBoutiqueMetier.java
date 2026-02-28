package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.metier.dto.BoutiqueDTO;
import java.util.List;

public interface IBoutiqueMetier {

    // EXISTANT (ne pas supprimer)
    List<BoutiqueDTO> lister();

    // ✅ NOUVEAU : pour réparateur
    List<BoutiqueDTO> listerPourReparateur(Long reparateurId);
}
