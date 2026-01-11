package ma.emsi.mvc.controllers.otherModules.api;

import javax.swing.JPanel;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;

public interface PatientsController {
    JPanel getView(UserPrincipal principal);
}


