package ma.emsi.mvc.ui.pages.otherPages;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class ParametragePanel extends BasePlaceholderPanel {
    public ParametragePanel(UserPrincipal principal) {
        super("Paramétrage Cabinet", principal);
    }
}
