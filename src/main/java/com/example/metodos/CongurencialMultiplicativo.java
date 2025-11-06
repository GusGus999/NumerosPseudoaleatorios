package com.example.metodos;

public class CongurencialMultiplicativo {
    public static void main(String[] args) {

        // X0  semilla
        int x0 = 5; //Numero entero impar primo relativo con m

        int d = 5;
        int m = (int) Math.pow(2, d); //d>3
        int t = 1;
        int a = 8 * t - 3;

        int n = 10;

        // Verificar que a y m sean primos relativos
        if (!sonPrimosRelativos(a, m)) {
            System.out.println("Advertencia: a y m no son primos relativos");
        }

        System.out.println("i\tXi\tri");
        System.out.println("------------------------");

        MCongurencialMultiplicativo(a, n, x0, m);


    }

    public static void MCongurencialMultiplicativo (int a, int n, int x0, int m) {
        int xi = x0;

        for (int i = 1; i <= n; i++) {
            // Xi = (a * Xi-1) mod m
            xi = (a * xi) % m;
            double ri = (double) xi / m;
            System.out.printf("%d\t%d\t%.6f\n", i, xi, ri);
        }

    }

    // Método para verificar si dos números son primos relativos
    public static boolean sonPrimosRelativos(int a, int b) {
        return mcd(a, b) == 1;
    }

    // Método para calcular el máximo común divisor
    public static int mcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
