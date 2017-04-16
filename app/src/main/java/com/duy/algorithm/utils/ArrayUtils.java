package com.duy.algorithm.utils;


import java.util.Random;

public class ArrayUtils {

    /**
     * create array with random value
     *
     * @param a    - array
     * @param bits - range random
     */
    public static void createIntArray(int[] a, int bits) {
        int len = a.length;
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            a[i] = random.nextInt(bits) + 1;
        }

    }

    /**
     * create array with random value
     *
     * @param length - length of array and range random
     */
    public static int[] createIntArray(int length) {
        int[] a = new int[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length) + 1;
        }
        return a;
    }

    public static void swap(int[] a, int i, int j) {
        int e = a[i];
        a[i] = a[j];
        a[j] = e;
    }

    public static String arrayToString(int[] array) {
        String res = "";
        for (int i = 0; i < array.length; i++) {
            res += array[i];
            if (i != array.length - 1)
                res += ",";
        }
        return res;
    }

    public static int[] arrayStringToInt(String[] strArray) {
        int array[] = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            try {
                array[i] = Integer.parseInt(strArray[i].replace(" ", ""));
            } catch (Exception e) {
                e.printStackTrace();
                array[0] = Integer.parseInt(strArray[i]);
                return array;
            }
        }
        return array;
    }

    public static int findMax(int[] array) {
        int max = -1;
        try {
            max = array[0];
            for (int a : array) {
                if (max < a) max = a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }
}
