package com.example.VariablesAleatorias;
import java.util.*;

public class CongruencialLineal {

    // Parámetros del generador (los mismos que tenías)
    static final int a = 21;
    static final int c = 13;
    static final int m = 100;
    static int xPrev = 10;  // semilla

    public static void main(String[] args) {

        if (!sonPrimosRelativos(c, m)) {
            System.out.println("Error: c y m no son primos relativos.");
            return;
        }

        if (!aMenosUnoMultiploFactoresPrimosDeM(a, m)) {
            System.out.println("Error: a-1 no es múltiplo de todos los factores primos de m.");
            return;
        }

        if (m % 4 == 0 && (a - 1) % 4 != 0) {
            System.out.println("Error: si m es múltiplo de 4, 4 debe dividir a a-1.");
            return;
        }

        System.out.println("i\txi\tri");
        System.out.println("-".repeat(20));
        System.out.println("0\t" + xPrev + "\t" + ((double) xPrev / m));

        // Itera exactamente hasta m-1
        for (int i = 1; i <= m - 1; i++) {
            int xi = (a * xPrev + c) % m;
            double ri = (double) xi / m;
            System.out.println(i + "\t" + xi + "\t" + ri);
            xPrev = xi;
        }
    }

    // 👉 Método para usar el generador desde OTRAS clases (método de composición)
    public static double siguienteRi() {
        xPrev = (a * xPrev + c) % m;
        return (double) xPrev / m;
    }

    private static boolean sonPrimosRelativos(int x, int y) {
        return gcd(x, y) == 1;
    }

    private static int gcd(int x, int y) {
        while (y != 0) {
            int temp = y;
            y = x % y;
            x = temp;
        }
        return x;
    }

    private static boolean aMenosUnoMultiploFactoresPrimosDeM(int a, int m) {
        int[] primos = factoresPrimos(m);
        for (int p : primos) {
            if ((a - 1) % p != 0) {
                return false;
            }
        }
        return true;
    }

    private static int[] factoresPrimos(int num) {
        Set<Integer> factores = new HashSet<>();
        int n = num;
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factores.add(i);
                n /= i;
            }
        }
        if (n > 1) {
            factores.add(n);
        }
        int[] res = new int[factores.size()];
        int idx = 0;
        for (int f : factores) {
            res[idx++] = f;
        }
        return res;
    }
}
