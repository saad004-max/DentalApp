package ma.emsi.mvc.controllers.otherModules.impl;

import javax.swing.JPanel;
import lombok.RequiredArgsConstructor;
import ma.emsi.mvc.controllers.otherModules.api.UsersController;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.UsersPanel;

@RequiredArgsConstructor
public class UsersControllerImpl implements UsersController {

    private JPanel cached;

    @Override
    public JPanel getView(UserPrincipal principal) {
        if (cached == null) cached = new UsersPanel(principal);
        return cached;
    }
}
