package com.example.metodos;
public class CongruencialAditivo {
    public static void main(String[] args) {
        int m = 100;
        int n = 5; // cantidad de semillas iniciales
        int[] semillas = {65, 89, 98, 3, 69}; // x1=65, x2=89, x3=98, x4=3, x5=69
        int cantidadNumeros = 15; // total xi,contando desde x1

        int totalGenerados = cantidadNumeros - n;
        int[] xi = new int[cantidadNumeros];
        double[] riGenerados = new double[totalGenerados];

        // Cargar semillas
        for (int i = 0; i < n; i++) {
            xi[i] = semillas[i];
        }

        // Generar números pseudoaleatorios (x6 en adelante)
        for (int i = n, j = 0; i < cantidadNumeros; i++, j++) {int nuevoXi = (xi[i - n] + xi[i - 1]) % m;
            xi[i] = nuevoXi;
            riGenerados[j] = (double) nuevoXi / (m - 1); // ri para generados solo
        }


        System.out.println("Números pseudoaleatorios generados:");
        System.out.println("I\tr_i\t   Xn"); // i=1 para r1 (x6), i=2 para r2 (x7)
        System.out.println("-".repeat(20));
        for (int i = 0; i < totalGenerados; i++) {
            System.out.printf("%d\t%.4f\t%d\n", i + 1, riGenerados[i], xi[n + i]);
        }

    }
}