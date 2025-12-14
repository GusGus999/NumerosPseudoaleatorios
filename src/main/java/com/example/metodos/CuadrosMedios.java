package com.example.metodos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import static com.example.tablas.TablaChiCuadrada.*;
import static com.example.tablas.TablaDistribucionNormal.*;

public class CuadrosMedios {
    public static void main(String[] args) {
        int semilla = 5735; // Semilla inicial (debe tener más de 3 dígitos)
        int n = 10; // Cantidad de números aleatorios a generar

        cuadradosMedios(semilla, n);
    }

    public static void cuadradosMedios(int y0, int n) {
        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();

        salida.append(String.format("%-4s %-8s %-12s %-25s %-8s %-8s\n",
                "i", "Yi", "Yi²", "Yi²(Ceros izq)", "Xi", "Ri"));
        salida.append("---------------------------------------------------------------------------------\n");

        int yi = y0;

        for (int i = 0; i < n; i++) {
            long yiCuadrado = (long) Math.pow(yi, 2);
            String yiCuadradoStr = String.format("%08d", yiCuadrado);

            int xi = 0;
            if (yiCuadradoStr.length() >= 6) {
                xi = Integer.parseInt(yiCuadradoStr.substring(2, 6));
            } else {
                xi = Integer.parseInt(yiCuadradoStr);
            }

            double ri = xi / 10000.0;
            listaRi.add(ri);

            salida.append(String.format("%-4d %-8d %-12d %-25s %-8d %.4f\n",
                    i, yi, yiCuadrado, yiCuadradoStr, xi, ri));

            yi = xi;
        }

        // Preparamos el componente visual
        JTextArea textArea = new JTextArea(salida.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        // IMPORTANTE: Creamos el ScrollPane una sola vez para reutilizarlo
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

        // Agregamos una opción extra "Salir" al final
        Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};

        int seleccion = -1;

        do {
            // Mostramos el diálogo dentro del ciclo
            seleccion = JOptionPane.showOptionDialog(
                    null,
                    scrollPane, // Reutilizamos la tabla ya generada
                    "Resultados - Seleccione prueba estadística",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            switch (seleccion) {
                case 0:
                    realizarPruebaUniformidad(listaRi);
                    break;
                case 1:
                    realizarPruebaAleatoriedad(listaRi);
                    break;
                case 2:
                    realizarPruebaIndependenciaPoker(listaRi);
                    break;
                case 3:
                    System.out.println("Saliendo del menú de pruebas...");
                    break;
                default:
                    // Caso si cierran con la X (-1)
                    break;
            }
        } while (seleccion != 3 && seleccion != -1);
    }

    // --- Métodos Placeholder para las pruebas (Lógica vacía por ahora) ---

    public static void realizarPruebaUniformidad(ArrayList<Double> datos) {
        int n = datos.size();

        // 1. Definir número de intervalos (m).
        // Regla común: m = raíz cuadrada de n.
        // Si n es muy pequeño (ej. 10), forzamos mínimo 5 intervalos para que la prueba tenga sentido.
        int m = (int) Math.sqrt(n);
        if (m < 5) m = 5;

        double anchoIntervalo = 1.0 / m;
        int[] frecuenciaObservada = new int[m];
        double frecuenciaEsperada = (double) n / m; // En uniformidad, se espera lo mismo en todos

        // 2. Contar frecuencias observadas (En qué "cubeta" cae cada número)
        for (double ri : datos) {
            int indice = (int) (ri / anchoIntervalo);
            if (indice >= m) indice = m - 1; // Para el caso raro de que ri sea exactamente 1.0
            frecuenciaObservada[indice]++;
        }

        // 3. Construir la tabla de resultados y calcular Chi-Cuadrada
        StringBuilder reporte = new StringBuilder();
        reporte.append("--- PRUEBA DE UNIFORMIDAD (CHI-CUADRADA) ---\n");
        reporte.append(String.format("Datos (n): %d | Intervalos (m): %d | Esp. (E): %.2f\n\n", n, m, frecuenciaEsperada));
        reporte.append(String.format("%-15s %-10s %-10s %-15s\n", "Intervalo", "Obs(O)", "Esp(E)", "(O-E)²/E"));
        reporte.append("----------------------------------------------------\n");

        double chiCuadradaCalculada = 0.0;

        for (int i = 0; i < m; i++) {
            double limiteInf = i * anchoIntervalo;
            double limiteSup = (i + 1) * anchoIntervalo;

            // Fórmula: (O - E)² / E
            double termino = Math.pow(frecuenciaObservada[i] - frecuenciaEsperada, 2) / frecuenciaEsperada;
            chiCuadradaCalculada += termino;

            String intervaloStr = String.format("[%.2f - %.2f)", limiteInf, limiteSup);
            reporte.append(String.format("%-15s %-10d %-10.2f %-15.4f\n",
                    intervaloStr, frecuenciaObservada[i], frecuenciaEsperada, termino));
        }

        reporte.append("----------------------------------------------------\n");
        reporte.append(String.format("Chi-Cuadrada Calculada: %.4f\n", chiCuadradaCalculada));

        // 4. Obtener valor crítico de tabla (Grados de libertad = m - 1)
        int gradosLibertad = m - 1;
        double valorCritico = obtenerValorCritico(gradosLibertad);

        reporte.append(String.format("Valor Crítico (Tabla, gl=%d): %.4f\n\n", gradosLibertad, valorCritico));

        // 5. Conclusión
        if (chiCuadradaCalculada < valorCritico) {
            reporte.append("CONCLUSIÓN: NO SE RECHAZA H0.\nLos números PARECEN UNIFORMES.");
        } else {
            reporte.append("CONCLUSIÓN: SE RECHAZA H0.\nLos números NO SON UNIFORMES.");
        }

        // 6. Mostrar resultado
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Resultados Chi-Cuadrada", JOptionPane.INFORMATION_MESSAGE);
    }

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

    public static void realizarPruebaIndependenciaPoker(ArrayList<Double> datos) {
        int n = datos.size();

        // Probabilidades teóricas para 5 decimales (Suman 1.0)
        double[] probEsperada = {0.3024, 0.5040, 0.1080, 0.0720, 0.0090, 0.0045, 0.0001};
        String[] nombresManos = {"Todos Diferentes", "Un Par", "Dos Pares", "Tercia", "Full House", "Póker", "Quintilla"};
        int[] conteoObservado = new int[7]; // Indices 0 al 6 corresponden a las manos de arriba

        // 1. Clasificar cada número
        for (double num : datos) {
            // Convertimos a String y tomamos 5 decimales.
            // Ej: 0.123456 -> "12345"
            String numStr = String.format("%.5f", num).replace(",", ".").substring(2);
            if (numStr.length() > 5) numStr = numStr.substring(0, 5); // Asegurar 5 dígitos

            int tipoMano = evaluarMano(numStr);
            conteoObservado[tipoMano]++;
        }

        // 2. Calcular Chi-Cuadrada
        StringBuilder reporte = new StringBuilder();
        reporte.append("--- PRUEBA DE PÓKER (INDEPENDENCIA) ---\n");
        reporte.append(String.format("Datos analizados (n): %d\n\n", n));
        reporte.append(String.format("%-18s %-10s %-10s %-10s\n", "Mano", "Obs(O)", "Esp(E)", "(O-E)²/E"));
        reporte.append("-------------------------------------------------------\n");

        double chiCuadradaCalculada = 0.0;

        for (int i = 0; i < 7; i++) {
            double esperado = probEsperada[i] * n;
            double termino = 0.0;
            // Evitar división por cero si n es muy pequeño
            if (esperado > 0) {
                termino = Math.pow(conteoObservado[i] - esperado, 2) / esperado;
            }

            chiCuadradaCalculada += termino;

            reporte.append(String.format("%-18s %-10d %-10.2f %-10.4f\n",
                    nombresManos[i], conteoObservado[i], esperado, termino));
        }

        reporte.append("-------------------------------------------------------\n");
        reporte.append(String.format("Chi-Cuadrada Calculada: %.4f\n", chiCuadradaCalculada));

        // 3. Obtener valor crítico
        // Grados de libertad = Categorías - 1 = 7 - 1 = 6
        int gradosLibertad = 6;
        double valorCritico = obtenerValorCritico(gradosLibertad);

        reporte.append(String.format("Valor Crítico (gl=%d):   %.4f\n\n", gradosLibertad, valorCritico));

        // 4. Conclusión
        if (chiCuadradaCalculada < valorCritico) {
            reporte.append("CONCLUSIÓN: NO SE RECHAZA H0.\nLos números tienen un comportamiento de independencia ACEPTABLE.");
        } else {
            reporte.append("CONCLUSIÓN: SE RECHAZA H0.\nLos dígitos NO parecen independientes (Patrón detectado).");
        }

        // Advertencia para muestras pequeñas
        if (n < 50) {
            reporte.append("\n\n(NOTA: Para mayor fiabilidad, use n >= 50)");
        }

        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Resultados Prueba de Póker", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Analiza 5 dígitos y determina qué mano de póker forman.
     * Retorna índice: 0=TD, 1=1P, 2=2P, 3=T, 4=FH, 5=P, 6=Q
     */
    private static int evaluarMano(String s) {
        int[] counts = new int[10]; // Conteo para dígitos 0-9
        for (char c : s.toCharArray()) {
            counts[c - '0']++;
        }

        boolean par = false;
        boolean tercia = false;
        boolean poker = false;
        boolean quintilla = false;
        int paresCount = 0;

        for (int c : counts) {
            if (c == 5) quintilla = true;
            if (c == 4) poker = true;
            if (c == 3) tercia = true;
            if (c == 2) {
                par = true;
                paresCount++;
            }
        }

        if (quintilla) return 6; // Quintilla
        if (poker) return 5;     // Póker
        if (tercia && par) return 4; // Full House
        if (tercia) return 3;    // Tercia
        if (paresCount == 2) return 2; // Dos Pares
        if (par) return 1;       // Un Par
        return 0;                // Todos Diferentes
    }
}