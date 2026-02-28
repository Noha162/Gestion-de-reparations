package ma.gestionreparation.presentation.controller;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.metier.interfaces.IAuthMetier;

public class LoginController {

    private final IAuthMetier authMetier;

    public LoginController(IAuthMetier authMetier) {
        this.authMetier = authMetier;
    }

    public User login(String email, String motDePasse) {
        return authMetier.login(email, motDePasse);
    }
}
