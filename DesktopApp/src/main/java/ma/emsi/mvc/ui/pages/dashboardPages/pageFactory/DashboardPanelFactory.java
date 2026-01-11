package ma.emsi.mvc.ui.pages.dashboardPages.pageFactory;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.entities.modules.enums.RoleType;

import javax.swing.*;
import ma.emsi.mvc.ui.pages.dashboardPages.AdminDashboardPanel;
import ma.emsi.mvc.ui.pages.dashboardPages.DefaultDashboardPanel;
import ma.emsi.mvc.ui.pages.dashboardPages.DoctorDashboardPanel;
import ma.emsi.mvc.ui.pages.dashboardPages.SecretaryDashboardPanel;

public final class DashboardPanelFactory {

    private DashboardPanelFactory(){}

    public static JComponent create(UserPrincipal principal) {
        RoleType role = (principal != null) ? principal.rolePrincipal() : null;

        if (role == null) {
            return new DefaultDashboardPanel(principal);
        }

        return switch (role) {
            case ADMIN      -> new AdminDashboardPanel(principal);
            case MEDECIN    -> new DoctorDashboardPanel(principal);
            case SECRETAIRE -> new SecretaryDashboardPanel(principal);
            default         -> new DefaultDashboardPanel(principal);
        };
    }
}
