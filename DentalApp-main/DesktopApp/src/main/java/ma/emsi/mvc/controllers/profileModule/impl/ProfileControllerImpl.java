package ma.emsi.mvc.controllers.profileModule.impl;



import java.util.function.Consumer;
import javax.swing.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ma.emsi.mvc.controllers.profileModule.api.ProfileController;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;
import ma.emsi.mvc.dto.profileDtos.ProfileData;
import ma.emsi.mvc.ui.pages.profilePages.ProfilePanel;
import ma.emsi.service.profileService.api.ProfileService;

@Getter @Setter
@AllArgsConstructor
public class ProfileControllerImpl implements ProfileController {

    private final ProfileService service;

    @Override
    public JPanel getView(UserPrincipal principal) {
        return getView(principal, null);


    }

    @Override
    public JPanel getView(UserPrincipal principal, Consumer<ProfileData> onProfileSaved) {
        if (principal == null || principal.id() == null) {
            return new JPanel(); // ou panel "Session invalide"
        }
        ProfileData data = service.loadByUserId(principal.id());

        // ✅ IMPORTANT : ProfilePanel doit accepter ce callback
        return new ProfilePanel(this, service, data, onProfileSaved);
    }
}
