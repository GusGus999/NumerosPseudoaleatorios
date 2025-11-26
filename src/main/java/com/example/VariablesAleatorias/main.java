package com.example.VariablesAleatorias;

import com.example.VariablesAleatorias.MetodoComposicion;

import javax.swing.*;

public class main {

    public static void main(String[] args) {

        int opcion = 0;
        boolean continuar = true;

        do {
            String menu =
                    "===== MENÚ MÉTODOS DE GENERACIÓN =====\n" +
                            "1. Método de la Transformada Inversa (Exponencial)\n" +
                            "2. Método de la Transformada Inversa (Uniforme Discreta)\n" +
                            "3. Método de Convolución (suma de 2 exponenciales)\n" +
                            "4. Método de Composición (mezcla de 2 exponenciales)\n" +
                            "5. Salir\n\n" +
                            "Elige una opción (1-5):";

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
                    // TransformadaInversa.main(null);
                    TransformadaInversaExponencial.ejecutarExponencial();
                    JOptionPane.showMessageDialog(null, "Método aún no implementado");
                    continuar = preguntarSiContinuar();  // 👈 Preguntar
                    break;
                case 2:
                    TransformadaInversaUniforme.ejecutarUniforme();
                    break;

                case 3:
                    // Convolucion.main(null);
                    JOptionPane.showMessageDialog(null, "Método aún no implementado");
                    continuar = preguntarSiContinuar();  // 👈 Preguntar
                    break;

                case 4:
                    MetodoComposicion.ejecutar();
                    continuar = preguntarSiContinuar();  // 👈 Preguntar
                    break;

                case 5:
                    JOptionPane.showMessageDialog(
                            null,
                            "¡Hasta pronto!",
                            "Salir",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    continuar = false;  // 👈 Salir directamente
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

            // 👇 Si el usuario eligió NO continuar, salir del bucle
            if (!continuar) {
                break;
            }

        } while (opcion != 4);
    }

    // 👉 MÉTODO QUE PREGUNTA SI DESEA CONTINUAR
    private static boolean preguntarSiContinuar() {
        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "¿Deseas regresar al menú principal?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Si elige "Sí" (YES_OPTION), retorna true para continuar
        // Si elige "No" (NO_OPTION), retorna false para salir
        return respuesta == JOptionPane.YES_OPTION;
    }
}