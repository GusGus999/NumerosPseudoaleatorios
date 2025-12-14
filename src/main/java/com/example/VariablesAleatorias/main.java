package com.example.VariablesAleatorias;

import com.example.metodos.CuadrosMedios;
import static com.example.metodos.CuadrosMedios.*;
import static com.example.metodos.MultiplicadorConstante.*;
import static com.example.metodos.ProductosMedios.*;
import static com.example.metodos.CuadrosMedios.*;
import static mx.edu.itzitacuaro.tecNM.utilidades.Utility.*;
import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) {
        int opcion;
        do {
            String[] metodosOVariables = {
                    "Metodos para generar numeros aleatorios",
                    "Metodos para generar variables aleatorias"
            };

            opcion = getOptionMenu(metodosOVariables,"Seleccione un tema", "Simulacion");

            switch (opcion){
                case 1:
                    numerosAleatorios();
                    break;
                case 2:
                    variablesAleatorias();
                    break;
            }
        } while (continuar("¿Quieres volver a elegir un tema?"));

    }

    public static void numerosAleatorios(){
        int opcion;

        do {
            String[] metodos = {
                    "Cuadrados medios",
                    "Multiplicador Constante",
                    "Productos Medios",
                    "Congruencial Aditivo",
                    "Congruencial Multiplicativo",
                    "Congruencial Lineal",
                    "Congruencial Cuadratico"
            };

            opcion = getOptionMenu(metodos, "Generadores", "Numeros Pseudoaleatorios");

            switch (opcion) {
                case 1: //----------Cuadrados Medios----------
                    int semilla_1 = Integer.parseInt(JOptionPane.showInputDialog(null,"Ingrese la semilla", "Racabando datos",JOptionPane.QUESTION_MESSAGE));
                    int n_1 = Integer.parseInt(JOptionPane.showInputDialog(null,"¿Cuantos numeros desea generar?", "Racabando datos",JOptionPane.QUESTION_MESSAGE));
                    cuadradosMedios(semilla_1,n_1);
                    break;
                case 2: //----------Multiplicador Constante----------
                    int semilla_2 = Integer.parseInt(JOptionPane.showInputDialog(null,"Ingrese la semilla", "Racabando datos",JOptionPane.QUESTION_MESSAGE));
                    int a_2 = Integer.parseInt(JOptionPane.showInputDialog(null,"Ingrese el multiplicador", "Racabando datos",JOptionPane.QUESTION_MESSAGE));
                    int n_2 = Integer.parseInt(JOptionPane.showInputDialog(null,"¿Cuantos numeros desea generar?", "Racabando datos",JOptionPane.QUESTION_MESSAGE));
                    multiplicadorConstante(a_2,semilla_2, n_2);
                    break;
                case 3: //----------Productos Medios----------
                    break;
                case 4: //----------Congruencial Aditivo----------
                    break;
                case 5: //----------Congruencial Multiplicativo---------
                    break;
                case 6: //----------Congruencial Lineal-----------
                    break;
                case 7: //----------Congruencial Cuadratico-----------
                    break;
            }
        } while (continuar("Volver a los generadores"));
    }


    public static void variablesAleatorias(){
        int opcion = 0;
        boolean continuarEnMenu = true;

        do {
            String menu =
                    "===== MENÚ MÉTODOS DE GENERACIÓN =====\n" +
                            "1. Método de la Transformada Inversa\n" +
                            "2. Método de Convolución\n" +
                            "3. Método de Composición\n" +
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
                                "Generación de Variables Aleatorias\nSeleccione la distribucion:",
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
                    MetodoConvolucion.ejecutar();
                    continuarEnMenu = preguntarSiContinuar();
                    break;
                case 3:
                    MetodoComposicion.ejecutar();
                    continuarEnMenu = preguntarSiContinuar();
                    break;
                case 4:
                    // Opción SALIR
                    JOptionPane.showMessageDialog(null, "¡Hasta pronto!", "Fin", JOptionPane.INFORMATION_MESSAGE);
                    continuarEnMenu = false; // Esto rompe el ciclo
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Elija 1, 2 o 3.", "Error", JOptionPane.WARNING_MESSAGE);
                    break;
            }
        } while (continuarEnMenu);
    }

    public static int getOptionMenu(String[] opciones, String title, String encabezado) {
        String menu = title + "\n";
        for (int i = 0; i < opciones.length; i++) {
            menu += (i + 1) + ".- " + opciones[i] + "\n";
        }
        menu += (opciones.length + 1) + ".- Salir\n";
        menu += "Seleccione su opción: [1... " + (opciones.length + 1) + "]";
        int opcion = 0;
        do {
            opcion = leerInt(menu, encabezado);
            if (opcion < 1 || opcion > (opciones.length + 1)) {
                mostrar("Opción inválida, por favor seleccione una opción válida.");
            }
        } while (opcion < 1 || opcion > (opciones.length + 1));
        return opcion;
    }

    //Metodo auxiliar para preguntar si desea volver al menu
    private static boolean preguntarSiContinuar() {
        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "¿Deseas regresar al menú principal?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return respuesta == JOptionPane.YES_OPTION;
    }

    public static int leerInt(String texto, String encabezado) {
        String entrada = JOptionPane.showInputDialog(null, texto, encabezado, JOptionPane.QUESTION_MESSAGE);
        int n = Integer.parseInt(entrada);
        return n;
    }
}