package upa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import upa.TelaDisponibilidade;

public class telaLogin {
    private JFrame frame;
    private JTextField textFieldCPF;
    private JPasswordField passwordField;
    public static int unidade;
    public String gestor;
    public String descricao;
    public static int cpfGestor;

    public telaLogin() {
        initialize();
    }

    public class Sessao {
        public static int idUnidade;
        public static int idGestor;
    }

    private void initialize() {
        frame = new JFrame("Tela de Login");
        frame.setBounds(100, 100, 391, 224);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Use cores personalizadas para tornar a interface mais agradável
        Color backgroundColor = new Color(220, 220, 220); // Cinza claro
        Color buttonColor = new Color(70, 130, 180); // Azul-royal

        frame.getContentPane().setBackground(backgroundColor);

        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setBounds(50, 30, 60, 20);
        frame.getContentPane().add(lblCPF);

        textFieldCPF = new JTextField();
        textFieldCPF.setBounds(120, 30, 200, 20);
        frame.getContentPane().add(textFieldCPF);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(50, 60, 60, 20);
        frame.getContentPane().add(lblSenha);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 60, 200, 20);
        frame.getContentPane().add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 100, 100, 30);
        frame.getContentPane().add(btnLogin);
        btnLogin.setBackground(buttonColor);
        btnLogin.setForeground(Color.WHITE); // Texto em branco
        btnLogin.setOpaque(true);

        // Configuração do ActionListener
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cpf = textFieldCPF.getText();
                String senha = new String(passwordField.getPassword());
                int adm = validarCredenciais(cpf, senha);

                if (adm != -1) {
                    frame.dispose();

                    GestorDAO gestorDAO = new GestorDAO();
                    int idUnidade = gestorDAO.recuperarIDUnidade(cpf);
                    int idGestor = gestorDAO.recuperarCpfGestor(cpf);

                    Sessao.idUnidade = idUnidade;
                    Sessao.idGestor = idGestor;

                    if (adm == 0) {
                        // Se adm for 0, vá para o frameTela
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    tela frameTela = new tela();
                                    frameTela.setVisible(true);
                                    frame.dispose();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        // Se adm for diferente de 0, vá para o frameAdm
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    Adm frameAdm = new Adm();
                                    frameAdm.setVisible(true);
                                    frame.dispose();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Credenciais inválidas. Tente novamente.");
                }


            }
        });


        frame.setVisible(true);
    }
    
    private int validarCredenciais(String cpf, String senha) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Conexao.ConnectDb();

            String sql = "SELECT adm FROM gestor WHERE cpf = ? AND senha = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("adm");
            } else {
                return -1; // Usuário não encontrado
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1; // Em caso de erro
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    telaLogin window = new telaLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public class GestorDAO {
        // Método para recuperar o ID da unidade associado a um CPF de gestor
        public int recuperarIDUnidade(String cpf) {
            int idUnidade = -1; // Valor padrão caso a recuperação falhe

            Connection conn =null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
            	conn = Conexao.ConnectDb();
                String sql = "SELECT id_unidade FROM gestor WHERE cpf = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, cpf);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    idUnidade = rs.getInt("id_unidade");
                    unidade = rs.getInt("id_unidade");

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            return idUnidade;
        }

        // Método para recuperar o ID do gestor associado a um CPF
        public int recuperarCpfGestor(String cpf) {
            int idGestor = -1; // Valor padrão caso a recuperação falhe

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
            	conn = Conexao.ConnectDb();
                String sql = "SELECT cpf FROM gestor WHERE cpf = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, cpf);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    idGestor = rs.getInt("cpf");
                    cpfGestor= rs.getInt("cpf");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            return idGestor;
        }

        // Método para recuperar o valor da coluna "adm" associado a um CPF
        public int recuperarAdmStatus(String cpf) {
            int adm = -1; // Valor padrão caso a recuperação falhe

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
            	conn = Conexao.ConnectDb();
                String sql = "SELECT adm FROM gestor WHERE cpf = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, cpf);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    adm = rs.getInt("adm");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            return adm;
        }
    }
}
