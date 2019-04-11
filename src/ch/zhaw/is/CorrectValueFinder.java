package ch.zhaw.is;

/**
 * Exercise 1
 */
final class CorrectValueFinder {

    private static final int[] W_VAL = new int[]{128, 256, 384, 512};

    public static void main(String[] args) {

        for (int w : W_VAL) {

            double space = Math.pow(2, w);

            int b = 1;
            double wb = 0;
            while (space >= wb) {
                wb = calcW(b++);
            }
            System.out.println("w=" + w + " ,b=" + b + " ,W(b)=" + wb);
        }

    }

    private static double calcW(int b) {
        return Math.exp(1.92 * Math.pow(b, 1f / 3f) * Math.pow(Math.log(b), 2f / 3f));
    }
}