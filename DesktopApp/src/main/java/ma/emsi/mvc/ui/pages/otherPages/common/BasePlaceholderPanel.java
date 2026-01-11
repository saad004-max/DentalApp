package ma.emsi.mvc.ui.pages.otherPages.common;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ma.emsi.mvc.dto.authentificationDtos.UserPrincipal;

public class BasePlaceholderPanel extends JPanel {

    public BasePlaceholderPanel(String title, UserPrincipal principal) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Optima", Font.BOLD, 28));

        JPanel head = new JPanel(new GridLayout(0, 1, 0, 6));
        head.setOpaque(false);
        head.add(lblTitle);

        if (principal != null) {
            JLabel lblUser = new JLabel("Session: " + safe(principal.nom()) + "  |  Login: " + safe(principal.login()));
            lblUser.setFont(new Font("Optima", Font.PLAIN, 14));
            head.add(lblUser);

            JLabel lblRole = new JLabel("Rôle principal: " + (principal.rolePrincipal() != null ? principal.rolePrincipal().name() : "—"));
            lblRole.setFont(new Font("Optima", Font.PLAIN, 14));
            head.add(lblRole);
        }

        JTextArea info = new JTextArea(
                "✅ Placeholder prêt.\n" +
                "Ici tu mettras la vraie UI du module (table, forms, filtres...).\n" +
                "Tu peux remplacer ce panel par ton vrai panel plus tard sans changer le routing."
        );
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setFont(new Font("Optima", Font.PLAIN, 15));
        info.setBorder(new EmptyBorder(18, 0, 0, 0));
        info.setBackground(Color.WHITE);

        add(head, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
    }

    private String safe(String s) { return s == null ? "—" : s; }
}
