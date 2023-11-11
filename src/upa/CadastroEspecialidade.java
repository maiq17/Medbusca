package upa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class CadastroEspecialidade extends JFrame {
    private JTextField textFieldDescricao;
    private JButton btnAdicionar;

    private Connection conn;
    private JScrollPane scrollPane;
    private JTable table;

    public CadastroEspecialidade() {
        setTitle("Cadastro de Especialidades");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(842, 582);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblNovaDescricao = new JLabel("Especialidade:");
        lblNovaDescricao.setBounds(10, 157, 220, 49);
        panel.add(lblNovaDescricao);

        textFieldDescricao = new JTextField();
        textFieldDescricao.setBounds(154, 157, 220, 49);
        panel.add(textFieldDescricao);

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBounds(503, 45, 220, 49);
        panel.add(btnAdicionar);
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(48, 276, 454, 231);
        panel.add(scrollPane);
        
        table = new JTable();
        table.setModel(new DefaultTableModel(
        	new Object[][] {},
        	new String[] {
        		"Id_especialidade", "Nome_especialidade"
        	}
        ));
        scrollPane.setViewportView(table);
        
        JButton btnExcluir = new JButton("Exluir");
        btnExcluir.setBounds(503, 115, 220, 49);
        panel.add(btnExcluir);
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(503, 185, 220, 49);
        panel.add(btnEditar);

        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarEspecialidade();
            }
        });

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
    	Connection conn = null;
        String novaDescricao = textFieldDescricao.getText().trim();

        if (!novaDescricao.isEmpty()) {
            try {
            	conn = Conexao.ConnectDb();
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroEspecialidade frame = new CadastroEspecialidade();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
