package ma.gestionreparation.presentation;

import ma.gestionreparation.metier.impl.AuthMetierImpl;
import ma.gestionreparation.metier.interfaces.IAuthMetier;
import ma.gestionreparation.presentation.ui.util.AppNavigator;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;

public class MainUI {
    public static void main(String[] args) {
        UiTheme.applyBaseLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            IAuthMetier auth = new AuthMetierImpl();
            AppNavigator nav = new AppNavigator(auth);
            nav.showLogin();
        });
    }
}
