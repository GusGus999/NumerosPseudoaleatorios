package com.example.modelo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DatoGenerado {
    private final SimpleIntegerProperty n;
    private final SimpleStringProperty yn;
    private final SimpleStringProperty xn;
    private final SimpleDoubleProperty rn;

    public DatoGenerado(int n, String yn, String xn, double rn) {
        this.n = new SimpleIntegerProperty(n);
        this.yn = new SimpleStringProperty(yn);
        this.xn = new SimpleStringProperty(xn);
        this.rn = new SimpleDoubleProperty(rn);
    }

    // --- Getters (necesarios para PropertyValueFactory) ---
    public int getN() { return n.get(); }
    public String getYn() { return yn.get(); }
    public String getXn() { return xn.get(); }
    public double getRn() { return rn.get(); }
}