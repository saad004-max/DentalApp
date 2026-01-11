package ma.emsi.mvc.ui.pages.otherPages;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class PatientsPanel extends BasePlaceholderPanel {
    public PatientsPanel(UserPrincipal principal) {
        super("Module Patients", principal);
    }
}
