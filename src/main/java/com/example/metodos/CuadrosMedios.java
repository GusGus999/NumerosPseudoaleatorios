package com.example.metodos;
public class CuadrosMedios {

    public static void main(String[] args) {
        int semilla = 5735; // Semilla inicial (debe tener más de 3 dígitos)
        int n = 10; // Cantidad de números aleatorios a generar

        cuadradosMedios(semilla, n);
    }

    public static void cuadradosMedios(int y0, int n) {

        System.out.printf("%-4s %-8s %-12s %-12s %-8s %-8s\n",
                "i", "Yi", "Yi²", "Yi²(Ceros a la izquierda)", "Xi", "Ri");
        System.out.println("---------------------------------------------------------------------------------");

        int yi = y0; // Asignamos la semilla a nuestra variable iterativa

        for (int i = 0; i < n; i++) {
            // Calcular Yi²
            long yiCuadrado = (long) Math.pow(yi, 2);

            // Formatear con 8 dígitos (agregar ceros a la izquierda si es necesario)
            String yiCuadradoStr = String.format("%08d", yiCuadrado);

            // Extraer los 4 dígitos centrales (posiciones 2, 3, 4, 5)
            int xi = Integer.parseInt(yiCuadradoStr.substring(2, 6));

            // Calcular ri (número pseudoaleatorio entre 0 y 1)
            double ri = xi / 10000.0;

            // Mostrar resultados
            System.out.printf("%-4d %-8d %-12d %-12s %-8d %.4f\n",
                    i, yi, yiCuadrado, yiCuadradoStr, xi, ri);

            yi = xi; // Actualizar yi para la siguiente iteración
        }
    }
}