package upa;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.border.EmptyBorder;

public class tela extends JFrame {
    private static JComboBox<String> comboBoxCRM;
    private static JTextField txtDataHoraFim;
    private static JTextField txtdataHoraInicio;
    private static JLabel lblEspecialidade;
    private static JLabel lblNome;
    private static JLabel lblDataI;
    private static JLabel lblCRM;
    private static JTable table;
    private static JLabel lblusuario;
    private static JButton btnNewButton_4;
    private static JButton btnNewButton_5;
    private static JTextField txtNome;
    private static JTextField txtEspecialidade;
    private static JTabbedPane tabbedPane;
    private JPanel contentPane;
    private JLabel lblNome_1;
    private JTextField textField;
    private JLabel lblCrm;
    private JTextField textField_1;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JComboBox<String> comboBoxEspecialidade;
    private JLabel lblEspecialidade_1;
    private JButton btnExcluirMedico;
    private JScrollPane scrollPane_1;
    private JTable table_1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
  
                try {
                    tela frameTela = new tela();
                    frameTela.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    
    
    
    
    private static String obterNomeDoGestor(int cpfGestor) {
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String nomeGestor = null;

        try {
        	conn = Conexao.ConnectDb();
            String sql = "SELECT nome FROM gestor WHERE cpf = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, telaLogin.cpfGestor);
            rs = stmt.executeQuery();

            if (rs.next()) {
                nomeGestor = rs.getString("nome");
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

        return nomeGestor;
    }

    private static String[] obterInfoDoMedicoPeloCRM(String crm) {
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        	conn = Conexao.ConnectDb();
            String sql = "SELECT m.nome_medico, e.descricao_esp " +
                    "FROM medico m " +
                    "JOIN especialidade_m e ON m.especialidade = e.id_especialidade_m " +
                    "WHERE m.crm_medico = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, crm);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String nomeMedico = rs.getString("nome_medico");
                String descricaoEspecialidade = rs.getString("descricao_esp");
                return new String[]{nomeMedico, descricaoEspecialidade};
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
        return null;
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
                    Object[] row = new Object[5];
                    row[0] = rs.getString("crm_medico");
                    row[1] = rs.getString("nome_medico");
                    row[2] = rs.getString("especialidade");
                    row[3] = rs.getString("dataHoraInicio");
                    row[4] = rs.getString("dataHoraFim");
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


    public tela() {
        setTitle("Tela Upa");
        setSize(915, 755);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();


        JPanel novaAbaPanel = new JPanel();
        novaAbaPanel.setLayout(null);

        tabbedPane.addTab("Disponibilidade Medica", novaAbaPanel);

        getContentPane().add(tabbedPane);

        comboBoxCRM = new JComboBox<>();
        comboBoxCRM.setBounds(10, 72, 133, 20);
        novaAbaPanel.add(comboBoxCRM);
        preencherCRMMedicos(comboBoxCRM);
      
        txtDataHoraFim = new JTextField();
        txtDataHoraFim.setBounds(192, 166, 133, 20);
        txtDataHoraFim.setColumns(10);
        novaAbaPanel.add(txtDataHoraFim);

        txtdataHoraInicio = new JTextField();
        txtdataHoraInicio.setBounds(192, 122, 133, 20);
        txtdataHoraInicio.setColumns(10);
        novaAbaPanel.add(txtdataHoraInicio);

        lblEspecialidade = new JLabel("Especialidade");
        lblEspecialidade.setBounds(33, 153, 82, 14);
        novaAbaPanel.add(lblEspecialidade);

        lblNome = new JLabel("Nome");
        lblNome.setBounds(50, 103, 46, 14);
        novaAbaPanel.add(lblNome);

        lblDataI = new JLabel("Data-Hora-Inicio");
        lblDataI.setBounds(215, 109, 95, 14);
        novaAbaPanel.add(lblDataI);

        lblCRM = new JLabel("CRM");
        lblCRM.setBounds(50, 59, 46, 14);
        novaAbaPanel.add(lblCRM);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(143, 197, 785, 325);
        novaAbaPanel.add(scrollPane);

        table = new JTable();
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {
                {false, null, null, null, "", null, null, null},
            },
            new String[] {
                "CRM", "NOME", "ESPECIALIDADE", "DATA-HORA-INICIO", "DATA-HORA-FIM"
            }
        );

        table.setModel(new DefaultTableModel(
        	new Object[][] {
        		{null, null, null, "", null},
        	},
        	new String[] {
        		"CRM", "NOME", "ESPECIALIDADE", "DATA-HORA-INICIO", "DATA-HORA-FIM"
        	}
        ));
        table.getColumnModel().getColumn(1).setPreferredWidth(113);
        table.getColumnModel().getColumn(2).setPreferredWidth(102);
        table.getColumnModel().getColumn(3).setPreferredWidth(114);
        table.getColumnModel().getColumn(4).setPreferredWidth(97);

        table.setModel(model);
        scrollPane.setViewportView(table);
        atualizarTabela(model);

        JButton btnNewButton = new JButton("Adicionar");
        btnNewButton.setBounds(428, 131, 148, 23);
        novaAbaPanel.add(btnNewButton);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection conexao = Conexao.ConnectDb();

                if (conexao != null) {
                    String CRM = (String) comboBoxCRM.getSelectedItem();
                    String nome = txtNome.getText();
                    String especialidade = txtEspecialidade.getText();
                    String dataHoraInicio = txtdataHoraInicio.getText();
                    String dataHoraFim = txtDataHoraFim.getText();

                    try {
                        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                        Date parsedDataHoraInicio = dateTimeFormat.parse(dataHoraInicio);
                        Date parsedDataHoraFim = dateTimeFormat.parse(dataHoraFim);

                        String horaInicio = dateTimeFormat.format(parsedDataHoraInicio);
                        String horaFim = dateTimeFormat.format(parsedDataHoraFim);

                        String inserirSQL = "INSERT INTO disponibilidade_medico (crm_medico, dataHoraInicio, dataHoraFim, id_unidade) VALUES (?, ?, ?, ?)";
                        PreparedStatement pstmt = conexao.prepareStatement(inserirSQL);
                        pstmt.setString(1, CRM);
                        pstmt.setTimestamp(2, new Timestamp(parsedDataHoraInicio.getTime()));
                        pstmt.setTimestamp(3, new Timestamp(parsedDataHoraFim.getTime()));
                        pstmt.setInt(4, telaLogin.unidade);

                        pstmt.executeUpdate();

                        // Agora, vamos buscar os dados da ViewDisponibilidadeMedico e enviar para o outro banco
                        String query = "SELECT * FROM ViewDisponibilidadeMedico";
                        PreparedStatement statement = conexao.prepareStatement(query);
                        ResultSet resultSet = statement.executeQuery();

                        // Chamar a função enviarDadosParaOutroBanco passando o ResultSet
                        enviarDadosParaOutroBanco(resultSet);

                        JOptionPane.showMessageDialog(null, "Médico inserido com sucesso!");

                        txtdataHoraInicio.setText("");
                        txtDataHoraFim.setText("");

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    } finally {
                        try {
                            atualizarTabela(model);
                            conexao.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });


        JButton btnNewButton_1 = new JButton("Remover");
        btnNewButton_1.setBounds(428, 163, 148, 23);
        novaAbaPanel.add(btnNewButton_1);

        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                	
                    JOptionPane.showMessageDialog(null, "Selecione um médico para remover.");
                    return;
                }

                String CRM = (String) model.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o médico: " + CRM + "?");

                if (confirm == JOptionPane.YES_OPTION) {
                    Connection conexao = Conexao.ConnectDb();

                    if (conexao != null) {
                        try {
                            String removerSQL = "DELETE FROM disponibilidade_medico WHERE crm_medico = ?";
                            PreparedStatement pstmt = conexao.prepareStatement(removerSQL);
                            pstmt.setString(1, CRM);
                            pstmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Médico removido com sucesso.");
                            atualizarTabela(model);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        } finally {
                        	atualizarTabela(model);
                            try {
                                conexao.close();}
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        JButton btnNewButton_2 = new JButton("Editar");
        btnNewButton_2.setBounds(584, 131, 148, 23);
        novaAbaPanel.add(btnNewButton_2);

        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Selecione um médico para atualizar.");
                    return;
                }

                String CRM = (String) model.getValueAt(selectedRow, 1);
                String nome = txtNome.getText();
                String especialidade = txtEspecialidade.getText();
                String dataInicio = txtdataHoraInicio.getText();
                String dataFim = txtDataHoraFim.getText();

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                    Date parsedDataInicio = dateFormat.parse(dataInicio);
                    Date parsedDataFim = dateFormat.parse(dataFim);

                    Connection conexao = Conexao.ConnectDb();
                    if (conexao != null) {
                        String atualizarSQL = "UPDATE medico SET nome_medico = ?, especialidade = ?, data_inicio = ?, data_fim = ?, horario_inicio = ?, horario_fim = ? WHERE CRM = ?";
                        PreparedStatement pstmt = conexao.prepareStatement(atualizarSQL);
                        pstmt.setString(1, nome);
                        pstmt.setString(2, especialidade);
                        pstmt.setDate(3, new java.sql.Date(parsedDataInicio.getTime()));
                        pstmt.setDate(4, new java.sql.Date(parsedDataFim.getTime()));
                        pstmt.setString(7, CRM);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Médico atualizado com sucesso!");
                        atualizarTabela(model);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        lblusuario = new JLabel("Usuário");
        lblusuario.setBounds(709, 28, 86, 14);
        novaAbaPanel.add(lblusuario);
        String nomeGestor = obterNomeDoGestor(telaLogin.cpfGestor);
        lblusuario.setText("Usuário: " + nomeGestor);

        btnNewButton_4 = new JButton("Sair");
        btnNewButton_4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton_4.setBounds(693, 89, 133, 23);
        novaAbaPanel.add(btnNewButton_4);

        btnNewButton_5 = new JButton("Mudar Senha");
        btnNewButton_5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton_5.setBounds(693, 55, 133, 23);
        novaAbaPanel.add(btnNewButton_5);

        txtNome = new JTextField();
        txtNome.setColumns(10);
        txtNome.setBounds(10, 120, 133, 20);
        novaAbaPanel.add(txtNome);

        txtEspecialidade = new JTextField();
        txtEspecialidade.setColumns(10);
        txtEspecialidade.setBounds(10, 166, 133, 20);
        novaAbaPanel.add(txtEspecialidade);
        
        JLabel lblDataI_1 = new JLabel("Data-Hora-Fim");
        lblDataI_1.setBounds(215, 153, 95, 14);
        novaAbaPanel.add(lblDataI_1);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        tabbedPane.addTab("Gerenciar Medicos", null, contentPane, null);
        contentPane.setLayout(null);
        
        lblNome_1 = new JLabel("Nome:");
        lblNome_1.setBounds(10, 81, 70, 20);
        contentPane.add(lblNome_1);
        
        textField = new JTextField();
        textField.setBounds(79, 81, 200, 20);
        textField.setColumns(10);
        contentPane.add(textField);
        
        lblCrm = new JLabel("CRM:");
        lblCrm.setBounds(10, 125, 70, 20);
        contentPane.add(lblCrm);
        
        textField_1 = new JTextField();
        textField_1.setBounds(79, 125, 200, 20);
        textField_1.setColumns(10);
        contentPane.add(textField_1);
        

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBounds(598, 444, 173, 52);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarMedico();
                
                
            }
        });
        contentPane.add(btnSalvar);
        
        
        btnEditar = new JButton("Editar");
        btnEditar.setBounds(598, 316, 173, 52);
        contentPane.add(btnEditar);
        
        comboBoxEspecialidade = new JComboBox<String>();
        comboBoxEspecialidade.setBounds(79, 165, 200, 22);
        contentPane.add(comboBoxEspecialidade);
        
        lblEspecialidade_1 = new JLabel("Especialidade:");
        lblEspecialidade_1.setBounds(10, 166, 70, 20);
        contentPane.add(lblEspecialidade_1);
        
        btnExcluirMedico = new JButton("Excluir Médico");
        btnExcluirMedico.setBounds(598, 379, 173, 54);
        contentPane.add(btnExcluirMedico);
        btnExcluirMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table_1.getSelectedRow();

                if (selectedRow == -1) {
                	
                    JOptionPane.showMessageDialog(null, "Selecione um médico para remover.");
                    return;
                }

                String CRM = (String) model.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o médico com o CRM: " + CRM + "?");

                if (confirm == JOptionPane.YES_OPTION) {
                    Connection conexao = Conexao.ConnectDb();

                    if (conexao != null) {
                        try {
                            String removerSQL = "DELETE FROM medico WHERE crm_medico = ?";
                            PreparedStatement pstmt = conexao.prepareStatement(removerSQL);
                            pstmt.setString(1, CRM);
                            pstmt.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Médico removido com sucesso.");
                            atualizarTabela(model);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        } finally {
                        	atualizarTabela(model);
                            try {
                                conexao.close();}
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(10, 276, 523, 220);
        contentPane.add(scrollPane_1);
        
        table_1 = new JTable();
        table_1.setModel(new DefaultTableModel(
        	new Object[][] {
        		{null, null, null},
        	},
        	new String[] {
        		"CRM", "NOME", "ESPECIALIDADE"
        	}
        ));
        atualizarTabelaMedico((DefaultTableModel) table_1.getModel());
        preencherEspecialidades();
        preencherTabelaMedicos();
       
        scrollPane_1.setViewportView(table_1);
        
        comboBoxCRM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCRM = (String) comboBoxCRM.getSelectedItem();
                if (selectedCRM != null) {
                    String[] medicoInfo = obterInfoDoMedicoPeloCRM(selectedCRM);
                    if (medicoInfo != null) {
                        txtNome.setText(medicoInfo[0]);
                        txtEspecialidade.setText(medicoInfo[1]);
                    } else {
                        txtNome.setText("");
                        txtEspecialidade.setText(" ");
                    }
                }
            }
        });

        novaAbaPanel.setVisible(true);
    }
    private void salvarMedico() {
        String nome = textField.getText();
        String crm = textField_1.getText();
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
                preencherTabelaMedicos();

            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar o médico.");
            }

            stmt.close();
            conn.close();

            textField.setText("");
            textField_1.setText("");

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
    public void atualizarTabelaMedico(DefaultTableModel model) {
        Connection conexao = Conexao.ConnectDb();

        if (conexao != null) {
            try {
                String consultaSQL = "SELECT m.crm_medico, m.nome_medico, e.descricao_esp " +
                        "FROM medico m " +
                        "JOIN especialidade_m e ON m.especialidade = e.id_especialidade_m " +
                        "WHERE m.id_unidade = ?";

                PreparedStatement pstmt = conexao.prepareStatement(consultaSQL);
                pstmt.setInt(1, telaLogin.unidade);

                ResultSet rs = pstmt.executeQuery();

                model.setRowCount(0);

                while (rs.next()) {
                    String crm = rs.getString("crm_medico");
                    String nome = rs.getString("nome_medico");
                    String especialidade = rs.getString("descricao_esp");

                    // Adicione a linha à tabela
                    model.addRow(new Object[]{crm, nome, especialidade});
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
    private void preencherTabelaMedicos() {
        DefaultTableModel tableModel = (DefaultTableModel) table_1.getModel();
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
    public void enviarDadosParaOutroBanco(ResultSet resultSet) {
        Connection connDestino = ConexaoDestino.ConnectDb(); // Conexão com o banco de destino

        if (connDestino != null) {
            try {
                while (resultSet.next()) {
                    String crmMedico = resultSet.getString("crm_medico");
                    String dataHoraInicio = resultSet.getString("dataHoraInicio");
                    String dataHoraFim = resultSet.getString("dataHoraFim");
                    String descricaoEspecialidade = resultSet.getString("descricao_esp");
                    String ruaUnidade = resultSet.getString("rua_unidade");
                    String numeroUnidade = resultSet.getString("numero_unidade");
                    String bairroUnidade = resultSet.getString("bairro_unidade");
                    String cidadeUnidade = resultSet.getString("cidade_unidade");
                    String estadoUnidade = resultSet.getString("estado_unidade");

                    String inserirSQL = "INSERT INTO tabela_destino (crm_medico, dataHoraInicio, dataHoraFim, descricao_esp, rua_unidade, numero_unidade, bairro_unidade, cidade_unidade, estado_unidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = connDestino.prepareStatement(inserirSQL);
                    pstmt.setString(1, crmMedico);
                    pstmt.setString(2, dataHoraInicio);
                    pstmt.setString(3, dataHoraFim);
                    pstmt.setString(4, descricaoEspecialidade);
                    pstmt.setString(5, ruaUnidade);
                    pstmt.setString(6, numeroUnidade);
                    pstmt.setString(7, bairroUnidade);
                    pstmt.setString(8, cidadeUnidade);
                    pstmt.setString(9, estadoUnidade);

                    pstmt.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Dados enviados com sucesso para o banco de destino!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } finally {
                try {
                    connDestino.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}