package com.example.metodos;
import java.util.Scanner;

public class VariablesAleatorias {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=== Generación de variables aleatorias ===");
            System.out.println("1. Transformada Inversa (Distribución Exponencial)");
            System.out.println("2. Convolución (Distribución Erlang k)");
            System.out.println("3. Composición (Distribución Triangular)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    generarExponencial();
                    break;
                case 2:
                    generarErlang();
                    break;
                case 3:
                    generarTriangular();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // ===========================================================
    // 1. Transformada Inversa: Distribución Exponencial
    // ===========================================================
    public static void generarExponencial() {

        System.out.print("\nIngrese lambda (λ): ");
        double lambda = sc.nextDouble();

        System.out.print("¿Cuántos valores desea generar?: ");
        int n = sc.nextInt();

        System.out.println("\nValores Exponenciales generados:");
        for (int i = 0; i < n; i++) {

            double U = Math.random();
            double X = -(Math.log(1 - U)) / lambda;

            System.out.printf("X[%d] = %.6f%n", i + 1, X);
        }
    }

    // ===========================================================
    // 2. Convolución: Distribución Erlang k (suma de k exponenciales)
    // ===========================================================
    public static void generarErlang() {
        System.out.print("\nIngrese k (entero): ");
        int k = sc.nextInt();

        System.out.print("Ingrese lambda (λ): ");
        double lambda = sc.nextDouble();

        System.out.print("¿Cuántos valores desea generar?: ");
        int n = sc.nextInt();

        System.out.println("\nValores Erlang generados:");
        for (int i = 0; i < n; i++) {

            double suma = 0.0;

            for (int j = 0; j < k; j++) {
                double U = Math.random();
                double exp = -(Math.log(1 - U)) / lambda;
                suma += exp;
            }

            System.out.printf("X[%d] = %.6f%n", i + 1, suma);
        }
    }

    // ===========================================================
    // 3. Composición: Distribución Triangular
    // ===========================================================
    public static void generarTriangular() {
        System.out.print("\nIngrese a (mínimo): ");
        double a = sc.nextDouble();

        System.out.print("Ingrese b (modo): ");
        double b = sc.nextDouble();

        System.out.print("Ingrese c (máximo): ");
        double c = sc.nextDouble();

        System.out.print("¿Cuántos valores desea generar?: ");
        int n = sc.nextInt();

        double Fb = (b - a) / (c - a); // Punto de corte de la CDF

        System.out.println("\nValores Triangulares generados:");
        for (int i = 0; i < n; i++) {

            double U = Math.random();
            double X;

            if (U < Fb) {
                X = a + Math.sqrt(U * (b - a) * (c - a));
            } else {
                X = c - Math.sqrt((1 - U) * (c - b) * (c - a));
            }

            System.out.printf("X[%d] = %.6f%n", i + 1, X);
        }
    }
}
