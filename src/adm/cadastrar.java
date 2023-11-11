package adm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cadastrar {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Cadastro de Administrador");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel lblRA = new JLabel("R.A:");
        JTextField txtRA = new JTextField();
        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = new JPasswordField();
        JLabel lblEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField();
        JCheckBox chkIsAdmin = new JCheckBox("É Administrador");

        JButton btnCadastrar = new JButton("Cadastrar");

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica de cadastro aqui
                String ra = txtRA.getText();
                String senha = new String(txtSenha.getPassword());
                String email = txtEmail.getText();
                boolean isAdmin = chkIsAdmin.isSelected();

                // Validação e inserção na base de dados aqui
                // Certifique-se de inserir isAdmin como verdadeiro para administradores
            }
        });

        panel.add(lblRA);
        panel.add(txtRA);
        panel.add(lblSenha);
        panel.add(txtSenha);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(chkIsAdmin);
        panel.add(btnCadastrar);

        frame.add(panel);
        frame.setVisible(true);
    }
}
