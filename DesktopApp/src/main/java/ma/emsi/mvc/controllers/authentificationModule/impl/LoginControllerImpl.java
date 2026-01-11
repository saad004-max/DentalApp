package ma.emsi.mvc.controllers.authentificationModule.impl;

import java.awt.*;
import javax.swing.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ma.emsi.common.consoleLog.ConsoleLogger;
import ma.emsi.config.ApplicationContext;
import ma.emsi.mvc.controllers.dashboardModule.api.DashboardController;
import ma.emsi.mvc.controllers.authentificationModule.api.LoginController;
import ma.emsi.mvc.dto.authentificationDtos.AuthRequest;
import ma.emsi.mvc.dto.authentificationDtos.AuthResult;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.frames.LoginUI;
import ma.emsi.mvc.ui.palette.alert.Alert;
import ma.emsi.service.authentificationService.api.AuthService;


@Getter @Setter
@AllArgsConstructor
public class LoginControllerImpl implements LoginController {

   private final AuthService service;
   private LoginUI view;

    public LoginControllerImpl(AuthService service) {
        this.service = service;
    }

    @Override
    public void showLoginView() {
        ConsoleLogger.info("Call for Login View...");
        // EDT Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            if (view == null) view = new LoginUI(this);
            view.clearErrors();
            view.setVisible(true);
        });
    }

    @Override
    public void onLoginRequested(String login, String password) {

        String lg = (login == null) ? "" : login.trim();
        String pw = (password == null) ? "" : password;

        AuthResult result = service.authenticate(new AuthRequest(lg, pw));

        // 1 - erreurs de validation (formulaire)
        if (!result.getFieldErrors().isEmpty()) {
            view.showFieldErrors(result.getFieldErrors());
            return;
        }

        // 2 - échec métier (user inexistant / mdp incorrect)
        if (!result.isSuccess() || result.getPrincipal() == null) {
            Alert.error(view, result.getMessage());
            return;
        }

        // 3 - succès
        UserPrincipal principal = result.getPrincipal();

        Alert.success(view, "Bienvenue " + principal.nom() + " ^_^");

        view.dispose();
        onLoginSuccess(principal);

    }

    @Override
    public void onCancelRequested() {
        if (Alert.confirm(view, "Voulez-vous quitter ?")) {
            view.dispose();
            Window window = SwingUtilities.getWindowAncestor(view);
            if (window != null) window.dispose();
        }
    }

    @Override
    public void onLoginSuccess(UserPrincipal principal) {

        // TODO: ouvrir Dashboard / MainFrame
        ConsoleLogger.log("LOGIN SUCCESS: " + principal.nom() + " => [ username : " + principal.login() + " ]");

        // Ouvre un seul dashboard qui masque/affiche les modules
        ConsoleLogger.info("Call for Dashboard View : ");
        SwingUtilities.invokeLater(() -> {

            var dashboardController = ApplicationContext.getInstance().getBean(DashboardController.class);
            dashboardController.showDashboard(principal);
        });

    }
}
