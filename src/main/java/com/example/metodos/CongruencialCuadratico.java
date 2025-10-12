package com.example.metodos;

public class CongruencialCuadratico {
    public static void main(String[] args) {
        int x0 = 13; //Numero entero cualquiera
        int a = 26; //Debe ser un numero par
        int b = 26; //Debe verificar que (b-1) mod 4 = 1
        int c = 27; //Debe ser un numero impar
        int d = 10; //Entero positivo para determinar m
        int m = (int) Math.pow(2, d);

        congruencialCuadratico(x0, a, b, c, d -1, m);

    }

    public static void congruencialCuadratico(int x0, int a, int b, int c, int n, int m) {
        System.out.println("n\tXn\t\tXn+1\t\tRi");
        System.out.println("---------------------------------------------------------------------------------");

        int xi = x0; // Asignamos la semilla a nuestra variable iterativa

        for (int i = 0; i < n; i++) {
            long operacion = (long) a * xi * xi + (long) b * xi + c;
            int siguiente = (int) (operacion % m);
            double ri = (double) siguiente / m;

            System.out.printf("%d\t%d\t\t%d\t\t%.4f\n", i, xi, siguiente, ri);

            xi = siguiente; // Actualizar xi para la siguiente iteración
        }
    }

}
