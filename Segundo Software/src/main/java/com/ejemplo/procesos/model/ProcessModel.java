package main.java.com.ejemplo.procesos.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ProcessModel {
    private final List<Process> processes = new ArrayList<>();
    private final List<TransitionRecord> registry = new ArrayList<>();
    private final Queue<Process> processQueue = new LinkedList<>();
    private static final int QUANTUM = 5; // Quantum de tiempo fijo

    public List<TransitionRecord> filterRegistryByState(Process.State state) {
        List<TransitionRecord> filteredRecords = new ArrayList<>();
        for (TransitionRecord record : registry) {
            if (record.getFromState() == state) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public List<TransitionRecord> filterRegistryByTransition(TransitionRecord.TransitionType transitionType) {
        List<TransitionRecord> filteredRecords = new ArrayList<>();
        for (TransitionRecord record : registry) {
            if (record.getTransitionType() == transitionType) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public void addProcess(Process process) throws IllegalArgumentException {
        if (processes.stream().anyMatch(p -> p.getNombre().equals(process.getNombre()))) {
            throw new IllegalArgumentException("Nombre duplicado");
        }
        if (processes.stream().anyMatch(p -> p.getPrioridad() == process.getPrioridad())) {
            throw new IllegalArgumentException("Prioridad duplicada");
        }
        processes.add(process);
    }

    public void editProcess(String nombre, int time, int prioridad, boolean bloqueado, boolean suspendido, boolean reanudado, boolean destruido) {
        processes.stream()
            .filter(p -> p.getNombre().equals(nombre))
            .findFirst()
            .ifPresent(p -> {
                p.setTime(time);
                p.setPrioridad(prioridad);
                p.setBloqueado(bloqueado);
                p.setSuspendido(suspendido);
                p.setReanudado(reanudado);
                p.setDestruido(destruido);
            });
    }

    public void simulate() {
        // No se limpia el registry para conservar el historial de transiciones
        processQueue.clear();
        // Inicializar la cola: todos arrancan en estado LISTO
        processes.forEach(p -> {
            p.setCurrentState(Process.State.LISTO);
            processQueue.add(p);
        });

        while (!processQueue.isEmpty()) {
            Process process = processQueue.poll();

            // Si el proceso está suspendido, registramos SUSPENDIDOS y simulamos su reanudación
            if (process.isSuspendido()) {
                registry.add(new TransitionRecord(
                    process.getNombre(),
                    process.getCurrentState(),
                    Process.State.SUSPENDIDO,
                    process.getTime(),
                    TransitionRecord.TransitionType.SUSPENDIDOS
                ));
                process.setCurrentState(Process.State.SUSPENDIDO);
                // Simulación: el proceso suspendido se "reanuda" automáticamente
                process.setSuspendido(false);
                process.setReanudado(true);
            }

            // Si se ha marcado para reanudarse, se registra y se limpia el flag
            if (process.isReanudado()) {
                registry.add(new TransitionRecord(
                    process.getNombre(),
                    process.getCurrentState(),
                    Process.State.REANUDADO,
                    process.getTime(),
                    TransitionRecord.TransitionType.REANUDADOS
                ));
                process.setCurrentState(Process.State.REANUDADO);
                process.setReanudado(false);
            }

            despachar(process); // Transición de LISTO a EN_EJECUCION
            ejecutar(process);

            // Si el proceso está en estado BLOQUEAR, simular que se despierta (DESPERTAR) y pasa a LISTO
            if (process.getCurrentState() == Process.State.BLOQUEAR) {
                registry.add(new TransitionRecord(
                    process.getNombre(),
                    Process.State.BLOQUEAR,
                    Process.State.LISTO,
                    process.getTime(),
                    TransitionRecord.TransitionType.DESPERTAR
                ));
                process.setCurrentState(Process.State.LISTO);
            }

            // Si el proceso no ha terminado, se vuelve a encolar
            if (process.getCurrentState() != Process.State.TERMINADO) {
                processQueue.add(process);
            }
        }
    }

    private void despachar(Process process) {
        registry.add(new TransitionRecord(
            process.getNombre(),
            process.getCurrentState(),
            Process.State.EN_EJECUCION,
            process.getTime(),
            TransitionRecord.TransitionType.DESPACHAR
        ));
        process.setCurrentState(Process.State.EN_EJECUCION);
    }

    private void ejecutar(Process process) {
        // Si el proceso tiene el flag destruido, registrar DESTRUIDOS y terminarlo sin restar tiempo
        if (process.isDestruido()) {
            registry.add(new TransitionRecord(
                process.getNombre(),
                Process.State.EN_EJECUCION,
                Process.State.TERMINADO,
                process.getTime(),
                TransitionRecord.TransitionType.DESTRUIDOS
            ));
            process.setCurrentState(Process.State.TERMINADO);
            return;
        }
        // Ejecutar con el quantum
        int executionTime = Math.min(process.getTime(), QUANTUM);
        process.setTime(process.getTime() - executionTime);
        if (process.getTime() > 0) {
            if (process.isBloqueado()) {
                registry.add(new TransitionRecord(
                    process.getNombre(),
                    Process.State.EN_EJECUCION,
                    Process.State.BLOQUEAR,
                    process.getTime(),
                    TransitionRecord.TransitionType.BLOQUEADO
                ));
                process.setCurrentState(Process.State.BLOQUEAR);
            } else {
                registry.add(new TransitionRecord(
                    process.getNombre(),
                    Process.State.EN_EJECUCION,
                    Process.State.LISTO,
                    process.getTime(),
                    TransitionRecord.TransitionType.EXPIRACION_TIEMPO
                ));
                process.setCurrentState(Process.State.LISTO);
            }
        } else {
            registry.add(new TransitionRecord(
                process.getNombre(),
                Process.State.EN_EJECUCION,
                Process.State.TERMINADO,
                process.getTime(),
                TransitionRecord.TransitionType.TERMINAR
            ));
            process.setCurrentState(Process.State.TERMINADO);
        }
    }

    public void clearProcesses() {
        processes.clear();
        registry.clear();
    }

    public void clearRegistry() {
        registry.clear();
    }

    public List<Process> getProcesses() {
        return new ArrayList<>(processes);
    }

    public List<TransitionRecord> getRegistry() {
        return new ArrayList<>(registry);
    }
}