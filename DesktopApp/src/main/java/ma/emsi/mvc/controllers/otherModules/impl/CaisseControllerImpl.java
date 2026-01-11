package ma.emsi.mvc.controllers.otherModules.impl;

import javax.swing.JPanel;
import lombok.RequiredArgsConstructor;
import ma.emsi.mvc.controllers.otherModules.api.CaisseController;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.CaissePanel;

@RequiredArgsConstructor
public class CaisseControllerImpl implements CaisseController {

    private JPanel cached;

    @Override
    public JPanel getView(UserPrincipal principal) {
        if (cached == null) cached = new CaissePanel(principal);
        return cached;
    }
}
