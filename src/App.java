import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;

public class App {

    static final int ITERATIONS = 5;
    static final int ARRAY_REFERENCE_SIZE = 10;
    static final int ARRAY_SIZE_VARIANCE = 1;
    static final int MIN_VALUE = -9;
    static final int MAX_VALUE = 9;

    public static void main(String[] args) throws Exception {

        demoTask1();
        demoTask2();

    }

    static void demoTask1() {
        printEmphasized("\nЗадача 1 \u2014 Поэлементное вычитание массивов");

        for (int i = 1; i <= ITERATIONS; ++i) {
            System.out.printf("\nИтерация %d\n", i);

            var arr1 = generateRandomArray(ARRAY_REFERENCE_SIZE, ARRAY_SIZE_VARIANCE, MIN_VALUE, MAX_VALUE);
            var arr2 = generateRandomArray(ARRAY_REFERENCE_SIZE, ARRAY_SIZE_VARIANCE, MIN_VALUE, MAX_VALUE);

            System.out.print("Первый массив:     ");
            System.out.println(Arrays.toString(arr1));
            System.out.print("Второй массив:     ");
            System.out.println(Arrays.toString(arr2));

            System.out.print("Результат функции: ");
            try {
                var resultArr = getDifferences(arr1, arr2);
                printEmphasized(Arrays.toString(resultArr));
            } catch (RuntimeException e) {
                printError("Произошла ошибка во время выполнения функции. " + e.getMessage());
            }
        }
    }

    static void demoTask2() {
        printEmphasized("\nЗадача 2 \u2014 Поэлементное деление массивов");

        for (int i = 1; i <= ITERATIONS; ++i) {
            System.out.printf("\nИтерация %d\n", i);

            var arr1 = generateRandomArray(ARRAY_REFERENCE_SIZE, ARRAY_SIZE_VARIANCE, MIN_VALUE, MAX_VALUE);
            var arr2 = generateRandomArray(ARRAY_REFERENCE_SIZE, ARRAY_SIZE_VARIANCE, MIN_VALUE, MAX_VALUE);

            System.out.print("Первый массив:     ");
            System.out.println(Arrays.toString(arr1));
            System.out.print("Второй массив:     ");
            System.out.println(Arrays.toString(arr2));

            System.out.print("Результат функции: ");
            try {
                var resultArr = getRatios(arr1, arr2);
                printEmphasized(Arrays.toString(resultArr));
            } catch (RuntimeException e) {
                printError("Произошла ошибка во время выполнения функции. " + e.getMessage());
            }
        }
    }

    /**
     * Поэлементная разность двух целочисленных массивов.
     * Аргументы должны быть массивами одинаковой длины.
     * 
     * @param array1 Первый массив.
     * @param array2 Второй массив.
     * @return Новый массив, каждый элемент которого равен разности элементов
     *         с тем же индексом первого и второго массивов.
     * @throws RuntimeException в случаях, если массивы имеют разную длину или
     *                          хотя бы один из них null.
     */
    static int[] getDifferences(int[] array1, int[] array2) {
        arraysRequireSameSize(array1, array2);

        int[] result = new int[array1.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = array1[i] - array2[i];
        }
        return result;
    }

    /**
     * Поэлементное частное двух целочисленных массивов.
     * Аргументы должны быть массивами одинаковой длины.
     * В массиве знаменателей не должно содержаться нулевых значений.
     * 
     * @param array1 Первый массив -- числители.
     * @param array2 Второй массив -- знаменатели.
     * @return Новый массив вещественных чисел, каждый элемент которого равен
     *         частному элементов с тем же индексом первого и второго массивов.
     * @throws RuntimeException в случаях, если массивы имеют разную длину,
     *                          если хотя бы один из них null, а также если
     *                          во втором массиве знаменателей содержится хотя бы
     *                          одно нулевое значение.
     */
    static double[] getRatios(int[] array1, int[] array2) {
        arraysRequireSameSize(array1, array2);
        arrayRequireNonZero(array2,
                i -> String.format("Недопустимые данные во втором массиве"
                        + " знаменателей: элемент с индексом %d имеет значение ноль,"
                        + " недопустимое для знаменателя.", i));

        double[] result = new double[array1.length];
        for (int i = 0; i < result.length; ++i) {
            result[i] = array1[i] / (double) array2[i];
        }
        return result;
    }

    private static void arrayRequireNonZero(int[] array, IntFunction<String> messageSupplier) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == 0)
                throw new RuntimeException(messageSupplier == null ? null : messageSupplier.apply(i));
        }
    }

    private static void arraysRequireSameSize(int[] array1, int[] array2) {
        if (array1 == null) {
            throw new RuntimeException("Недопустимый первый аргумент:"
                    + " ссылка на массив не должна быть null.");
        }
        if (array2 == null) {
            throw new RuntimeException("Недопустимый второй аргумент:"
                    + " ссылка на массив не должна быть null.");
        }
        if (array1.length != array2.length) {
            throw new RuntimeException(String.format("Некорректные аргументы:"
                    + " массивы не должны иметь разную длину."
                    + " Длина первого массива %d. Длина второго массива %d.",
                    array1.length, array2.length));
        }
    }

    private static int[] generateRandomArray(int size, int sizeVariance, int min, int max) {
        if (min > max) {
            int tmp = max;
            max = min;
            min = tmp;
        }
        sizeVariance = Math.abs(sizeVariance);
        sizeVariance = ThreadLocalRandom.current().nextInt(-sizeVariance, sizeVariance);
        size = Math.max(1, size + sizeVariance);

        return ThreadLocalRandom.current().ints(min, max + 1).limit(size).toArray();
    }

    private static void printEmphasized(String text) {
        System.out.println("\u001b[1;97m" + text + "\u001b[0m");
    }

    private static void printError(String message) {
        System.err.println("\u001b[1;91m" + message + "\u001b[0m");
    }

}
