




package ma.emsi.mvc.ui.frames;


import java.awt.event.KeyEvent;
import ma.emsi.mvc.controllers.authentificationModule.api.LoginController;
import ma.emsi.mvc.ui.palette.buttons.MyButton;
import ma.emsi.mvc.ui.palette.fields.CustomPasswordField;
import ma.emsi.mvc.ui.palette.fields.CustomTextField;
import ma.emsi.mvc.ui.palette.utils.ImageTools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class LoginUI extends JFrame {

    private final LoginController controller;

    private JTextField txt_lg;
    private JPasswordField txt_pass;

    private JLabel lbl_err_login, lbl_err_pass, lbl_err_global;

    // Bouton Login (référence nécessaire pour KeyBinding)
    private JButton btLogin, btCancel;

    public LoginUI(LoginController controller) {
        this.controller = controller;

        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        // Construction UI
        setContentPane(buildRoot());

        // KeyBinding ENTER = Login (bonne pratique Swing)
        installKeyBindings();

        setVisible(false);
    }

   // Actions ———————————————————————————————————————————————————————————————————
    private void loginAction(ActionEvent e) {
        clearErrors();
        String login    = txt_lg.getText();
        String pass     = new String(txt_pass.getPassword());
        controller.onLoginRequested( login , pass );
    }

    private void cancelAction(ActionEvent e) {
        controller.onCancelRequested();
    }
    // ——————————————————————————————————————————————————————————————————————————



    // API Controller -> View
    public void showFieldErrors(Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) return;

        lbl_err_login.setText(errors.getOrDefault("login", " "));
        lbl_err_pass.setText(errors.getOrDefault("password", " "));
        lbl_err_global.setText(errors.getOrDefault("_global", " "));

        txt_lg.setBorder(errors.containsKey("login")
                ? BorderFactory.createLineBorder(new Color(222, 112, 112), 2)
                : UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));

        txt_pass.setBorder(errors.containsKey("password")
                ? BorderFactory.createLineBorder(new Color(222, 112, 112), 2)
                : UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }

    public void clearErrors() {
        // Vider les Labels d'erreurs
        lbl_err_login   .setText("");
        lbl_err_pass    .setText("");
        lbl_err_global  .setText("");


        // réinitialiser les bords des textFields
        txt_lg      .setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        txt_pass    .setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
    }

    // UI Builders ——————————————————————————————————————————————————————————————————————————
    // ——————————————————————————————————————————————————————————————————————————————————————
    // UI Header Builder  ———————————————————————————————————————————————————————————————————
    private JPanel buildHeader()        {

        JLabel lblTitleIcon = new JLabel(ImageTools.loadIcon("/static/icons/login.png", 120, 120));

        JPanel  header       = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
                header.setOpaque(false);
                header.add(lblTitleIcon);

        return header;
    }
    // UI Form Builder  —————————————————————————————————————————————————————————————————————
    private JPanel buildCenterForm()    {

        // Ligne LOGIN (icône + champ)       —————————————————————————————————————————————————
        JPanel  rowLogin = new JPanel();
                rowLogin.setOpaque(false);
                rowLogin.setLayout(new BoxLayout(rowLogin, BoxLayout.X_AXIS));

        JLabel  iconUser = new JLabel(ImageTools.loadIcon("/static/icons/user.png", 48, 48));
                iconUser.setBorder(new EmptyBorder(0, 0, 0, 12));

                txt_lg = new CustomTextField("Nom d'utilisateur");
                txt_lg.setFont(new Font("Optima", Font.BOLD, 18));
                txt_lg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                txt_lg.setPreferredSize(new Dimension(360, 60));
        txt_lg.setText("admin.omar");

                rowLogin.add(iconUser);
                rowLogin.add(txt_lg);

        // Erreur login       ———————————————————————————————————————————————————————————————
                lbl_err_login = new JLabel("");
                lbl_err_login.setFont(new Font("Optima", Font.BOLD, 14));
                lbl_err_login.setForeground(new Color(222, 112, 112));
                lbl_err_login.setBorder(new EmptyBorder(8, 5, 0, 0));


        // Ligne PASSWORD (icône + champ)      —————————————————————————————————————————————————

        JPanel  rowPass = new JPanel();
                rowPass.setOpaque(false);
                rowPass.setLayout(new BoxLayout(rowPass, BoxLayout.X_AXIS));
                rowPass.setBorder(new EmptyBorder(14, 0, 0, 0));

        JLabel  iconPass = new JLabel(ImageTools.loadIcon("/static/icons/pass.png", 48, 48));
                iconPass.setBorder(new EmptyBorder(0, 0, 0, 12));

                txt_pass = new CustomPasswordField("Mot de passe");
                txt_pass.setFont(new Font("Optima", Font.BOLD, 18));
                txt_pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                txt_pass.setPreferredSize(new Dimension(360, 60));

        txt_pass.setText("Admin@2025");

                rowPass.add(iconPass);
                rowPass.add(txt_pass);

        // Erreur pass       ———————————————————————————————————————————————————————————————
                lbl_err_pass = new JLabel(" ");
                lbl_err_pass.setFont(new Font("Optima", Font.BOLD, 14));
                lbl_err_pass.setForeground(new Color(222, 112, 112));
                lbl_err_pass.setBorder(new EmptyBorder(8, 5, 0, 0));

        // Erreur globale      —————————————————————————————————————————————————————————————
                lbl_err_global = new JLabel(" ");
                lbl_err_global.setFont(new Font("Optima", Font.BOLD, 14));
                lbl_err_global.setForeground(new Color(222, 112, 112));
                lbl_err_global.setBorder(new EmptyBorder(16, 0, 0, 0));
                lbl_err_global.setAlignmentX(Component.CENTER_ALIGNMENT);

        // —————————————————————————————————————————————————————————————————————————————————
        JPanel  center = new JPanel();
                center.setOpaque(false);
                center.setBorder(new EmptyBorder(60, 20, 2, 20));
                center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

                center.add(Box.createVerticalStrut(10));  // Margin vertical de 10 px

                center.add(rowLogin);
                center.add(lbl_err_login);
                center.add(rowPass);
                center.add(lbl_err_pass);
                center.add(lbl_err_global);

        return center;
    }
    // UI Footer Builder  ———————————————————————————————————————————————————————————————————
    private JPanel buildFooterButtons() {

        var buttonFont = new Font("Optima", Font.BOLD, 19);
        var loginIcon = ImageTools.loadIcon("/static/icons/connect.png", 40, 40);
        var cancelIcon = ImageTools.loadIcon("/static/icons/cancel.png", 40, 40);
                btLogin = new MyButton("Se connecter", loginIcon,buttonFont);
                btLogin.addActionListener(this::loginAction);

                btCancel = new MyButton("Annuler",cancelIcon, buttonFont);
                btCancel.addActionListener(this::cancelAction);

        JPanel  footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
                footer.setOpaque(false);

                footer.add(btLogin);
                footer.add(btCancel);

        return footer;
    }


    private JPanel buildRoot() {
        JPanel  root = new JPanel(new BorderLayout(0, 0));
                root.setBackground(new Color(255, 255, 255));
                root.setBorder(new EmptyBorder(18, 18, 30, 18));

                root.add(buildHeader(), BorderLayout.NORTH);
                root.add(buildCenterForm(), BorderLayout.CENTER);
                root.add(buildFooterButtons(), BorderLayout.SOUTH);
        return root;
    }

    // —————————————— KEY BINDINGS (BONNE PRATIQUE SWING)

    private void installKeyBindings() {

        // InputMap du RootPane → actif partout dans la fenêtre
        InputMap inputMap   =  getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap =  getRootPane().getActionMap();

        // Association de la touche ENTER à l'action "LOGIN"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "LOGIN");
        // Association de la touche Escape à l'action "CANCEL"
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), "CANCEL");

        // Action exécutée quand ENTER est pressée
        actionMap.put("LOGIN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction(e);
            }
        });
        // Action exécutée quand ENTER est pressée
        actionMap.put("CANCEL", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAction(e);
            }
        });
    }
}





