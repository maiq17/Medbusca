package upa;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class CadastroGestor extends JFrame {
    private JPanel contentPane;
    private JTextField textFieldCPF;
    private JTextField textFieldTelefone;
    private JTextField textFieldNome;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtEmail;
    private JComboBox<String> comboBox;
    private JTextField txtSenha;
    private JCheckBox checkBoxAdmin;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroGestor frame = new CadastroGestor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CadastroGestor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 842, 582);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setBounds(30, 30, 70, 20);
        contentPane.add(lblCPF);

        textFieldCPF = new JTextField();
        textFieldCPF.setBounds(110, 30, 200, 20);
        contentPane.add(textFieldCPF);
        textFieldCPF.setColumns(10);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 60, 70, 20);
        contentPane.add(lblTelefone);

        textFieldTelefone = new JTextField();
        textFieldTelefone.setBounds(110, 60, 200, 20);
        contentPane.add(textFieldTelefone);
        textFieldTelefone.setColumns(10);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 90, 70, 20);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(110, 90, 200, 20);
        contentPane.add(textFieldNome);
        textFieldNome.setColumns(10);

        JLabel lblUnidadeID = new JLabel("ID da Unidade:");
        lblUnidadeID.setBounds(10, 183, 100, 20);
        contentPane.add(lblUnidadeID);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(424, 25, 238, 42);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarGestor();
            }
        });
        contentPane.add(btnSalvar);

        comboBox = new JComboBox<String>();
        comboBox.setBounds(110, 182, 200, 22);
        contentPane.add(comboBox);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(43, 296, 681, 225);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null, null},
            },
            new String[] {
                "CPF", "NOME", "TELEFONE", "ID UNIDADE", "EMAIL", "Administrador"
            }
        ));
        scrollPane.setViewportView(table);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 122, 70, 20);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setColumns(10);
        txtEmail.setBounds(110, 122, 200, 20);
        contentPane.add(txtEmail);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 152, 70, 20);
        contentPane.add(lblSenha);

        txtSenha = new JTextField();
        txtSenha.setColumns(10);
        txtSenha.setBounds(110, 152, 200, 20);
        contentPane.add(txtSenha);

        checkBoxAdmin = new JCheckBox("Administrador");
        checkBoxAdmin.setBounds(10, 229, 106, 23);
        contentPane.add(checkBoxAdmin);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(424, 89, 238, 42);
        contentPane.add(btnExcluir);
        

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(424, 151, 238, 42);
        contentPane.add(btnEditar);

        preencherComboBoxComUnidades();
        tableModel = (DefaultTableModel) table.getModel();
        preencherTabelaComGestores();
        
    }

    private void salvarGestor() {
        // Outros campos
        String cpf = textFieldCPF.getText();
        String telefone = textFieldTelefone.getText();
        String nome = textFieldNome.getText();
        String idUnidade = (String) comboBox.getSelectedItem();
        String email = txtEmail.getText();
        String senhaGestor = txtSenha.getText();
        int adm = checkBoxAdmin.isSelected() ? 1 : 0; // 1 para administrador, 0 para gestor

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
        	conn = Conexao.ConnectDb();
            String sql = "INSERT INTO gestor (cpf, nome, telefone, id_unidade, email, senha, adm) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            stmt.setString(4, idUnidade);
            stmt.setString(5, email);
            stmt.setString(6, senhaGestor);
            stmt.setInt(7, adm);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Gestor de unidade cadastrado com sucesso!");

                tableModel.addRow(new Object[]{cpf, nome, telefone, idUnidade, email, (adm == 1) ? "Sim" : "Não"});
                textFieldCPF.setText("");
                textFieldTelefone.setText("");
                textFieldNome.setText("");
                txtEmail.setText("");
                txtSenha.setText("");
                checkBoxAdmin.setSelected(false); // Limpar a seleção do CheckBox
            } else {
                System.out.println("Erro ao cadastrar o gestor de unidade.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void preencherComboBoxComUnidades() {
    	Connection conn = null;
        PreparedStatement stmt = null;

        try {
        	conn = Conexao.ConnectDb();
            String sql = "SELECT id_unidade FROM unidade";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Limpar o ComboBox
            comboBox.removeAllItems();

            // Preencher o ComboBox com os IDs das unidades
            while (rs.next()) {
                String idUnidade = rs.getString("id_unidade");
                comboBox.addItem(idUnidade);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
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
    }

    private void preencherTabelaComGestores() {
    	Connection conn = null;
        PreparedStatement stmt = null;

        try {
        	conn = Conexao.ConnectDb();
            // Consulta SQL para buscar os gestores existentes
            String sql = "SELECT cpf, nome, telefone, id_unidade, email, adm FROM gestor";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Limpar a tabela
            tableModel.setRowCount(0);

            // Preencher a tabela com os gestores existentes
            while (rs.next()) {
                String cpf = rs.getString("cpf");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String idUnidade = rs.getString("id_unidade");
                String email = rs.getString("email");
                int adm = rs.getInt("adm");

                // Converter o valor "adm" em uma representação de texto
                String isAdm = (adm == 1) ? "Sim" : "Não";

                tableModel.addRow(new Object[]{cpf, nome, telefone, idUnidade, email, isAdm});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
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
    }
}
