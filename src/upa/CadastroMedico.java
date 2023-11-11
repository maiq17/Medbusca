package upa;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import upa.telaLogin;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTabbedPane;

public class CadastroMedico extends JFrame {
    private JPanel contentPane;
    private JTextField textFieldNome;
    private JTextField textFieldCRM;
    private JComboBox<String> comboBoxEspecialidade;
    private JButton btnAtualizarMedico; // Novo botão para atualizar médico
    private JButton btnExcluirMedico; // Novo botão para excluir médico
    private JLabel lblNomeMedico;
    private JLabel lblCrmMedico;
    private JLabel lblEspecialidadeMedico;
    private JTable table;
    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroMedico frame = new CadastroMedico();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CadastroMedico() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 842, 582);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 81, 70, 20);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(79, 81, 200, 20);
        contentPane.add(textFieldNome);
        textFieldNome.setColumns(10);

        JLabel lblCrm = new JLabel("CRM:");
        lblCrm.setBounds(10, 125, 70, 20);
        contentPane.add(lblCrm);

        textFieldCRM = new JTextField();
        textFieldCRM.setBounds(79, 125, 200, 20);
        contentPane.add(textFieldCRM);
        textFieldCRM.setColumns(10);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(598, 444, 173, 52);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarMedico();
                
                
            }
        });
        contentPane.add(btnSalvar);
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(598, 316, 173, 52);
        contentPane.add(btnEditar);
        
        comboBoxEspecialidade = new JComboBox<String>();
        comboBoxEspecialidade.setBounds(79, 165, 200, 22);
        contentPane.add(comboBoxEspecialidade);
        
        JLabel lblEspecialidade = new JLabel("Especialidade:");
        lblEspecialidade.setBounds(10, 166, 70, 20);
        contentPane.add(lblEspecialidade);

        // Botão para atualizar médico

        // Botão para excluir médico
        btnExcluirMedico = new JButton("Excluir Médico");
        btnExcluirMedico.setBounds(598, 379, 173, 54);
        btnExcluirMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        contentPane.add(btnExcluirMedico);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 276, 523, 220);
        contentPane.add(scrollPane);
        
        table = new JTable();
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        		{null, null, null},
        	},
        	new String[] {
        		"CRM", "NOME", "ESPECIALIDADE"
        	}
        ));
        table.getColumnModel().getColumn(0).setPreferredWidth(112);
        table.getColumnModel().getColumn(1).setPreferredWidth(129);
        table.getColumnModel().getColumn(2).setPreferredWidth(103);
        scrollPane.setViewportView(table);

        // Preencher o JComboBox com as especialidades cadastradas
        preencherEspecialidades();
        preencherMedicos(); // Preenche o JComboBox de médicos
    }
    private void preencherEspecialidades() {
        // Lógica para recuperar as especialidades do banco de dados
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        	conn = Conexao.ConnectDb();

            String sql = "SELECT descricao_esp FROM especialidade_m";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<String> especialidades = new ArrayList<>();
            while (rs.next()) {
                especialidades.add(rs.getString("descricao_esp"));
            }

            // Preencher o JComboBox com as especialidades
            for (String especialidade : especialidades) {
                comboBoxEspecialidade.addItem(especialidade);
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
    }

    private void preencherMedicos() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0); // Limpe os dados da tabela

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

        	conn = Conexao.ConnectDb();
            String sql = "SELECT crm_medico, nome_medico, descricao_esp " +
                         "FROM medico " +
                         "INNER JOIN especialidade_m ON medico.especialidade = especialidade_m.id_especialidade_m " +
                         "WHERE medico.id_unidade = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, telaLogin.unidade); // Filtra por unidade

            rs = stmt.executeQuery();

            while (rs.next()) {
                String crm = rs.getString("crm_medico");
                String nome = rs.getString("nome_medico");
                String especialidade = rs.getString("descricao_esp");

                // Adicione a linha à tabela
                tableModel.addRow(new Object[]{crm, nome, especialidade});
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
    }

    private void salvarMedico() {
        String nome = textFieldNome.getText();
        String crm = textFieldCRM.getText();
        String especialidade = (String) comboBoxEspecialidade.getSelectedItem();

        int especialidadeId = buscarIdEspecialidade(especialidade);

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
        	conn = Conexao.ConnectDb();
            String sql = "INSERT INTO medico (crm_medico, especialidade, nome_medico, id_unidade) VALUES (?, ?, ?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, crm);
            stmt.setInt(2, especialidadeId);
            stmt.setString(3, nome);
            stmt.setInt(4, telaLogin.unidade); 

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Médico cadastrado com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar o médico.");
            }

            stmt.close();
            conn.close();

            textFieldNome.setText("");
            textFieldCRM.setText("");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar o médico.");
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

    private int buscarIdEspecialidade(String nomeEspecialidade) {
        int especialidadeId = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        	conn = Conexao.ConnectDb();
            String sql = "SELECT id_especialidade_m FROM especialidade_m WHERE descricao_esp = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeEspecialidade);
            rs = stmt.executeQuery();

            if (rs.next()) {
                especialidadeId = rs.getInt("id_especialidade_m");
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

        return especialidadeId;
    }
}
