package ma.emsi.mvc.controllers.dashboardModule.api;


import javax.swing.*;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.pagesNames.ApplicationPages;

public interface DashboardController {

    void showDashboard(UserPrincipal principal);

    // callbacks venant de la vue
    JComponent onNavigateRequested(ApplicationPages page);

    void onLogoutRequested();
    void onExitRequested();
}
