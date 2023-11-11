package upa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MudarSenha {
    private JFrame frame;
    private JPasswordField passwordFieldAtual;
    private JPasswordField passwordFieldNova;
    private JPasswordField passwordFieldConfirmarNova;
    private String cpf;

    public MudarSenha(String cpf) {
        this.cpf = cpf;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Mudar Senha");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Use cores personalizadas para tornar a interface mais agradável
        Color backgroundColor = new Color(220, 220, 220); // Cinza claro
        Color buttonColor = new Color(70, 130, 180); // Azul-royal

        frame.getContentPane().setBackground(backgroundColor);

        JLabel lblSenhaAtual = new JLabel("Senha Atual:");
        lblSenhaAtual.setBounds(50, 30, 100, 20);
        frame.getContentPane().add(lblSenhaAtual);

        passwordFieldAtual = new JPasswordField();
        passwordFieldAtual.setBounds(160, 30, 200, 20);
        frame.getContentPane().add(passwordFieldAtual);

        JLabel lblNovaSenha = new JLabel("Nova Senha:");
        lblNovaSenha.setBounds(50, 60, 100, 20);
        frame.getContentPane().add(lblNovaSenha);

        passwordFieldNova = new JPasswordField();
        passwordFieldNova.setBounds(160, 60, 200, 20);
        frame.getContentPane().add(passwordFieldNova);

        JLabel lblConfirmarNovaSenha = new JLabel("Confirmar Nova Senha:");
        lblConfirmarNovaSenha.setBounds(50, 90, 150, 20);
        frame.getContentPane().add(lblConfirmarNovaSenha);

        passwordFieldConfirmarNova = new JPasswordField();
        passwordFieldConfirmarNova.setBounds(200, 90, 160, 20);
        frame.getContentPane().add(passwordFieldConfirmarNova);

        JButton btnMudarSenha = new JButton("Mudar Senha");
        btnMudarSenha.setBounds(150, 140, 150, 30);
        frame.getContentPane().add(btnMudarSenha);
        btnMudarSenha.setBackground(buttonColor);
        btnMudarSenha.setForeground(Color.WHITE);
        btnMudarSenha.setOpaque(true);

        // Configuração do ActionListener
        btnMudarSenha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String senhaAtual = new String(passwordFieldAtual.getPassword());
                String novaSenha = new String(passwordFieldNova.getPassword());
                String confirmarNovaSenha = new String(passwordFieldConfirmarNova.getPassword());

                if (novaSenha.equals(confirmarNovaSenha)) {
                    if (mudarSenha(cpf, senhaAtual, novaSenha)) {
                        JOptionPane.showMessageDialog(frame, "Senha alterada com sucesso!");
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Erro ao alterar a senha. Verifique suas credenciais.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "A nova senha e a confirmação da senha não coincidem.");
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean mudarSenha(String cpf, String senhaAtual, String novaSenha) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.ConnectDb();

            String sql = "UPDATE gestor SET senha = ? WHERE cpf = ? AND senha = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, novaSenha);
            stmt.setString(2, cpf);
            stmt.setString(3, senhaAtual);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
