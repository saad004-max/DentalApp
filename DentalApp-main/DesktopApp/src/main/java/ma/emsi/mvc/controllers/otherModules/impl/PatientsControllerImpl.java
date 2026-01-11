package ma.emsi.mvc.controllers.otherModules.impl;

import javax.swing.JPanel;
import lombok.RequiredArgsConstructor;
import ma.emsi.mvc.controllers.otherModules.api.PatientsController;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.ui.pages.otherPages.PatientsPanel;

@RequiredArgsConstructor
public class PatientsControllerImpl implements PatientsController {

    private JPanel cached;

    @Override
    public JPanel getView(UserPrincipal principal) {
        if (cached == null) {
            cached = new PatientsPanel(principal);
        }
        return cached;
    }
}
