package ma.emsi.mvc.ui.pages.otherPages;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class UsersPanel extends BasePlaceholderPanel {
    public UsersPanel(UserPrincipal principal) {
        super("Backoffice — Users", principal);
    }
}
