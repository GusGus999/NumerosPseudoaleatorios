package com.example.tablas;

public class TablaDistribucionNormal {

    /**
     * Devuelve el valor Z crítico para una prueba de dos colas.
     * @param nivelConfianza (ej. 0.95 para 95%)
     * @return valor Z (ej. 1.96)
     */
    public static double obtenerValorZ(double nivelConfianza) {
        // Por simplicidad para este ejercicio escolar, manejamos los más comunes.
        // Si necesitas más precisión, se requeriría una implementación de la función de error inversa.

        if (nivelConfianza == 0.90) return 1.645;
        if (nivelConfianza == 0.95) return 1.960; // El estándar
        if (nivelConfianza == 0.98) return 2.326;
        if (nivelConfianza == 0.99) return 2.576;

        return 1.960; // Default al 95%
    }
}