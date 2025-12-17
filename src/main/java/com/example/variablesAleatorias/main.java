package com.example.variablesAleatorias;

import static com.example.metodos.CongruencialAditivo.congruencialAditivo;
import static com.example.metodos.CongruencialCuadratico.congruencialCuadratico;
import static com.example.metodos.CongruencialLineal.congruencialLineal;
import static com.example.metodos.CongruencialLineal.obtenerFactoresPrimos;
import static com.example.metodos.CongurencialMultiplicativo.calcularMCD;
import static com.example.metodos.CongurencialMultiplicativo.congruencialMultiplicativo;
import static com.example.metodos.CuadrosMedios.*;
import static com.example.metodos.MultiplicadorConstante.*;
import static com.example.metodos.ProductosMedios.productosMedios;
import static mx.edu.itzitacuaro.tecNM.utilidades.Utility.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;

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
                    try {
                        // 1. Pedir la semilla (y0)
                        String inputY0 = JOptionPane.showInputDialog(null,
                                "Ingrese la semilla inicial (y0):\n(Se recomienda de 4 dígitos)",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        // Si el usuario da click en Cancelar, inputY0 será null
                        if (inputY0 == null) return;
                        int semilla = Integer.parseInt(inputY0);

                        // 2. Pedir la cantidad de iteraciones (n)
                        String inputN = JOptionPane.showInputDialog(null,
                                "Ingrese la cantidad de números a generar (n):",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        if (inputN == null) return;

                        int n = Integer.parseInt(inputN);

                        // 3. Llamar al metodo con los datos ingresados
                        cuadradosMedios(semilla, n);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Error: Por favor ingrese solo números enteros válidos.",
                                "Error de formato",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 2: //----------Multiplicador Constante----------
                    try {
                        // 1. Pedir la semilla (y0)
                        String inputY0 = JOptionPane.showInputDialog(null,
                                "Ingrese la semilla inicial (y0):\n(Se recomienda de 4 dígitos)",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        // Si el usuario da click en Cancelar, inputY0 será null
                        if (inputY0 == null) return;
                        int semilla = Integer.parseInt(inputY0);

                        // 2. Pedir la constante
                        String inputA = JOptionPane.showInputDialog(null,
                                "Ingrese el valor de la constante multiplicadora(a):",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        if (inputA == null) return;
                        int a = Integer.parseInt(inputA);

                        // 3. Pedir la cantidad de iteraciones (n)
                        String inputN = JOptionPane.showInputDialog(null,
                                "Ingrese la cantidad de números a generar (n):",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        if (inputN == null) return;
                        int n = Integer.parseInt(inputN);

                        // 4. Llamar al metodo con los datos ingresados
                        multiplicadorConstante(a, semilla, n);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Error: Por favor ingrese solo números enteros válidos.",
                                "Error de formato",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 3: //----------Productos Medios----------
                    try {
                        // 1. Pedir la semilla (y0)
                        String inputY0 = JOptionPane.showInputDialog(null,
                                "Ingrese la semilla inicial (y0):\n(Se recomienda de 4 dígitos)",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        // Si el usuario da click en Cancelar, inputY0 será null
                        if (inputY0 == null) return;
                        int semilla1 = Integer.parseInt(inputY0);

                        // 1. Pedir la semilla (y0)
                        String inputY1 = JOptionPane.showInputDialog(null,
                                "Ingrese la segunda semilla (y1):\n(Se recomienda de 4 dígitos)",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        // Si el usuario da click en Cancelar, inputY0 será null
                        if (inputY1 == null) return;
                        int semilla2 = Integer.parseInt(inputY1);

                        // 3. Pedir la cantidad de iteraciones (n)
                        String inputN = JOptionPane.showInputDialog(null,
                                "Ingrese la cantidad de números a generar (n):",
                                "Entrada de Datos",
                                JOptionPane.QUESTION_MESSAGE);

                        if (inputN == null) return;
                        int n = Integer.parseInt(inputN);

                        // 4. Llamar al metodo con los datos ingresados
                        productosMedios(semilla1, semilla2, n);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Error: Por favor ingrese solo números enteros válidos.",
                                "Error de formato",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 4: //----------Congruencial Aditivo----------
                    try {
                        // 1. Pedir la cantidad de semillas
                        String inputCantidad = JOptionPane.showInputDialog(null,
                                "¿Cuántas semillas desea ingresar?",
                                "Configuración - Paso 1",
                                JOptionPane.QUESTION_MESSAGE);

                        if (inputCantidad == null) return; // Si cancela
                        int cantidadSemillas = Integer.parseInt(inputCantidad);

                        if (cantidadSemillas < 2) {
                            JOptionPane.showMessageDialog(null, "El método requiere al menos 2 semillas.");
                            return;
                        }

                        // 2. Pedir las semillas una por una
                        ArrayList<Integer> semillas = new ArrayList<>();
                        for (int i = 0; i < cantidadSemillas; i++) {
                            String inputSemilla = JOptionPane.showInputDialog(null,
                                    "Ingrese la semilla número " + (i + 1) + ":",
                                    "Ingresando semillas (" + (i + 1) + "/" + cantidadSemillas + ")",
                                    JOptionPane.QUESTION_MESSAGE);

                            if (inputSemilla == null) return; // Si cancela a mitad del proceso
                            semillas.add(Integer.parseInt(inputSemilla));
                        }

                        // 3. Pedir el módulo
                        String inputM = JOptionPane.showInputDialog(null,
                                "Ingrese el valor del módulo (m):",
                                "Configuración - Paso Final",
                                JOptionPane.QUESTION_MESSAGE);

                        if (inputM == null) return;
                        int m = Integer.parseInt(inputM);

                        // 4. Ejecutar algoritmo
                        congruencialAditivo(semillas, m);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Error: Por favor ingrese solo números enteros válidos.",
                                "Error de Formato",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 5: //----------Congruencial Multiplicativo---------
                    try {
                        // 1. Pedir Semilla (X0)
                        String inputX0 = JOptionPane.showInputDialog(null,
                                "Ingrese la semilla inicial (X0):\n(Debe ser impar)",
                                "Paso 1: Semilla", JOptionPane.QUESTION_MESSAGE);
                        if (inputX0 == null) return;
                        int x0 = Integer.parseInt(inputX0);

                        // Validar que X0 sea impar (Recomendación fuerte)
                        if (x0 % 2 == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Advertencia: X0 es par. El periodo será muy corto.",
                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                        }

                        // 2. Pedir Multiplicador (a)
                        String inputA = JOptionPane.showInputDialog(null,
                                "Ingrese el multiplicador (a):",
                                "Paso 2: Multiplicador", JOptionPane.QUESTION_MESSAGE);
                        if (inputA == null) return;
                        int a = Integer.parseInt(inputA);

                        // 3. Pedir 'd' para calcular m = 2^d
                        int d = 0;
                        boolean dValido = false;

                        while (!dValido) {
                            String inputD = JOptionPane.showInputDialog(null,
                                    "Ingrese el valor de 'd' (para m = 2^d):\nNota: d debe ser mayor a 3.",
                                    "Paso 3: Exponente", JOptionPane.QUESTION_MESSAGE);
                            if (inputD == null) return;

                            d = Integer.parseInt(inputD);

                            if (d > 3) {
                                dValido = true;
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Error: El valor de 'd' debe ser estrictamente mayor a 3.",
                                        "Valor inválido", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // Calcular m y verificar primos relativos
                        int m = (int) Math.pow(2, d);
                        int mcd = calcularMCD(a, m);

                        if (mcd != 1) {
                            JOptionPane.showMessageDialog(null,
                                    "ERROR CRÍTICO:\n" +
                                            "El multiplicador 'a' (" + a + ") y el módulo 'm' (" + m + ") NO son primos relativos.\n" +
                                            "Su Máximo Común Divisor es: " + mcd + ".\n\n" +
                                            "El generador no es válido.",
                                    "Falla de Primos Relativos", JOptionPane.ERROR_MESSAGE);
                            return; // Detener el programa
                        }

                        // Ejecutar algoritmo
                        congruencialMultiplicativo(x0, a, m);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 6: //----------Congruencial Lineal-----------
                    try {
                        // --- ENTRADA DE DATOS ---

                        // X0
                        String inX0 = JOptionPane.showInputDialog(null, "Ingrese la semilla (X0):", "Paso 1", JOptionPane.QUESTION_MESSAGE);
                        if(inX0==null) return;
                        int x0 = Integer.parseInt(inX0);

                        // a
                        String inA = JOptionPane.showInputDialog(null, "Ingrese el multiplicador (a):", "Paso 2", JOptionPane.QUESTION_MESSAGE);
                        if(inA==null) return;
                        int a = Integer.parseInt(inA);

                        // c
                        String inC = JOptionPane.showInputDialog(null, "Ingrese la constante aditiva (c):", "Paso 3", JOptionPane.QUESTION_MESSAGE);
                        if(inC==null) return;
                        int c = Integer.parseInt(inC);

                        // m
                        String inM = JOptionPane.showInputDialog(null, "Ingrese el módulo (m): ", "Paso 4", JOptionPane.QUESTION_MESSAGE);
                        if(inM==null) return;
                        int m = Integer.parseInt(inM);

                        // --- VALIDACIONES DEL TEOREMA DE HULL-DOBELL ---

                        // 0. Validación básica m > 0
                        if (m <= 0) {
                            JOptionPane.showMessageDialog(null, "Error: El módulo (m).", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // 1. c y m deben ser primos relativos (MCD = 1)
                        if (calcularMCD(c, m) != 1) {
                            JOptionPane.showMessageDialog(null,
                                    "'c' y 'm' NO son primos relativos.\n" +
                                            "MCD(" + c + ", " + m + ") != 1.\n" +
                                            "Esto evitará que se genere un periodo completo.",
                                    "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // 2. Si q es factor primo de m, entonces a-1 debe ser múltiplo de q
                        ArrayList<Integer> factoresPrimosM = obtenerFactoresPrimos(m);
                        int aMenos1 = a - 1;

                        for (int primo : factoresPrimosM) {
                            if (aMenos1 % primo != 0) {
                                JOptionPane.showMessageDialog(null,
                                        "El número " + primo + " es factor primo de m (" + m + "),\n" +
                                                "pero (a-1) = " + aMenos1 + " NO es divisible entre " + primo + ".",
                                        "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        // 3. Si m es múltiplo de 4, entonces a-1 debe ser múltiplo de 4
                        // Nota: Interpretamos "4 debe ser múltiplo de a-1" como "(a-1) es divisible por 4" según el teorema estándar.
                        if (m % 4 == 0) {
                            if (aMenos1 % 4 != 0) {
                                JOptionPane.showMessageDialog(null,
                                        "Como m (" + m + ") es múltiplo de 4,\n" +
                                                "(a-1) = " + aMenos1 + " también debe ser múltiplo de 4.",
                                        "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        congruencialLineal(x0, a, c, m);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 7: //----------Congruencial Cuadratico-----------
                    try {
                        // Pedir Semilla X0
                        String inX0 = JOptionPane.showInputDialog(null, "Ingrese la semilla (X0):", "Paso 1: Semilla", JOptionPane.QUESTION_MESSAGE);
                        if(inX0 == null) return;
                        int x0 = Integer.parseInt(inX0);

                        // Pedir 'a' y validar que sea PAR
                        int a = 0;
                        boolean aValido = false;
                        while (!aValido) {
                            String inA = JOptionPane.showInputDialog(null, "Ingrese 'a'\nDebe ser un numero par:", "Paso 2: Coeficiente Cuadrático", JOptionPane.QUESTION_MESSAGE);
                            if(inA == null) return;
                            a = Integer.parseInt(inA);

                            if (a % 2 == 0) {
                                aValido = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Error: 'a'\nDebe ser un número PAR.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // Pedir 'b' y validar (b-1) mod 4 = 1
                        int b = 0;
                        boolean bValido = false;
                        while (!bValido) {
                            String inB = JOptionPane.showInputDialog(null, "Ingrese 'b'\nCondición: (b-1) mod 4 = 1):", "Paso 3: Coeficiente Lineal", JOptionPane.QUESTION_MESSAGE);
                            if(inB == null) return;
                            b = Integer.parseInt(inB);

                            // Validación específica solicitada
                            if ((b - 1) % 4 == 1) {
                                bValido = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Error:\nNo se cumple que (b-1) mod 4 = 1.\nEjemplo válido: 26 (25 mod 4 = 1).", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // Pedir 'c' y validar que sea IMPAR
                        int c = 0;
                        boolean cValido = false;
                        while (!cValido) {
                            String inC = JOptionPane.showInputDialog(null, "Ingrese 'c'\nDebe ser un numero impar:", "Paso 4: Constante Aditiva", JOptionPane.QUESTION_MESSAGE);
                            if(inC == null) return;
                            c = Integer.parseInt(inC);

                            if (c % 2 != 0) {
                                cValido = true;
                            } else {
                                JOptionPane.showMessageDialog(null, "Error: 'c'\nDebe ser un número impar.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        // Pedir 'd' para calcular m = 2^d
                        String inD = JOptionPane.showInputDialog(null, "Ingrese el valor de 'd'\nSe usa para m=2^d:", "Paso 5: Módulo", JOptionPane.QUESTION_MESSAGE);
                        if(inD == null) return;
                        int d = Integer.parseInt(inD);
                        if (d <= 0) {
                            JOptionPane.showMessageDialog(null, "El valor de d debe ser positivo.");
                            return;
                        }
                        int m = (int) Math.pow(2, d);

                        // Llamada al metodo
                        congruencialCuadratico(x0, a, b, c, d, m);

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
        } while (continuar("Volver a los generadores"));
    }



    //-------------------------Opcion de variables aleatorias---------------------------

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
        menu += "Seleccione su opción: [1..." + (opciones.length + 1) + "]";
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