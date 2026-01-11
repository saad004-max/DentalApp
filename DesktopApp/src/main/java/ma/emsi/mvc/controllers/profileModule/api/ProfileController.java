package ma.emsi.mvc.controllers.profileModule.api;



import java.util.function.Consumer;
import javax.swing.*;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.dto.profileDtos.ProfileData;

public interface ProfileController {
    JPanel getView(UserPrincipal principal);
    JPanel getView(UserPrincipal principal, Consumer<ProfileData> onProfileSaved);
}
