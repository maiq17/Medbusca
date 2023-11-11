package upa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        // Defina o tamanho do JFrame
        setSize(495, 454);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Menu Principal");

        // Crie um painel para conter os botões
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        // Defina o layout do painel como GridLayout
        panel.setLayout(new GridLayout(5, 1));

        // Crie botões para cada funcionalidade
        JButton btnCadastroEspecialidade = new JButton("Cadastro de Especialidade");
        JButton btnCadastroGestor = new JButton("Cadastro de Gestor");
        JButton btnCadastroUnidade = new JButton("Cadastro de Unidade");

        // Estilize os botões
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        btnCadastroEspecialidade.setFont(buttonFont);
        btnCadastroGestor.setFont(buttonFont);
        btnCadastroUnidade.setFont(buttonFont);

        // Adicione os botões ao painel
        panel.add(btnCadastroEspecialidade);
        panel.add(btnCadastroGestor);
        panel.add(btnCadastroUnidade);

        // Adicione ouvintes de ação aos botões para direcionar para as telas correspondentes
        btnCadastroEspecialidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroEspecialidade cadastroEspecialidade = new CadastroEspecialidade();
                cadastroEspecialidade.setVisible(true);
            }
        });

        btnCadastroGestor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroGestor cadastroGestor = new CadastroGestor();
                cadastroGestor.setVisible(true);
            }
        });

        btnCadastroUnidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CadastroUnidade cadastroUnidade = new CadastroUnidade();
                cadastroUnidade.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuPrincipal frame = new MenuPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
