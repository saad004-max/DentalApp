package ma.emsi.mvc.ui.pages.otherPages;

import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.common.BasePlaceholderPanel;

public class NotificationsPanel extends BasePlaceholderPanel {
    public NotificationsPanel(UserPrincipal principal) {
        super("Notifications", principal);
    }
}
