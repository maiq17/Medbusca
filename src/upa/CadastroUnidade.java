package upa;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;

public class CadastroUnidade extends JFrame {
    private JPanel contentPane;
    private JTextField textFieldRua;
    private JTextField textFieldCEP;
    private JTextField textFieldNumero;
    private JTextField textFieldBairro;
    private JTextField textFieldCidade;
    private JTextField textFieldEstado;
    private JTextField textFieldNome;
    private JButton btnVoltar;
    private JButton btn;
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane; 


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroUnidade frame = new CadastroUnidade();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CadastroUnidade() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 842, 582);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblRua = new JLabel("Rua:");
        lblRua.setBounds(30, 30, 70, 20);
        contentPane.add(lblRua);

        textFieldRua = new JTextField();
        textFieldRua.setBounds(110, 30, 200, 20);
        contentPane.add(textFieldRua);
        textFieldRua.setColumns(10);


        JLabel lblCEP = new JLabel("CEP:");
        lblCEP.setBounds(30, 60, 70, 20);
        contentPane.add(lblCEP);

        textFieldCEP = new JTextField();
        textFieldCEP.setBounds(110, 60, 200, 20);
        contentPane.add(textFieldCEP);
        textFieldCEP.setColumns(10);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(30, 90, 70, 20);
        contentPane.add(lblNumero);

        textFieldNumero = new JTextField();
        textFieldNumero.setBounds(110, 90, 200, 20);
        contentPane.add(textFieldNumero);
        textFieldNumero.setColumns(10);

        JLabel lblBairro = new JLabel("Bairro:");
        lblBairro.setBounds(30, 120, 70, 20);
        contentPane.add(lblBairro);

        textFieldBairro = new JTextField();
        textFieldBairro.setBounds(110, 120, 200, 20);
        contentPane.add(textFieldBairro);
        textFieldBairro.setColumns(10);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(30, 150, 70, 20);
        contentPane.add(lblCidade);

        textFieldCidade = new JTextField();
        textFieldCidade.setBounds(110, 150, 200, 20);
        contentPane.add(textFieldCidade);
        textFieldCidade.setColumns(10);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 180, 70, 20);
        contentPane.add(lblEstado);

        textFieldEstado = new JTextField();
        textFieldEstado.setBounds(110, 180, 200, 20);
        contentPane.add(textFieldEstado);
        textFieldEstado.setColumns(10);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 210, 70, 20);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(110, 210, 200, 20);
        contentPane.add(textFieldNome);
        textFieldNome.setColumns(10);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarUnidade();
            }
        });
        btnSalvar.setBounds(499, 30, 253, 54);
        contentPane.add(btnSalvar);
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Rua");
        tableModel.addColumn("CEP");
        tableModel.addColumn("Número");
        tableModel.addColumn("Bairro");
        tableModel.addColumn("Cidade");
        tableModel.addColumn("Estado");
        tableModel.addColumn("Nome");

        table = new JTable(tableModel);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(499, 103, 253, 55);
        contentPane.add(btnVoltar);

        btn = new JButton("Editar");
        btn.setBounds(499, 179, 253, 55);
        contentPane.add(btn);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 299, 722, 214);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "Rua", "Cep", "Numero", "Bairro", "Cidade", "Estado", "Nome", "URL"
            }
        ));
        scrollPane.setViewportView(table);

        JLabel lblUrl = new JLabel("URL:");
        lblUrl.setBounds(30, 241, 280, 20);
        contentPane.add(lblUrl);
        
        

        tableModel = (DefaultTableModel) table.getModel();
        preencherTabelaComUnidades();
    }

    private void salvarUnidade() {
        String rua = textFieldRua.getText();
        String cep = textFieldCEP.getText();
        String numero = textFieldNumero.getText();
        String bairro = textFieldBairro.getText();
        String cidade = textFieldCidade.getText();
        String estado = textFieldEstado.getText();
        String nome = textFieldNome.getText();

        PreparedStatement stmt = null;
        Connection conn = null;

        try {
            conn = Conexao.ConnectDb();

            String sql = "INSERT INTO unidade (rua_unidade, cep_unidade, numero_unidade, bairro_unidade, cidade_unidade, estado_unidade, nome_unidade) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, rua);
            stmt.setString(2, cep);
            stmt.setString(3, numero);
            stmt.setString(4, bairro);
            stmt.setString(5, cidade);
            stmt.setString(6, estado);
            stmt.setString(7, nome);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Unidade médica cadastrada com sucesso!");
                textFieldRua.setText("");
                textFieldCEP.setText("");
                textFieldNumero.setText("");
                textFieldBairro.setText("");
                textFieldCidade.setText("");
                textFieldEstado.setText("");
                textFieldNome.setText("");
                preencherTabelaComUnidades();
            } else {
                System.out.println("Erro ao cadastrar a unidade médica.");
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

    private void preencherTabelaComUnidades() {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexao.ConnectDb();
            String sql = "SELECT rua_unidade, cep_unidade, numero_unidade, bairro_unidade, cidade_unidade, estado_unidade, nome_unidade FROM unidade";
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                String rua = rs.getString("rua_unidade");
                String cep = rs.getString("cep_unidade");
                String numero = rs.getString("numero_unidade");
                String bairro = rs.getString("bairro_unidade");
                String cidade = rs.getString("cidade_unidade");
                String estado = rs.getString("estado_unidade");
                String nome = rs.getString("nome_unidade");

                tableModel.addRow(new Object[]{rua, cep, numero, bairro, cidade, estado, nome});
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