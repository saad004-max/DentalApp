package ma.emsi.mvc.controllers.authentificationModule.api;


import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;

public interface LoginController {

    void showLoginView();

    // callbacks venant de la vue
    void onLoginRequested(String login, String password);
    void onCancelRequested();

    void onLoginSuccess(UserPrincipal principal);
}
