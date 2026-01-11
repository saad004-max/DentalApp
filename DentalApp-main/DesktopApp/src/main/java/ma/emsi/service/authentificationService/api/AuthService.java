package ma.emsi.service.authentificationService.api;


import ma.emsi.mvc.dto.authentificationDtos.AuthRequest;
import ma.emsi.mvc.dto.authentificationDtos.AuthResult;

public interface AuthService {

    AuthResult authenticate(AuthRequest request);




}
