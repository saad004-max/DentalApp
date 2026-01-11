package ma.emsi.mvc.controllers.otherModules.api;

import javax.swing.*;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;

public interface UsersController {
    JPanel getView(UserPrincipal principal);
}

