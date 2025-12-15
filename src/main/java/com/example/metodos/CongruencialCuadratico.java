package com.example.metodos;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;

public class CongruencialCuadratico {
    /*public static void main(String[] args) {
        int x0 = 13; //Numero entero cualquiera
        int a = 26; //Debe ser un numero par
        int b = 26; //Debe verificar que (b-1) mod 4 = 1
        int c = 27; //Debe ser un numero impar
        int d = 10; //Entero positivo para determinar m
        int m = (int) Math.pow(2, d);

        congruencialCuadratico(x0, a, b, c, d -1, m);
    }*/
        public static void congruencialCuadratico(int x0, int a, int b, int c, int m) {
            StringBuilder salida = new StringBuilder();
            ArrayList<Double> listaRi = new ArrayList<>();

            salida.append("Parámetros: X0=" + x0 + ", a=" + a + ", b=" + b + ", c=" + c + ", m=" + m + "\n");
            salida.append("Fórmula: Xi+1 = (a*Xi² + b*Xi + c) mod m\n");

            // Cabecera de la tabla
            salida.append(String.format("%-6s %-10s %-25s %-15s %-10s\n",
                    "i", "Xi", "aXi² + bXi + c", "Xi+1 (Mod)", "Ri"));
            salida.append("-----------------------------------------------------------------------------\n");

            int xi = x0;
            int i = 0;
            int limiteSeguridad = m + 1000; // Seguridad por si el periodo es m completo

            do {
                // 1. Calcular la ecuación cuadrática
                // Usamos long para cada término para evitar desbordamiento antes de sumar
                long termino1 = (long) a * xi * xi;
                long termino2 = (long) b * xi;
                long operacion = termino1 + termino2 + c;

                // 2. Calcular siguiente (Módulo)
                int xiSiguiente = (int) (operacion % m);

                // 3. Calcular Ri
                double ri = (double) xiSiguiente / (m - 1);
                // Nota: En algunos textos usan 'm', en otros 'm-1'. Usamos m-1 para normalizar a 1.0 máx.

                listaRi.add(ri);

                salida.append(String.format("%-6d %-10d %-25d %-15d %.4f\n",
                        i, xi, operacion, xiSiguiente, ri));

                // Actualizar para siguiente iteración
                xi = xiSiguiente;
                i++;

                // --- DETECCIÓN DE CICLO (Periodo) ---
                if (xi == x0) {
                    salida.append("\nPeriodo completado. Volvimos a la semilla " + x0 + ".\n");
                    salida.append("Total de números generados: " + i + "\n");
                    break;
                }

                if (i >= limiteSeguridad) {
                    salida.append("\nAlerta: Se alcanzó el límite de seguridad (posible ciclo infinito no detectado).\n");
                    break;
                }

            } while (true);

            // --- Componente Visual ---
            JTextArea textArea = new JTextArea(salida.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(750, 450));

            Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};
            int seleccion = -1;

            do {
                seleccion = JOptionPane.showOptionDialog(
                        null, scrollPane, "Resultados - Congruencial Cuadrático",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, opciones, opciones[0]);

                switch (seleccion) {
                    case 0: realizarPruebaUniformidad(listaRi); break;
                    case 1: realizarPruebaAleatoriedad(listaRi); break;
                    case 2: realizarPruebaIndependenciaPoker(listaRi); break;
                    case 3: System.out.println("Saliendo..."); break;
                    default: break;
                }
            } while (seleccion != 3 && seleccion != -1);
        }

        public static void main(String[] args) {
            try {
                // 1. Pedir 'd' para calcular m = 2^d
                String inD = JOptionPane.showInputDialog(null, "Ingrese el valor de 'd' (para m=2^d):", "Paso 1: Módulo", JOptionPane.QUESTION_MESSAGE);
                if(inD == null) return;
                int d = Integer.parseInt(inD);
                if (d <= 0) {
                    JOptionPane.showMessageDialog(null, "El valor de d debe ser positivo.");
                    return;
                }
                int m = (int) Math.pow(2, d);

                // 2. Pedir Semilla X0
                String inX0 = JOptionPane.showInputDialog(null, "Ingrese la semilla (X0):", "Paso 2: Semilla", JOptionPane.QUESTION_MESSAGE);
                if(inX0 == null) return;
                int x0 = Integer.parseInt(inX0);

                // 3. Pedir 'a' y validar que sea PAR
                int a = 0;
                boolean aValido = false;
                while (!aValido) {
                    String inA = JOptionPane.showInputDialog(null, "Ingrese 'a' (Debe ser PAR):", "Paso 3: Coeficiente Cuadrático", JOptionPane.QUESTION_MESSAGE);
                    if(inA == null) return;
                    a = Integer.parseInt(inA);

                    if (a % 2 == 0) {
                        aValido = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: 'a' debe ser un número PAR.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // 4. Pedir 'b' y validar (b-1) mod 4 = 1
                int b = 0;
                boolean bValido = false;
                while (!bValido) {
                    String inB = JOptionPane.showInputDialog(null, "Ingrese 'b' (Condición: (b-1) mod 4 = 1):", "Paso 4: Coeficiente Lineal", JOptionPane.QUESTION_MESSAGE);
                    if(inB == null) return;
                    b = Integer.parseInt(inB);

                    // Validación específica solicitada
                    if ((b - 1) % 4 == 1) {
                        bValido = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: No se cumple que (b-1) mod 4 = 1.\nEjemplo válido: 26 (25 mod 4 = 1).", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // 5. Pedir 'c' y validar que sea IMPAR
                int c = 0;
                boolean cValido = false;
                while (!cValido) {
                    String inC = JOptionPane.showInputDialog(null, "Ingrese 'c' (Debe ser IMPAR):", "Paso 5: Constante Aditiva", JOptionPane.QUESTION_MESSAGE);
                    if(inC == null) return;
                    c = Integer.parseInt(inC);

                    if (c % 2 != 0) {
                        cValido = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Error: 'c' debe ser un número IMPAR.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                    }
                }

                // Resumen de validaciones
                JOptionPane.showMessageDialog(null,
                        "Validaciones Correctas:\n" +
                                "- m = 2^" + d + " = " + m + "\n" +
                                "- a es Par (" + a + ")\n" +
                                "- (b-1) mod 4 = 1 (" + b + " -> " + ((b-1)%4) + ")\n" +
                                "- c es Impar (" + c + ")\n\n" +
                                "Ejecutando...",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Llamada al metodo
                congruencialCuadratico(x0, a, b, c, m);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
}
