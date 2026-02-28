package ma.gestionreparation.metier.interfaces;

import ma.gestionreparation.dao.User;

public interface IAuthMetier {
    User login(String email, String motDePasse);
}
