package ma.emsi.mvc.controllers.otherModules.impl;

import javax.swing.JPanel;
import lombok.RequiredArgsConstructor;
import ma.emsi.mvc.controllers.otherModules.api.ParametrageController;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.ParametragePanel;

@RequiredArgsConstructor
public class ParametrageControllerImpl implements ParametrageController {

    private JPanel cached;

    @Override
    public JPanel getView(UserPrincipal principal) {
        if (cached == null) cached = new ParametragePanel(principal);
        return cached;
    }
}
