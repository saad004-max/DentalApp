package ma.emsi.mvc.ui.pages.otherPages;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class DossiersPanel extends BasePlaceholderPanel {
    public DossiersPanel(UserPrincipal principal) {
        super("Dossiers Médicaux", principal);
    }
}
