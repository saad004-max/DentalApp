package ma.emsi.service.profileService.api;


import java.util.Map;
import ma.emsi.mvc.dto.profileDtos.ProfileUpdateRequest;

public interface ProfileValidator {
    Map<String, String> validate(ProfileUpdateRequest req);
}
