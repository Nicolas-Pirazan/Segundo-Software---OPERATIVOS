package main.java.com.ejemplo.procesos.view.panels;

import main.java.com.ejemplo.procesos.model.TransitionRecord;
import main.java.com.ejemplo.procesos.view.models.RegistryTableModel;
import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RegistryPanel extends JPanel {
    private final RegistryTableModel tableModel;
    private final JTable table;
    
    // Botones de filtro para states y transitions
    private final Map<String, JButton> stateFilterButtons;
    private final Map<TransitionRecord.TransitionType, JButton> transitionFilterButtons;
    private final JButton clearFilterButton;

    public RegistryPanel() {
        setLayout(new BorderLayout());
        
        // Panel de filtros
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(2, 1));
        
        // Filtros por estado (usando los valores del enum Process.State)
        JPanel statePanel = new JPanel();
        statePanel.setBorder(BorderFactory.createTitledBorder("Filtrar por Estado"));
        stateFilterButtons = new HashMap<>();
        // Para crear botones se usan los valores literales de estados usados en registros (se filtra por "fromState")
        String[] estados = {"LISTO", "EN_EJECUCION", "BLOQUEAR", "TERMINADO"};
        for (String est : estados) {
            JButton btn = new JButton(est);
            statePanel.add(btn);
            stateFilterButtons.put(est, btn);
        }
        
        // Filtros por transición
        JPanel transitionPanel = new JPanel();
        transitionPanel.setBorder(BorderFactory.createTitledBorder("Filtrar por Transición"));
        transitionFilterButtons = new EnumMap<>(TransitionRecord.TransitionType.class);
        for (TransitionRecord.TransitionType type : TransitionRecord.TransitionType.values()) {
            JButton btn = new JButton(type.name());
            transitionPanel.add(btn);
            transitionFilterButtons.put(type, btn);
        }
        
        // Panel contenedor de filtros
        JPanel filtros = new JPanel(new BorderLayout());
        filtros.add(statePanel, BorderLayout.NORTH);
        filtros.add(transitionPanel, BorderLayout.CENTER);
        
        // Botón para limpiar filtro
        clearFilterButton = new JButton("Limpiar Filtro");
        filtros.add(clearFilterButton, BorderLayout.SOUTH);
        
        filterPanel.add(filtros);
        add(filterPanel, BorderLayout.NORTH);
        
        tableModel = new RegistryTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    public void updateRegistry(java.util.List<TransitionRecord> records) {
        tableModel.setRecords(records);
        tableModel.fireTableDataChanged();
    }
    
    // Getters para que el controlador pueda añadir los listeners
    public Map<String, JButton> getStateFilterButtons() {
        return stateFilterButtons;
    }
    
    public Map<TransitionRecord.TransitionType, JButton> getTransitionFilterButtons() {
        return transitionFilterButtons;
    }
    
    public JButton getClearFilterButton() {
        return clearFilterButton;
    }
}