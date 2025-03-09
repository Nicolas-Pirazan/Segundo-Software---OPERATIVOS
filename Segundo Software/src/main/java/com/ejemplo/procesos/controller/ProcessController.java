package main.java.com.ejemplo.procesos.controller;

import main.java.com.ejemplo.procesos.model.*;
import main.java.com.ejemplo.procesos.model.Process;
import main.java.com.ejemplo.procesos.view.MainView;
import main.java.com.ejemplo.procesos.view.models.ProcessTableModel;
import main.java.com.ejemplo.procesos.view.panels.RegistryPanel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class ProcessController {
    private final ProcessModel model;
    private final MainView view;

    public ProcessController(ProcessModel model, MainView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        setupButtonListeners();
        addFilterListeners(); // nuevos listeners para filtros en RegistryPanel
        updateProcessTable();
    }

    private void setupButtonListeners() {
        view.getProcessPanel().getAddButton().addActionListener(this::addProcess);
        view.getProcessPanel().getEditButton().addActionListener(this::editProcess);
        view.getProcessPanel().getSimulateButton().addActionListener(e -> simulate());
        view.getExitButton().addActionListener(e -> System.exit(0));
        view.getClearButton().addActionListener(e -> clearProcesses());
    }
    
    private void addFilterListeners() {
        RegistryPanel regPanel = view.getRegistryPanel();
        
        // Listeners para botones de estado
        Map<String, JButton> stateButtons = regPanel.getStateFilterButtons();
        stateButtons.forEach((estado, btn) -> {
            btn.addActionListener((ActionEvent e) -> {
                // Filtrar por estado (se asume que se filtra por el "fromState")
                Process.State st = Process.State.valueOf(estado);
                List<TransitionRecord> filtered = model.filterRegistryByState(st);
                regPanel.updateRegistry(filtered);
            });
        });
        
        // Listeners para botones de transición
        Map<TransitionRecord.TransitionType, JButton> transitionButtons = regPanel.getTransitionFilterButtons();
        transitionButtons.forEach((transType, btn) -> {
            btn.addActionListener((ActionEvent e) -> {
                List<TransitionRecord> filtered = model.filterRegistryByTransition(transType);
                regPanel.updateRegistry(filtered);
            });
        });
        
        // Listener para limpiar filtro
        regPanel.getClearFilterButton().addActionListener(e ->
            regPanel.updateRegistry(model.getRegistry())
        );
    }

    private void addProcess(ActionEvent e) {
        JTextField nameField = new JTextField(10);
        JTextField timeField = new JTextField(5);
        JTextField priorityField = new JTextField(5);
        // Campo para ingresar procesos con los que se desea comunicar (separados por coma)
        JTextField communicationField = new JTextField(15);
        JCheckBox bloqueadoCheck = new JCheckBox("Bloqueado");
        JCheckBox suspendidoCheck = new JCheckBox("Suspendido");
        JCheckBox reanudadoCheck = new JCheckBox("Reanudado");
        JCheckBox destruidoCheck = new JCheckBox("Destruido");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre del Proceso:"));
        panel.add(nameField);
        panel.add(new JLabel("Tiempo:"));
        panel.add(timeField);
        panel.add(new JLabel("Prioridad:"));
        panel.add(priorityField);
        panel.add(new JLabel("Comunicar con (separar con ,):"));
        panel.add(communicationField);
        panel.add(bloqueadoCheck);
        panel.add(suspendidoCheck);
        panel.add(reanudadoCheck);
        panel.add(destruidoCheck);

        int result = JOptionPane.showConfirmDialog(null, panel, "Nuevo Proceso", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nameField.getText();
                int time = Integer.parseInt(timeField.getText());
                int prioridad = Integer.parseInt(priorityField.getText());
                boolean bloqueado = bloqueadoCheck.isSelected();
                boolean suspendido = suspendidoCheck.isSelected();
                boolean reanudado = reanudadoCheck.isSelected();
                boolean destruido = destruidoCheck.isSelected();

                Process process = new Process(nombre, time, prioridad, bloqueado, suspendido, reanudado, destruido);
                model.addProcess(process);

                String commInput = communicationField.getText().trim();
                if (!commInput.isEmpty()) {
                    List<Process> existing = model.getProcesses().stream()
                        .filter(p -> !p.getNombre().equals(process.getNombre()))
                        .toList();
                    if (existing.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No existen otros procesos para comunicar con.");
                    } else {
                        String[] nombresComunicacion = commInput.split(",");
                        for (String nombreComm : nombresComunicacion) {
                            String nom = nombreComm.trim();
                            existing.stream()
                                .filter(p -> p.getNombre().equals(nom))
                                .findFirst()
                                .ifPresent(process::communicate);
                        }
                    }
                }
                updateProcessTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Datos numéricos inválidos");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private void editProcess(ActionEvent e) {
        int selectedRow = view.getProcessPanel().getTable().getSelectedRow();
        if (selectedRow == -1) return;

        ProcessTableModel tblModel = view.getProcessPanel().getTableModel();
        Process process = tblModel.getProcessAt(selectedRow);

        JTextField timeField = new JTextField(String.valueOf(process.getTime()));
        JTextField priorityField = new JTextField(String.valueOf(process.getPrioridad()));
        JCheckBox bloqueadoCheck = new JCheckBox("Bloqueado", process.isBloqueado());
        JCheckBox suspendidoCheck = new JCheckBox("Suspendido", process.isSuspendido());
        JCheckBox reanudadoCheck = new JCheckBox("Reanudado", process.isReanudado());
        JCheckBox destruidoCheck = new JCheckBox("Destruido", process.isDestruido());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Tiempo:"));
        panel.add(timeField);
        panel.add(new JLabel("Prioridad:"));
        panel.add(priorityField);
        panel.add(bloqueadoCheck);
        panel.add(suspendidoCheck);
        panel.add(reanudadoCheck);
        panel.add(destruidoCheck);

        int result = JOptionPane.showConfirmDialog(null, panel, "Editar Proceso", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newTime = Integer.parseInt(timeField.getText());
                int newPrioridad = Integer.parseInt(priorityField.getText());
                boolean bloqueado = bloqueadoCheck.isSelected();
                boolean suspendido = suspendidoCheck.isSelected();
                boolean reanudado = reanudadoCheck.isSelected();
                boolean destruido = destruidoCheck.isSelected();

                this.model.editProcess(process.getNombre(), newTime, newPrioridad, bloqueado, suspendido, reanudado, destruido);
                updateProcessTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Datos numéricos inválidos");
            }
        }
    }

    private void simulate() {
        model.simulate();
        updateProcessTable();
        view.getRegistryPanel().updateRegistry(model.getRegistry());
    }

    private void clearProcesses() {
        model.clearProcesses();
        model.clearRegistry();
        updateProcessTable();
        view.getRegistryPanel().updateRegistry(model.getRegistry());
    }

    private void updateProcessTable() {
        view.getProcessPanel().getTableModel().setProcesses(model.getProcesses());
        view.getProcessPanel().getTableModel().fireTableDataChanged();
    }
}