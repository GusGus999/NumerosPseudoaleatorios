package com.example.metodos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ProductosMedios {


    public record ResultadoProductosMedios(int iteracion, long semilla1, long semilla2, String producto, long xn, double rn) {}


    public ObservableList<ResultadoProductosMedios> generar(long semilla0, long semilla1, int n) throws IllegalArgumentException {
        String s_semilla0 = String.valueOf(semilla0);
        String s_semilla1 = String.valueOf(semilla1);

        // --- Validación de las entradas ---
        if (s_semilla0.length() != s_semilla1.length()) {
            throw new IllegalArgumentException("Las semillas deben tener la misma cantidad de dígitos.");
        }

        if (s_semilla0.length() <= 3) {
            throw new IllegalArgumentException("Las semillas deben tener más de 3 dígitos para evitar una rápida degeneración.");
        }

        int D = s_semilla0.length(); // Cantidad de dígitos
        ObservableList<ResultadoProductosMedios> resultados = FXCollections.observableArrayList();

        long currentSemilla1 = semilla0;
        long currentSemilla2 = semilla1;

        for (int i = 1; i <= n; i++) {
            long producto = currentSemilla1 * currentSemilla2;
            String s_producto = String.valueOf(producto);

            // Aseguramos que la longitud del producto sea par para poder extraer el centro correctamente.
            // Si es impar, se añade un '0' a la izquierda.
            if (s_producto.length() % 2 != 0) {
                s_producto = "0" + s_producto;
            }

            int len = s_producto.length();
            int startIndex = (len - D) / 2;

            // Extraer los D dígitos del centro
            String s_nuevaSemilla = s_producto.substring(startIndex, startIndex + D);
            long nuevaSemilla = Long.parseLong(s_nuevaSemilla);

            // Calcular el número aleatorio r_n
            double rn = nuevaSemilla / Math.pow(10, D);

            // Guardar el resultado de la iteración
            resultados.add(new ResultadoProductosMedios(i, currentSemilla1, currentSemilla2, String.valueOf(producto), nuevaSemilla, rn));

            // Actualizar las semillas para la siguiente iteración
            currentSemilla1 = currentSemilla2;
            currentSemilla2 = nuevaSemilla;
        }

        return resultados;
    }
}