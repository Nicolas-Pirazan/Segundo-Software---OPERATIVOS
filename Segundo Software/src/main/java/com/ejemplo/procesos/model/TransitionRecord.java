package main.java.com.ejemplo.procesos.model;

public class TransitionRecord {
    private final String processId;
    private final Process.State fromState;
    private final Process.State toState;
    private final int timeAfter;
    private final TransitionType transitionType;

    public enum TransitionType {
        DESPACHAR, EXPIRACION_TIEMPO, BLOQUEADO, DESPERTAR, TERMINAR, 
        SUSPENDIDOS, REANUDADOS, DESTRUIDOS
    }

    public TransitionRecord(String processId, Process.State fromState, Process.State toState, int timeAfter, TransitionType transitionType) {
        this.processId = processId;
        this.fromState = fromState;
        this.toState = toState;
        this.timeAfter = timeAfter;
        this.transitionType = transitionType;
    }

    public String getProcessId() { return processId; }
    public Process.State getFromState() { return fromState; }
    public Process.State getToState() { return toState; }
    public int getTimeAfter() { return timeAfter; }
    public TransitionType getTransitionType() { return transitionType; }
}