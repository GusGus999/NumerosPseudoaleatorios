package com.example.tablas;

public class TablaDistribucionNormal {

    public static double obtenerValorZ(double nivelConfianza) {
        // Solo los más comunes.
        if (nivelConfianza == 0.90) return 1.645;
        if (nivelConfianza == 0.95) return 1.960; // El estándar
        if (nivelConfianza == 0.98) return 2.326;
        if (nivelConfianza == 0.99) return 2.576;

        return 1.960; // Default al 95%
    }
}