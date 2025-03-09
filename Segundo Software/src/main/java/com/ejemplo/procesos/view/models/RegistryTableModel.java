package main.java.com.ejemplo.procesos.view.models;

import main.java.com.ejemplo.procesos.model.TransitionRecord;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RegistryTableModel extends AbstractTableModel {
    private final String[] COLUMNS = {"Proceso", "Tiempo"};
    private List<TransitionRecord> records = new ArrayList<>();

    @Override
    public int getRowCount() { 
        return records.size(); 
    }

    @Override
    public int getColumnCount() { 
        return COLUMNS.length; 
    }

    @Override
    public String getColumnName(int column) { 
        return COLUMNS[column]; 
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TransitionRecord record = records.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> record.getProcessId();
            case 1 -> record.getTimeAfter();
            default -> throw new IllegalArgumentException("Índice de columna inválido");
        };
    }

    public void setRecords(List<TransitionRecord> records) {
        this.records = new ArrayList<>(records);
        fireTableDataChanged();
    }
}