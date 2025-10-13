package com.example.metodos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Implementa el algoritmo de generación de números pseudoaleatorios "Productos Medios".
 * Este método utiliza dos semillas iniciales (X0 y X1) para generar una secuencia.
 */
public class ProductosMedios {

    /**
     * Representa una fila de resultados para la tabla de la interfaz de usuario.
     * Un 'record' es una forma moderna y concisa de crear una clase inmutable para almacenar datos.
     *
     * @param iteracion El número de la iteración (i).
     * @param semilla1 La primera semilla usada en la iteración (X_{i-1}).
     * @param semilla2 La segunda semilla usada en la iteración (X_{i}).
     * @param producto El resultado de multiplicar semilla1 * semilla2.
     * @param xn El nuevo número generado (la nueva semilla, X_{i+1}).
     * @param rn El número pseudoaleatorio normalizado (r_n).
     */
    public record ResultadoProductosMedios(int iteracion, long semilla1, long semilla2, String producto, long xn, double rn) {}

    /**
     * Genera una lista de números pseudoaleatorios usando el método de productos medios.
     *
     * @param semilla0 La primera semilla inicial (X0).
     * @param semilla1 La segunda semilla inicial (X1).
     * @param n El número de iteraciones a realizar.
     * @return Una ObservableList con los resultados de cada iteración.
     * @throws IllegalArgumentException si las semillas no tienen la misma cantidad de dígitos o si tienen 3 o menos dígitos.
     */
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