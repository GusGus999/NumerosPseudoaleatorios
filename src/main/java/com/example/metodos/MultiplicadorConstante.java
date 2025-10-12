package com.example.metodos;

public class MultiplicadorConstante {
    public static void main(String[] args) {
        int x0 = 9803;
        int a = 6965;
        int n = 10;

        multiplicadorConstante(a, x0, n);
    }

    public static void multiplicadorConstante(int a, int x0, int n) {
        if (String.valueOf(x0).length() != 4) {
            System.out.println("La semilla tiene que ser de 4 dígitos");
            return;
        }

        System.out.println("n\tyn\t\t\txn\t\trn");
        System.out.println("---------------------------------");

        int xi = x0; // Variable iterativa

        for (int i = 0; i < n; i++) {
            long producto = (long) a * xi;
            String productoStr = String.valueOf(producto);

            // Asegurarse de que la cadena del producto tenga al menos 8 dígitos para extraer los 4 centrales
            while (productoStr.length() < 8) {
                productoStr = "0" + productoStr;
            }

            int longitud = productoStr.length();
            int inicio = (longitud - 4) / 2;
            String dijitosCentrales = productoStr.substring(inicio, inicio + 4);

            int siguiente = Integer.parseInt(dijitosCentrales);
            double ri = (double) siguiente / 10000;

            System.out.printf(i + "   " + productoStr + "    " + dijitosCentrales + "    " + ri + "\n");

            xi = siguiente;
        }
    }
}
