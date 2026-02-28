package ma.gestionreparation.presentation.ui.util;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.metier.interfaces.IAuthMetier;
import ma.gestionreparation.presentation.controller.LoginController;
import ma.gestionreparation.presentation.ui.FrameLogin;
import ma.gestionreparation.presentation.ui.FrameProprietaire;
import ma.gestionreparation.presentation.ui.FrameReparateur;

import javax.swing.*;

public class AppNavigator {

    private final IAuthMetier authMetier;

    public AppNavigator(IAuthMetier authMetier) {
        this.authMetier = authMetier;
    }

    public void showLogin() {
        SwingUtilities.invokeLater(() -> {
            LoginController controller = new LoginController(authMetier);
            FrameLogin f = new FrameLogin(controller, this);
            f.setVisible(true);
        });
    }

    public void showProprietaire(User u) {
        SwingUtilities.invokeLater(() -> {
            FrameProprietaire f = new FrameProprietaire(u, this);
            f.setVisible(true);
        });
    }

    public void showReparateur(User u) {
        SwingUtilities.invokeLater(() -> {
            FrameReparateur f = new FrameReparateur(u, this);
            f.setVisible(true);
        });
    }
}
