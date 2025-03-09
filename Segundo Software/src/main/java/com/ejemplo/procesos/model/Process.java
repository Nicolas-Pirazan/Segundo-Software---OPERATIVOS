package main.java.com.ejemplo.procesos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Process implements Serializable {
    private final String nombre;
    private int time;
    private int prioridad;
    private boolean bloqueado;
    private boolean suspendido;
    private boolean reanudado;
    private boolean destruido;
    private final List<Process> comunicaciones;

    private State currentState;

    public enum State {
        LISTO, EN_EJECUCION, BLOQUEAR, SUSPENDIDO, REANUDADO, TERMINADO
    }

    public Process(String nombre, int time, int prioridad, boolean bloqueado, boolean suspendido, boolean reanudado, boolean destruido) {
        this.nombre = nombre;
        this.time = time;
        this.prioridad = prioridad;
        this.bloqueado = bloqueado;
        this.suspendido = suspendido;
        this.reanudado = reanudado;
        this.destruido = destruido;
        this.comunicaciones = new ArrayList<>();
        this.currentState = State.LISTO;
    }
    
    // getters y setters existentes...
    public String getNombre() { return nombre; }
    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }
    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }
    public boolean isSuspendido() { return suspendido; }
    public void setSuspendido(boolean suspendido) { this.suspendido = suspendido; }
    public boolean isReanudado() { return reanudado; }
    public void setReanudado(boolean reanudado) { this.reanudado = reanudado; }
    public boolean isDestruido() { return destruido; }
    public void setDestruido(boolean destruido) { this.destruido = destruido; }
    public State getCurrentState() { return currentState; }
    public void setCurrentState(State currentState) { this.currentState = currentState; }
    
    // Permite comunicar este proceso con otro
    public void communicate(Process other) {
        if (!comunicaciones.contains(other)) {
            comunicaciones.add(other);
        }
    }
    
    public List<Process> getComunicaciones() {
        return new ArrayList<>(comunicaciones);
    }
}