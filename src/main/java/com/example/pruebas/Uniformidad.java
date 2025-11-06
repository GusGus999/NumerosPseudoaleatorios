package com.example.pruebas;

import java.util.List;

public class Uniformidad {
    List<Double> numerosGenerados;

    public static void main(String[] args) {
        Uniformidad u = new Uniformidad();
        u.congruencialCuadratico(13,26,26,27,10-1,1024);
        u.uniformidad();
    }

    private void uniformidad(){
        List<Double> numeros = this.numerosGenerados;

        int longitud = numeros.toArray().length;
        int k = (int) Math.sqrt(longitud);
        System.out.println(k);
    }

    public void congruencialCuadratico(int x0, int a, int b, int c, int n, int m) {

        int xi = x0; // Asignamos la semilla a nuestra variable iterativa

        for (int i = 0; i < n; i++) {
            long operacion = (long) a * xi * xi + (long) b * xi + c;
            int siguiente = (int) (operacion % m);
            double ri = (double) siguiente / m;

            numerosGenerados.add(ri);
            xi = siguiente; // Actualizar xi para la siguiente iteración
        }
    }


}
