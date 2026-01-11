package ma.emsi.mvc.ui.pages.otherPages;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class CabinetsPanel extends BasePlaceholderPanel {
    public CabinetsPanel(UserPrincipal principal) {
        super("Backoffice — Cabinets", principal);
    }
}
