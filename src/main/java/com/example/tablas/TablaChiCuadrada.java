package com.example.tablas;

public class TablaChiCuadrada {

    // Tabla de valores críticos para Alpha = 0.05 (95% de confianza)
    // El índice del arreglo corresponde a los Grados de Libertad (gl).
    private static final double[] VALORES_CRITICOS_05 = {
            0.0,    // 0 (No existe)
            3.841,  // 1
            5.991,  // 2
            7.815,  // 3
            9.488,  // 4
            11.070, // 5
            12.592, // 6
            14.067, // 7
            15.507, // 8
            16.919, // 9
            18.307, // 10
            19.675, // 11
            21.026, // 12
            22.362, // 13
            23.685, // 14
            24.996, // 15
            26.296, // 16
            27.587, // 17
            28.869, // 18
            30.144, // 19
            31.410, // 20
            32.671, // 21
            33.924, // 22
            35.172, // 23
            36.415, // 24
            37.652, // 25
            38.885, // 26
            40.113, // 27
            41.337, // 28
            42.557, // 29
            43.773  // 30
    };

    public static double obtenerValorCritico(int gradosLibertad) {
        // Validación básica
        if (gradosLibertad < 1) return 0.0;

        // Si está en nuestra tabla exacta (1 a 30), lo devolvemos directo
        if (gradosLibertad < VALORES_CRITICOS_05.length) {
            return VALORES_CRITICOS_05[gradosLibertad];
        } else {
            // Si es mayor a 30, usamos la aproximación de Wilson-Hilferty
            return calcularAproximacionWilsonHilferty(gradosLibertad);
        }
    }

    private static double calcularAproximacionWilsonHilferty(int gl) {
        double z = 1.645; // Valor Z para 95% de confianza
        double parteA = 2.0 / (9.0 * gl);
        double termino = 1 - parteA + (z * Math.sqrt(parteA));
        return gl * Math.pow(termino, 3);
    }
}
