package com.example.pruebas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.example.tablas.TablaDistribucionNormal.obtenerValorZ;

public class Aleatoridad {

    public static void realizarPruebaAleatoriedad(ArrayList<Double> datos) {
        int n = datos.size();
        StringBuilder secuencia = new StringBuilder();

        int n0 = 0; // Cantidad de números < 0.5 (Ceros)
        int n1 = 0; // Cantidad de números >= 0.5 (Unos)
        int corridas = 0;

        // 1. Generar la secuencia de bits (0 o 1) y contar n0 y n1
        // Usamos una lista temporal de booleanos para facilitar el conteo de corridas después
        ArrayList<Integer> bits = new ArrayList<>();

        for (double num : datos) {
            if (num < 0.5) {
                secuencia.append("0");
                bits.add(0);
                n0++;
            } else {
                secuencia.append("1");
                bits.add(1);
                n1++;
            }
        }

        // 2. Contar las corridas (cambios de 0 a 1 o viceversa)
        if (n > 0) {
            corridas = 1; // La primera secuencia cuenta como 1 corrida
            int valorActual = bits.get(0);

            for (int i = 1; i < n; i++) {
                if (bits.get(i) != valorActual) {
                    corridas++; // Hubo un cambio, nueva corrida
                    valorActual = bits.get(i);
                }
            }
        }

        // 3. Cálculos Estadísticos (Fórmulas de Wald-Wolfowitz)
        // Media Esperada (Mu) = (2 * n0 * n1) / n + 0.5
        double mu = ((2.0 * n0 * n1) / n) + 0.5;

        // Varianza (Sigma^2) = [2*n0*n1 * (2*n0*n1 - n)] / [n^2 * (n - 1)]
        double numerador = 2.0 * n0 * n1 * (2.0 * n0 * n1 - n);
        double denominador = Math.pow(n, 2) * (n - 1);
        double varianza = numerador / denominador;
        double desvStd = Math.sqrt(varianza);

        // Estadístico Z0 = (Corridas - Mu) / DesvStd
        double zCalculado = (corridas - mu) / desvStd;
        // Tomamos el valor absoluto para prueba de dos colas
        double zCalculadoAbs = Math.abs(zCalculado);

        // 4. Obtener valor crítico (Z alpha/2 para 95% = 1.96)
        double zCritico = obtenerValorZ(0.95);

        // --- CONSTRUCCIÓN DEL REPORTE ---
        StringBuilder reporte = new StringBuilder();
        reporte.append("--- PRUEBA DE ALEATORIEDAD (CORRIDAS) ---\n\n");

        // Mostramos un fragmento de la secuencia visualmente
        reporte.append("Secuencia Generada (<0.5=0, >=0.5=1):\n");
        if (secuencia.length() > 60) {
            reporte.append(secuencia.substring(0, 60)).append("... (truncado)\n\n");
        } else {
            reporte.append(secuencia.toString()).append("\n\n");
        }

        reporte.append(String.format("Datos (n):   %d\n", n));
        reporte.append(String.format("Ceros (n0):  %d\n", n0));
        reporte.append(String.format("Unos (n1):   %d\n", n1));
        reporte.append(String.format("Corridas Observadas (Co): %d\n", corridas));
        reporte.append("------------------------------------------\n");
        reporte.append(String.format("Media Esperada (Mu):      %.4f\n", mu));
        reporte.append(String.format("Varianza:                 %.4f\n", varianza));
        reporte.append(String.format("Desviación Estándar:      %.4f\n", desvStd));
        reporte.append("------------------------------------------\n");
        reporte.append(String.format("Z Calculado (Z0):         %.4f\n", zCalculado));
        reporte.append(String.format("|Z0| Absoluto:            %.4f\n", zCalculadoAbs));
        reporte.append(String.format("Z Crítico (95%%):          %.4f\n\n", zCritico));

        // 5. Conclusión
        if (zCalculadoAbs < zCritico) {
            reporte.append("CONCLUSIÓN: NO SE RECHAZA H0.\nLos números se comportan de forma ALEATORIA (Independientes).");
        } else {
            reporte.append("CONCLUSIÓN: SE RECHAZA H0.\nLos números NO son independientes (Patrón detectado).");
        }

        // 6. Mostrar resultado
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Resultados Prueba de Corridas", JOptionPane.INFORMATION_MESSAGE);
    }
}
