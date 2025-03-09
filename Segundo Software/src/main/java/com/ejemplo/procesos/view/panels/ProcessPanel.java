package main.java.com.ejemplo.procesos.view.panels;

import main.java.com.ejemplo.procesos.view.models.ProcessTableModel;
import javax.swing.*;
import java.awt.*;

public class ProcessPanel extends JPanel {
    private final ProcessTableModel tableModel;
    private final JTable table;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton simulateButton;

    public ProcessPanel() {
        setLayout(new BorderLayout());
        
        tableModel = new ProcessTableModel();
        table = new JTable(tableModel);
        
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Agregar");
        editButton = new JButton("Editar");
        simulateButton = new JButton("Simular");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(simulateButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getSimulateButton() { return simulateButton; }
    public ProcessTableModel getTableModel() { return tableModel; }
    public JTable getTable() { return table; }
}