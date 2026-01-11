package ma.emsi.service.authentificationService.api;

import java.util.Map;
import ma.emsi.mvc.dto.authentificationDtos.AuthRequest;

public interface LoginFormValidator {


    Map<String, String> validate(AuthRequest request);


}
