package upa;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.EventQueue;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Adm extends JFrame {
    private JTextField textFieldRua;
    private JTextField textFieldCEP;
    private JTextField textFieldNumero;
    private JTextField textFieldBairro;
    private JTextField textFieldCidade;
    private JTextField textFieldEstado;
    private JTextField textFieldNome;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtFieldCpf;
    private JTextField txtFieldTelefone;
    private JTextField txtFieldNome;
    private JTextField txtFieldEmail;
    private JTextField txtFieldSenha;
    private JComboBox<String> comboBoxIdUnidade;
    private JCheckBox checkBoxAdmin;
    private DefaultTableModel tableModelGestores;
    private JTable table_1;
    private JTextField txtFieldDescricao;
    private JTable table_2;

    public Adm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabbedPane.addTab("Cadastro Unidade", null, contentPane, null);

        JLabel lblRua = new JLabel("Rua:");
        lblRua.setBounds(30, 30, 70, 20);
        contentPane.add(lblRua);

        textFieldRua = new JTextField();
        textFieldRua.setColumns(10);
        textFieldRua.setBounds(110, 30, 200, 20);
        contentPane.add(textFieldRua);

        JLabel lblCEP = new JLabel("CEP:");
        lblCEP.setBounds(30, 60, 70, 20);
        contentPane.add(lblCEP);

        textFieldCEP = new JTextField();
        textFieldCEP.setColumns(10);
        textFieldCEP.setBounds(110, 60, 200, 20);
        contentPane.add(textFieldCEP);

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setBounds(30, 90, 70, 20);
        contentPane.add(lblNumero);

        textFieldNumero = new JTextField();
        textFieldNumero.setColumns(10);
        textFieldNumero.setBounds(110, 90, 200, 20);
        contentPane.add(textFieldNumero);

        JLabel lblBairro = new JLabel("Bairro:");
        lblBairro.setBounds(30, 120, 70, 20);
        contentPane.add(lblBairro);

        textFieldBairro = new JTextField();
        textFieldBairro.setColumns(10);
        textFieldBairro.setBounds(110, 120, 200, 20);
        contentPane.add(textFieldBairro);

        JLabel lblCidade = new JLabel("Cidade:");
        lblCidade.setBounds(30, 150, 70, 20);
        contentPane.add(lblCidade);

        textFieldCidade = new JTextField();
        textFieldCidade.setColumns(10);
        textFieldCidade.setBounds(110, 150, 200, 20);
        contentPane.add(textFieldCidade);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 180, 70, 20);
        contentPane.add(lblEstado);

        textFieldEstado = new JTextField();
        textFieldEstado.setColumns(10);
        textFieldEstado.setBounds(110, 180, 200, 20);
        contentPane.add(textFieldEstado);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 210, 70, 20);
        contentPane.add(lblNome);

        textFieldNome = new JTextField();
        textFieldNome.setColumns(10);
        textFieldNome.setBounds(110, 210, 200, 20);
        contentPane.add(textFieldNome);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		salvarUnidade();
        	}
        });
        btnSalvar.setBounds(499, 30, 253, 54);
        contentPane.add(btnSalvar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(499, 103, 253, 55);
        contentPane.add(btnVoltar);

        JButton btn = new JButton("Editar");
        btn.setBounds(499, 179, 253, 55);
        contentPane.add(btn);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 299, 722, 214);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "Rua", "CEP", "Número", "Bairro", "Cidade", "Estado", "Nome"
            }
        ));
        scrollPane.setViewportView(table);

        JLabel lblUrl = new JLabel("URL:");
        lblUrl.setBounds(30, 241, 280, 20);
        contentPane.add(lblUrl);

        tableModel = (DefaultTableModel) table.getModel();
        preencherTabelaComUnidades();

        getContentPane().add(tabbedPane);
        
        JPanel contentPane_1 = new JPanel();
        contentPane_1.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabbedPane.addTab("Cadastro Gestor", null, contentPane_1, null);
        contentPane_1.setLayout(null);

        JLabel lblCPF = new JLabel("CPF:");
        lblCPF.setBounds(30, 30, 70, 20);
        contentPane_1.add(lblCPF);

        txtFieldCpf = new JTextField();
        txtFieldCpf.setBounds(110, 30, 200, 20);
        txtFieldCpf.setColumns(10);
        contentPane_1.add(txtFieldCpf);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 60, 70, 20);
        contentPane_1.add(lblTelefone);

        txtFieldTelefone = new JTextField();
        txtFieldTelefone.setBounds(110, 60, 200, 20);
        txtFieldTelefone.setColumns(10);
        contentPane_1.add(txtFieldTelefone);

        JLabel lblNome_1 = new JLabel("Nome:");
        lblNome_1.setBounds(30, 90, 70, 20);
        contentPane_1.add(lblNome_1);

        txtFieldNome = new JTextField();
        txtFieldNome.setBounds(110, 90, 200, 20);
        txtFieldNome.setColumns(10);
        contentPane_1.add(txtFieldNome);

        JLabel lblUnidadeID = new JLabel("ID da Unidade:");
        lblUnidadeID.setBounds(10, 183, 100, 20);
        contentPane_1.add(lblUnidadeID);

        JButton btnSalvar_1 = new JButton("Salvar");
        btnSalvar_1.setBounds(424, 25, 238, 42);
        btnSalvar_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  salvarGestor();
        	}
        });
        contentPane_1.add(btnSalvar_1);

        comboBoxIdUnidade = new JComboBox<String>();
        comboBoxIdUnidade.setBounds(110, 182, 200, 22);
        contentPane_1.add(comboBoxIdUnidade);


        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(43, 296, 681, 225);
        contentPane_1.add(scrollPane_1);
        
        table_1 = new JTable();
        table_1.setModel(new DefaultTableModel(
        	new Object[][] {
        		{null, null, null, null, null, null},
        	},
        	new String[] {
        			"CPF", "NOME", "TELEFONE", "ID UNIDADE", "EMAIL", "Administrador"
        	}
        ));
        preencherTabelaComGestores();
        scrollPane_1.setViewportView(table_1);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 122, 70, 20);
        contentPane_1.add(lblEmail);

        txtFieldEmail = new JTextField();
        txtFieldEmail.setBounds(110, 122, 200, 20);
        txtFieldEmail.setColumns(10);
        contentPane_1.add(txtFieldEmail);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 152, 70, 20);
        contentPane_1.add(lblSenha);

        txtFieldSenha = new JTextField();
        txtFieldSenha.setBounds(110, 152, 200, 20);
        txtFieldSenha.setColumns(10);
        contentPane_1.add(txtFieldSenha);

        checkBoxAdmin = new JCheckBox("Administrador");
        checkBoxAdmin.setBounds(10, 229, 106, 23);
        contentPane_1.add(checkBoxAdmin);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(424, 89, 238, 42);
        contentPane_1.add(btnExcluir);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(424, 151, 238, 42);
        contentPane_1.add(btnEditar);

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(43, 296, 681, 225);
        contentPane_1.add(scrollPane_2);
        

        	
        	preencherComboBoxComUnidades();

        	getContentPane().add(tabbedPane);
        	
        	JPanel panel = new JPanel();
        	tabbedPane.addTab("Cadastro Especialidade", null, panel, null);
        	panel.setLayout(null);
        	
        	JLabel lblNovaDescricao = new JLabel("Especialidade:");
        	lblNovaDescricao.setBounds(10, 157, 220, 49);
        	panel.add(lblNovaDescricao);
        	
        	txtFieldDescricao = new JTextField();
        	txtFieldDescricao.setBounds(154, 157, 220, 49);
        	panel.add(txtFieldDescricao);
        	
        	JButton btnAdicionar = new JButton("Adicionar");
        	btnAdicionar.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			adicionarEspecialidade();
        		}
        	});
        	btnAdicionar.setBounds(503, 45, 220, 49);
        	panel.add(btnAdicionar);
        	
        	JScrollPane scrollPane_3 = new JScrollPane();
        	scrollPane_3.setBounds(48, 276, 454, 231);
        	panel.add(scrollPane_3);
        	
        	table_2 = new JTable();
        	table_2.setModel(new DefaultTableModel(
        		new Object[][] {
        			{null, null},
        		},
        		new String[] {
        			"Id_especialidade", "Nome_especialidade"
        		}
        	));
        	scrollPane_3.setViewportView(table_2);
        	
        	JButton btnExcluir_1 = new JButton("Exluir");
        	btnExcluir_1.setBounds(503, 115, 220, 49);
        	panel.add(btnExcluir_1);
        	
        	JButton btnEditar_1 = new JButton("Editar");
        	btnEditar_1.setBounds(503, 185, 220, 49);
        	panel.add(btnEditar_1);
        	  // Inicializa a conexão com o banco de dados
            conectarAoBanco();

            // Preenche a tabela de especialidades do banco de dados
            preencherTabelaEspecialidades();

            // Fecha a conexão com o banco de dados ao fechar a janela
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    desconectarDoBanco();
                }
            });

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Adm frameAdm = new Adm();
                    frameAdm.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
               	JOptionPane.showMessageDialog(null, "Cadastro concluido com sucesso!");
                textFieldRua.setText("");
                textFieldCEP.setText("");
                textFieldNumero.setText("");
                textFieldBairro.setText("");
                textFieldCidade.setText("");
                textFieldEstado.setText("");
                textFieldNome.setText("");
                preencherTabelaComUnidades();
            } else {
             JOptionPane.showMessageDialog(null, "Erro ao cadastrar unidade!");
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
    private void salvarGestor() {
        // Outros campos
        String cpf = txtFieldCpf.getText();
        String telefone = txtFieldTelefone.getText();
        String nome = txtFieldNome.getText();
        String idUnidade = (String) comboBoxIdUnidade.getSelectedItem();
        String email = txtFieldEmail.getText();
        String senhaGestor = txtFieldSenha.getText();
        
        // Use a variável de instância checkBoxAdmin
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
            	 JOptionPane.showMessageDialog(null, "Cadastro concluido com sucesso!");

                tableModel.addRow(new Object[]{cpf, nome, telefone, idUnidade, email, (adm == 1) ? "Sim" : "Não"});
                txtFieldCpf.setText("");
                txtFieldTelefone.setText("");
                txtFieldNome.setText("");
                txtFieldEmail.setText("");
                txtFieldSenha.setText("");
                checkBoxAdmin.setSelected(false); // Use a variável de instância para desmarcar o JCheckBox
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
            comboBoxIdUnidade.removeAllItems();

            // Preencher o ComboBox com os IDs das unidades
            while (rs.next()) {
                String idUnidade = rs.getString("id_unidade");
                comboBoxIdUnidade.addItem(idUnidade);
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
    	DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();

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
    private void conectarAoBanco() {
        Connection conn = Conexao.ConnectDb();
    }

    private void desconectarDoBanco() {
    	 Connection conn = Conexao.ConnectDb();
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void preencherTabelaEspecialidades() {
        DefaultTableModel tableModel = (DefaultTableModel) table_2.getModel();
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
    	Connection conn = null;
        String novaDescricao = txtFieldDescricao.getText().trim();

        if (!novaDescricao.isEmpty()) {
            try {
            	conn = Conexao.ConnectDb();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO especialidade_m (descricao_esp) VALUES (?)");
                stmt.setString(1, novaDescricao);
                stmt.executeUpdate();
                stmt.close();

                preencherTabelaEspecialidades(); // Atualiza a tabela após adicionar
                txtFieldDescricao.setText(""); // Limpa o campo de texto
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
