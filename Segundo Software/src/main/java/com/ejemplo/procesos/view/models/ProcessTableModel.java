package main.java.com.ejemplo.procesos.view.models;

import main.java.com.ejemplo.procesos.model.Process;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessTableModel extends AbstractTableModel {
    private final String[] COLUMNS = {
        "Nombre", "Tiempo", "Prioridad", 
        "Estado Bloqueado", "Estado Suspendido", 
        "Estado Reanudado", "Estado Destruido",
        "Comunicaciones"
    };
    private List<Process> processes = new ArrayList<>();

    @Override
    public int getRowCount() { 
        return processes.size(); 
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
        Process process = processes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> process.getNombre();
            case 1 -> process.getTime();
            case 2 -> process.getPrioridad();
            case 3 -> process.isBloqueado() ? "BLOQUEADO" : "NO BLOQUEADO";
            case 4 -> process.isSuspendido() ? "SUSPENDIDO" : "NO SUSPENDIDO";
            case 5 -> process.isReanudado() ? "REANUDADO" : "NO REANUDADO";
            case 6 -> process.isDestruido() ? "DESTRUIDO" : "NO DESTRUIDO";
            case 7 -> {
                if (process.getComunicaciones().isEmpty()) {
                    yield "Ninguna";
                } else {
                    yield process.getComunicaciones().stream()
                         .map(Process::getNombre)
                         .collect(Collectors.joining(", "));
                }
            }
            default -> throw new IllegalArgumentException("Índice de columna inválido");
        };
    }

    public void setProcesses(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        fireTableDataChanged();
    }

    public Process getProcessAt(int rowIndex) {
        return processes.get(rowIndex);
    }
}