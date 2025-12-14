package com.example.modelo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DatoGenerado {
    private final SimpleIntegerProperty n;
    private final SimpleStringProperty yn;
    private final SimpleStringProperty xn;
    private final SimpleStringProperty rn;

    public DatoGenerado(int n, String yn, String xn, String rn) {
        this.n = new SimpleIntegerProperty(n);
        this.yn = new SimpleStringProperty(yn);
        this.xn = new SimpleStringProperty(xn);
        this.rn = new SimpleStringProperty(rn);
    }

    // --- Getters (necesarios para PropertyValueFactory) ---
    public int getN() { return n.get(); }
    public String getYn() { return yn.get(); }
    public String getXn() { return xn.get(); }
    public String getRn() { return rn.get(); }
}