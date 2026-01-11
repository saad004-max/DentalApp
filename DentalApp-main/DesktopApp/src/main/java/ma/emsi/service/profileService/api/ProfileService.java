package ma.emsi.service.profileService.api;


import ma.emsi.mvc.dto.profileDtos.*;

public interface ProfileService {
    ProfileData loadByUserId(Long userId);
    ProfileUpdateResult update(ProfileUpdateRequest req);
    ChangePasswordResult changePassword(ChangePasswordRequest req);
}
