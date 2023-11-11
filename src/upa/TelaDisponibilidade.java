package upa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class TelaDisponibilidade {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    static void createAndShowGUI() {
        JFrame frame = new JFrame("Tela de Disponibilidade");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel disponibilidadePanel = new DisponibilidadePanel();
        tabbedPane.addTab("Disponibilidade do Médico", disponibilidadePanel);
        
        CadastroUnidadePanel cadastroUnidadePanel = new CadastroUnidadePanel();
        tabbedPane.add("Cadastro de Unidade", cadastroUnidadePanel);
        
        // Crie um DefaultTableModel e uma JTable para o CadastroGestorPanel
        DefaultTableModel gestorTableModel = new DefaultTableModel();
        JTable gestorTable = new JTable(gestorTableModel);

        CadastroGestorPanel cadastroGestorPanel = new CadastroGestorPanel(gestorTableModel, gestorTable);
        tabbedPane.addTab("Cadastro de Gestor", cadastroGestorPanel);

        JPanel cadastroMedicoPanel = new CadastroMedicoPanel();
        tabbedPane.addTab("Cadastro de Médico", cadastroMedicoPanel);

        CadastroEspecialidadePanel cadastroEspecialidadePanel = new CadastroEspecialidadePanel();
        tabbedPane.addTab("Cadastro de Especialidade", cadastroEspecialidadePanel);

        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}

class CadastroMedicoPanel extends JPanel {
    private JTextField textFieldNome;
    private JTextField textFieldCRM;
    private JComboBox<String> comboBoxEspecialidade;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluirMedico;
    private JTable table;

    public CadastroMedicoPanel() {
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(10, 81, 70, 20);
        add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setBounds(79, 81, 200, 20);
        add(textFieldNome);
        textFieldNome.setColumns(10);

        JLabel lblCrm = new JLabel("CRM:");
        lblCrm.setBounds(10, 125, 70, 20);
        add(lblCrm);

        textFieldCRM = new JTextField();
        textFieldCRM.setBounds(79, 125, 200, 20);
        add(textFieldCRM);
        textFieldCRM.setColumns(10);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(598, 444, 173, 52);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarMedico();
            }
        });
        add(btnSalvar);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(598, 316, 173, 52);
        add(btnEditar);

        comboBoxEspecialidade = new JComboBox<String>();
        comboBoxEspecialidade.setBounds(79, 165, 200, 22);
        add(comboBoxEspecialidade);

        JLabel lblEspecialidade = new JLabel("Especialidade:");
        lblEspecialidade.setBounds(10, 166, 70, 20);
        add(lblEspecialidade);

        btnExcluirMedico = new JButton("Excluir Médico");
        btnExcluirMedico.setBounds(598, 379, 173, 54);
        add(btnExcluirMedico);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 276, 523, 220);
        add(scrollPane);

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
            stmt.setInt(1, telaLogin.unidade); 

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


class DisponibilidadePanel extends JPanel {
    private JComboBox<String> comboBoxCRM;
    private JTextField txtDataHoraFim;
    private JTextField txtDataHoraInicio;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNome;
    private JTextField txtEspecialidade;

    public DisponibilidadePanel() {
        setLayout(null);

        comboBoxCRM = new JComboBox<>();
        comboBoxCRM.setBounds(10, 72, 133, 20);
        add(comboBoxCRM);
        preencherCRMMedicos(comboBoxCRM);

        txtDataHoraFim = new JTextField();
        txtDataHoraFim.setBounds(192, 166, 133, 20);
        txtDataHoraFim.setColumns(10);
        add(txtDataHoraFim);

        txtDataHoraInicio = new JTextField();
        txtDataHoraInicio.setBounds(192, 122, 133, 20);
        txtDataHoraInicio.setColumns(10);
        add(txtDataHoraInicio);

        JLabel lblDataF = new JLabel("Data-Hora-Fim");
        lblDataF.setBounds(215, 153, 82, 14);
        add(lblDataF);

        JLabel lblEspecialidade = new JLabel("Especialidade");
        lblEspecialidade.setBounds(33, 153, 82, 14);
        add(lblEspecialidade);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(50, 103, 46, 14);
        add(lblNome);

        JLabel lblDataI = new JLabel("Data-Hora-Inicio");
        lblDataI.setBounds(215, 109, 95, 14);
        add(lblDataI);

        JLabel lblCRM = new JLabel("CRM");
        lblCRM.setBounds(50, 59, 46, 14);
        add(lblCRM);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 197, 785, 325);
        add(scrollPane);

        table = new JTable();
        model = new DefaultTableModel(
            new Object[][] {
                {false, null, null, null, "", null, null, null},
            },
            new String[] {
                "", "CRM", "NOME", "ESPECIALIDADE", "DATA-HORA-INICIO", "DATA-HORA-FIM"
            }
        );

        table.setModel(model);
        table.getColumnModel().getColumn(2).setPreferredWidth(113);
        table.getColumnModel().getColumn(3).setPreferredWidth(102);
        table.getColumnModel().getColumn(4).setPreferredWidth(114);
        table.getColumnModel().getColumn(5).setPreferredWidth(97);

        TableColumn checkBoxColumn = table.getColumnModel().getColumn(0);
        checkBoxColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        checkBoxColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        scrollPane.setViewportView(table);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(428, 131, 148, 23);
        add(btnAdicionar);

        JButton btnRemover = new JButton("Remover");
        btnRemover.setBounds(428, 163, 148, 23);
        add(btnRemover);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(584, 131, 148, 23);
        add(btnAtualizar);

        JButton btnCadastroMedico = new JButton("Cadastrar Médico");
        btnCadastroMedico.setBounds(586, 165, 146, 23);
        add(btnCadastroMedico);

        // Adicione os ouvintes de ação para os botões (você precisa implementar essa parte)

        btnCadastroMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Código para abrir a tela de cadastro de médico
            }
        });
    }

    public static void preencherNomesMedicos(JComboBox<String> comboBoxNome) {
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

        	conn = Conexao.ConnectDb();
            String sql = "SELECT nome_medico FROM medico";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nomeMedico = rs.getString("nome_medico");
                comboBoxNome.addItem(nomeMedico);
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

    public static void preencherCRMMedicos(JComboBox<String> comboBoxCRM) {
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

        	conn = Conexao.ConnectDb();
            String sql = "SELECT crm_medico FROM medico WHERE id_unidade = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, telaLogin.unidade); // Filtrar pela unidade definida

            rs = stmt.executeQuery();

            // Limpe o JComboBox para evitar duplicatas
            comboBoxCRM.removeAllItems();

            while (rs.next()) {
                String crmMedico = rs.getString("crm_medico");
                comboBoxCRM.addItem(crmMedico);
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

    public static void atualizarTabela(DefaultTableModel model) {
        Connection conexao = Conexao.ConnectDb();

        if (conexao != null) {
            try {
                String consultaSQL = "SELECT dm.crm_medico, dm.dataHoraInicio, dm.dataHoraFim, m.nome_medico, m.especialidade " +
                        "FROM disponibilidade_medico dm " +
                        "JOIN medico m ON dm.crm_medico = m.crm_medico " +
                        "WHERE dm.id_unidade = ?";

                PreparedStatement pstmt = conexao.prepareStatement(consultaSQL);
                pstmt.setInt(1, telaLogin.unidade);

                ResultSet rs = pstmt.executeQuery();

                model.setRowCount(0);

                while (rs.next()) {
                    Object[] row = new Object[6];
                    row[0] = false;
                    row[1] = rs.getString("crm_medico");
                    row[2] = rs.getString("nome_medico");
                    row[3] = rs.getString("especialidade");
                    row[4] = rs.getString("dataHoraInicio");
                    row[5] = rs.getString("dataHoraFim");
                    model.addRow(row);
                }

                rs.close();
                pstmt.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } finally {
                try {
                    conexao.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
 class CadastroEspecialidadePanel extends JPanel {
    private JTextField textFieldDescricao;
    private JButton btnAdicionar;

    private Connection conn;
    private JScrollPane scrollPane;
    private JTable table;

    public CadastroEspecialidadePanel() {
        setLayout(null);

        JLabel lblNovaDescricao = new JLabel("Especialidade:");
        lblNovaDescricao.setBounds(10, 157, 220, 49);
        add(lblNovaDescricao);

        textFieldDescricao = new JTextField();
        textFieldDescricao.setBounds(154, 157, 220, 49);
        add(textFieldDescricao);

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(503, 45, 220, 49);
        add(btnAdicionar);
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(48, 276, 454, 231);
        add(scrollPane);
        
        table = new JTable();
        table.setModel(new DefaultTableModel(
        	new Object[][] {},
        	new String[] {
        		"Id_especialidade", "Nome_especialidade"
        	}
        ));
        scrollPane.setViewportView(table);
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(503, 115, 220, 49);
        add(btnExcluir);
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(503, 185, 220, 49);
        add(btnEditar);

        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarEspecialidade();
            }
        });

        // Inicializa a conexão com o banco de dados
        conectarAoBanco();

        // Preenche a tabela de especialidades do banco de dados
        preencherTabelaEspecialidades();
    }

    private void conectarAoBanco() {
        Connection conn = Conexao.ConnectDb();
    }

    private void desconectarDoBanco() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void preencherTabelaEspecialidades() {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0); // Limpar a tabela
        Connection conn = null;
        try {
        	conn = Conexao.ConnectDb();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM especialidade_m");

            while (rs.next()) {
                int id = rs.getInt("id_especialidade_m");
                String descricao = rs.getString("descricao_esp");
                tableModel.addRow(new Object[]{id, descricao});
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void adicionarEspecialidade() {
        String novaDescricao = textFieldDescricao.getText().trim();

        if (!novaDescricao.isEmpty()) {
            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO especialidade_m (descricao_esp) VALUES (?)");
                stmt.setString(1, novaDescricao);
                stmt.executeUpdate();
                stmt.close();

                preencherTabelaEspecialidades(); // Atualiza a tabela após adicionar
                textFieldDescricao.setText(""); // Limpa o campo de texto
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
 
 class CadastroGestorPanel extends JPanel {
	    private JTextField textFieldCPF;
	    private JTextField textFieldTelefone;
	    private JTextField textFieldNome;
	    private DefaultTableModel tableModel;
	    private JTable table;
	    private JTextField txtEmail;
	    private JComboBox<String> comboBox;
	    private JTextField txtSenha;
	    private JCheckBox checkBoxAdmin;

	    public CadastroGestorPanel(DefaultTableModel tableModel, JTable table) {
	        this.tableModel = tableModel;
	        this.table = table;

	        setLayout(null);

	        JLabel lblCPF = new JLabel("CPF:");
	        lblCPF.setBounds(30, 30, 70, 20);
	        add(lblCPF);

	        textFieldCPF = new JTextField();
	        textFieldCPF.setBounds(110, 30, 200, 20);
	        add(textFieldCPF);
	        textFieldCPF.setColumns(10);

	        JLabel lblTelefone = new JLabel("Telefone:");
	        lblTelefone.setBounds(30, 60, 70, 20);
	        add(lblTelefone);

	        textFieldTelefone = new JTextField();
	        textFieldTelefone.setBounds(110, 60, 200, 20);
	        add(textFieldTelefone);
	        textFieldTelefone.setColumns(10);

	        JLabel lblNome = new JLabel("Nome:");
	        lblNome.setBounds(30, 90, 70, 20);
	        add(lblNome);

	        textFieldNome = new JTextField();
	        textFieldNome.setBounds(110, 90, 200, 20);
	        add(textFieldNome);
	        textFieldNome.setColumns(10);

	        JLabel lblUnidadeID = new JLabel("ID da Unidade:");
	        lblUnidadeID.setBounds(10, 183, 100, 20);
	        add(lblUnidadeID);

	        JButton btnSalvar = new JButton("Salvar");
	        btnSalvar.setBounds(424, 25, 238, 42);
	        btnSalvar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                salvarGestor();
	            }
	        });
	        add(btnSalvar);

	        comboBox = new JComboBox<String>();
	        comboBox.setBounds(110, 182, 200, 22);
	        add(comboBox);

	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBounds(43, 296, 681, 225);
	        add(scrollPane);

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
	        add(lblEmail);

	        txtEmail = new JTextField();
	        txtEmail.setColumns(10);
	        txtEmail.setBounds(110, 122, 200, 20);
	        add(txtEmail);

	        JLabel lblSenha = new JLabel("Senha:");
	        lblSenha.setBounds(30, 152, 70, 20);
	        add(lblSenha);

	        txtSenha = new JTextField();
	        txtSenha.setColumns(10);
	        txtSenha.setBounds(110, 152, 200, 20);
	        add(txtSenha);

	        checkBoxAdmin = new JCheckBox("Administrador");
	        checkBoxAdmin.setBounds(10, 229, 106, 23);
	        add(checkBoxAdmin);

	        JButton btnExcluir = new JButton("Excluir");
	        btnExcluir.setBounds(424, 89, 238, 42);
	        add(btnExcluir);

	        JButton btnEditar = new JButton("Editar");
	        btnEditar.setBounds(424, 151, 238, 42);
	        add(btnEditar);

	        preencherComboBoxComUnidades();
	        preencherTabelaComGestores();
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
 
 class CadastroUnidadePanel extends JPanel {
	    private JTextField textFieldRua;
	    private JTextField textFieldCEP;
	    private JTextField textFieldNumero;
	    private JTextField textFieldBairro;
	    private JTextField textFieldCidade;
	    private JTextField textFieldEstado;
	    private JTextField textFieldNome;
	    private JButton btnSalvar;
	    private JScrollPane scrollPane;
	    private JTable table;
	    private DefaultTableModel tableModel;

	    public CadastroUnidadePanel() {
	        setLayout(null);

	        JLabel lblRua = new JLabel("Rua:");
	        lblRua.setBounds(30, 30, 70, 20);
	        add(lblRua);

	        textFieldRua = new JTextField();
	        textFieldRua.setBounds(110, 30, 200, 20);
	        add(textFieldRua);
	        textFieldRua.setColumns(10);

	        JLabel lblCEP = new JLabel("CEP:");
	        lblCEP.setBounds(30, 60, 70, 20);
	        add(lblCEP);

	        textFieldCEP = new JTextField();
	        textFieldCEP.setBounds(110, 60, 200, 20);
	        add(textFieldCEP);
	        textFieldCEP.setColumns(10);

	        JLabel lblNumero = new JLabel("Número:");
	        lblNumero.setBounds(30, 90, 70, 20);
	        add(lblNumero);

	        textFieldNumero = new JTextField();
	        textFieldNumero.setBounds(110, 90, 200, 20);
	        add(textFieldNumero);
	        textFieldNumero.setColumns(10);

	        JLabel lblBairro = new JLabel("Bairro:");
	        lblBairro.setBounds(30, 120, 70, 20);
	        add(lblBairro);

	        textFieldBairro = new JTextField();
	        textFieldBairro.setBounds(110, 120, 200, 20);
	        add(textFieldBairro);
	        textFieldBairro.setColumns(10);

	        JLabel lblCidade = new JLabel("Cidade:");
	        lblCidade.setBounds(30, 150, 70, 20);
	        add(lblCidade);

	        textFieldCidade = new JTextField();
	        textFieldCidade.setBounds(110, 150, 200, 20);
	        add(textFieldCidade);
	        textFieldCidade.setColumns(10);

	        JLabel lblEstado = new JLabel("Estado:");
	        lblEstado.setBounds(30, 180, 70, 20);
	        add(lblEstado);

	        textFieldEstado = new JTextField();
	        textFieldEstado.setBounds(110, 180, 200, 20);
	        add(textFieldEstado);
	        textFieldEstado.setColumns(10);

	        JLabel lblNome = new JLabel("Nome:");
	        lblNome.setBounds(30, 210, 70, 20);
	        add(lblNome);

	        textFieldNome = new JTextField();
	        textFieldNome.setBounds(110, 210, 200, 20);
	        add(textFieldNome);
	        textFieldNome.setColumns(10);

	        btnSalvar = new JButton("Salvar");
	        btnSalvar.setBounds(499, 30, 253, 54);
	        add(btnSalvar);

	        tableModel = new DefaultTableModel();
	        tableModel.addColumn("Rua");
	        tableModel.addColumn("CEP");
	        tableModel.addColumn("Número");
	        tableModel.addColumn("Bairro");
	        tableModel.addColumn("Cidade");
	        tableModel.addColumn("Estado");
	        tableModel.addColumn("Nome");

	        table = new JTable(tableModel);

	        Component btnVoltar = new JButton("Voltar");
	        btnVoltar.setBounds(499, 103, 253, 55);
	        add(btnVoltar);

	        JButton btn = new JButton("Editar");
	        btn.setBounds(499, 179, 253, 55);
	        add(btn);

	        scrollPane = new JScrollPane();
	        scrollPane.setBounds(30, 299, 722, 214);
	        add(scrollPane);

	        table = new JTable();
	        table.setModel(new DefaultTableModel(
	            new Object[][] {},
	            new String[] {"Rua", "CEP", "Número", "Bairro", "Cidade", "Estado", "Nome", "URL"}
	        ));
	        scrollPane.setViewportView(table);

	        JLabel lblUrl = new JLabel("URL:");
	        lblUrl.setBounds(30, 241, 280, 20);
	        add(lblUrl);

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

	        Connection conn = null;
	        PreparedStatement stmt = null;

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