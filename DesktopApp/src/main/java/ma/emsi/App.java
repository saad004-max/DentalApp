package ma.emsi;

import ma.emsi.config.ApplicationContext;
import ma.emsi.mvc.controllers.authentificationModule.api.LoginController;


public class App 
{
    public static void main( String[] args ) throws Exception
    {
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        ApplicationContext.getInstance().getBean(LoginController.class).showLoginView();
    }
}
