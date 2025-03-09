package main.java.com.ejemplo.procesos;

import main.java.com.ejemplo.procesos.controller.ProcessController;
import main.java.com.ejemplo.procesos.model.ProcessModel;
import main.java.com.ejemplo.procesos.view.MainView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProcessModel model = new ProcessModel();
            MainView view = new MainView();
            new ProcessController(model, view);
            view.setVisible(true);
        });
    }
}