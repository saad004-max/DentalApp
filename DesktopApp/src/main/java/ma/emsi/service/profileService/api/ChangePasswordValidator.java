package ma.emsi.service.profileService.api;


import java.util.Map;
import ma.emsi.mvc.dto.profileDtos.ChangePasswordRequest;

public interface ChangePasswordValidator {
    Map<String, String> validate(ChangePasswordRequest req);
}
