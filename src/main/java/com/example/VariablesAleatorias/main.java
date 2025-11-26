package com.example.VariablesAleatorias;

import javax.swing.*;

public class main {

    public static void main(String[] args) {

        int opcion = 0;
        boolean continuarEnMenu = true; // Esta variable controla el ciclo

        do {
            String menu =
                    "===== MENÚ MÉTODOS DE GENERACIÓN =====\n" +
                            "1. Método de la Transformada Inversa (Exponencial)\n" +
                            "2. Método de Convolución (suma de 2 exponenciales)\n" +
                            "3. Método de Composición (mezcla de 2 exponenciales)\n" +
                            "4. Salir\n\n" +//
                            "Elige una opción (1-4):";

            String entrada = JOptionPane.showInputDialog(
                    null,
                    menu,
                    "Generador de Variables Aleatorias",
                    JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario presiona "Cancelar" o la X en el menú principal, salimos
            if (entrada == null) {
                continuarEnMenu = false;
                break;
            }

            try {
                opcion = Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            switch (opcion) {
                case 1:
                    String[] subOpciones = {"Uniforme", "Exponencial", "Volver"};
                    int subSeleccion;
                    do {
                        subSeleccion = JOptionPane.showOptionDialog(
                                null,
                                "Generación de Variables Aleatorias\nSeleccione el método:",
                                "Submenú de Simulación",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                subOpciones,
                                subOpciones[0]
                        );
                        switch (subSeleccion) {
                            case 0:
                                TransformadaInversaUniforme.ejecutarUniforme();
                                break;
                            case 1:
                                TransformadaInversaExponencial.ejecutarExponencial();
                                break;
                            case 2:
                                break;
                        }
                    } while (subSeleccion != 2 && subSeleccion != -1);
                    continuarEnMenu = preguntarSiContinuar();
                    break;
                case 2:
                    continuarEnMenu = preguntarSiContinuar();
                    break;
                case 3:
                    MetodoConvolucion.ejecutar();
                    continuarEnMenu = preguntarSiContinuar();
                    break;
                case 4:
                    MetodoComposicion.ejecutar();
                    continuarEnMenu = preguntarSiContinuar();
                    break;

                case 5:
                    // Opción SALIR
                    JOptionPane.showMessageDialog(null, "¡Hasta pronto!", "Fin", JOptionPane.INFORMATION_MESSAGE);
                    continuarEnMenu = false; // Esto rompe el ciclo
                    break;


                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Elija 1, 2 o 3.", "Error", JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (continuarEnMenu); // El ciclo solo depende de esta variable
    }

    // Método auxiliar para preguntar si desea volver al menú
    private static boolean preguntarSiContinuar() {
        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "¿Deseas regresar al menú principal?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        // Si responde SI (0) retorna true, si responde NO (1) retorna false
        return respuesta == JOptionPane.YES_OPTION;
    }
}