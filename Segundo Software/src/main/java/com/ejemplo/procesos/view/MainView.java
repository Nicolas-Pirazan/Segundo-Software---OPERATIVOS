package main.java.com.ejemplo.procesos.view;

import main.java.com.ejemplo.procesos.view.panels.ProcessPanel;
import main.java.com.ejemplo.procesos.view.panels.RegistryPanel;
import javax.swing.*;
import java.awt.BorderLayout;

public class MainView extends JFrame {
    private final ProcessPanel processPanel;
    private final RegistryPanel registryPanel;
    private final JButton exitButton;
    private final JButton clearButton;

    public MainView() {
        setTitle("Simulador de Procesos");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // Se elimina el panel de filtros de la parte superior

        JTabbedPane tabbedPane = new JTabbedPane();
        processPanel = new ProcessPanel();
        registryPanel = new RegistryPanel();

        tabbedPane.addTab("Procesos", processPanel);
        tabbedPane.addTab("Registros", registryPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        exitButton = new JButton("Finalizar");
        clearButton = new JButton("Limpiar Procesos");

        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public ProcessPanel getProcessPanel() { return processPanel; }
    public RegistryPanel getRegistryPanel() { return registryPanel; }
    public JButton getExitButton() { return exitButton; }
    public JButton getClearButton() { return clearButton; }
}