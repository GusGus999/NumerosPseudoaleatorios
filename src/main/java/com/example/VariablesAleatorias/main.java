package com.example.VariablesAleatorias;

import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) {

        int opcion = 0;

        do {
            String menu =
                    "===== MENÚ MÉTODOS DE GENERACIÓN =====\n" +
                            "1. Método de la Transformada Inversa (Exponencial)\n" +
                            "2. Método de Convolución (suma de 2 exponenciales)\n" +
                            "3. Método de Composición (mezcla de 2 exponenciales)\n" +
                            "4. Salir\n\n" +
                            "Elige una opción (1-4):";

            String entrada = JOptionPane.showInputDialog(
                    null,
                    menu,
                    "Menú métodos aleatorios",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (entrada == null) { // Cancelar = salir
                break;
            }

            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Opción inválida, ingresa un número de 1 a 4.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            switch (opcion) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:
                    JOptionPane.showMessageDialog(
                            null,
                            "Saliendo del programa...",
                            "Salir",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
                default:
                    JOptionPane.showMessageDialog(
                            null,
                            "Opción inválida, ingresa un número de 1 a 4.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    break;
            }

        } while (opcion != 4);
    }

}
