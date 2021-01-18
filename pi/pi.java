import java.util.Random;

public class pi{
    public static double pi_calc() {
        Random r = new Random();

        int length = 10000000;
        double[][] arr = new double[length][2]; //One hundred coordinate pairs
        for(int i = 0; i < length; i++) {
            arr[i][0] = r.nextDouble() - 0.5;
            arr[i][1] = r.nextDouble() - 0.5;
        }

        int circle_counter = 0;
        for(int i = 0; i < length; i++) {
            if((arr[i][0] * arr[i][0]) + (arr[i][1] * arr[i][1]) < 0.25) {
                circle_counter += 1;
            }
        }
        double out = (double) circle_counter / length;
        return out * 4;
    }
    public static void main(String[] args) {
        double tmptotal = 0;
        int iterations = 50;
        for(int i = 0; i < iterations; i++) {
            tmptotal += pi.pi_calc();
            System.out.println("Done with " + i);
        }
        double out = tmptotal / (double) iterations;
        System.out.println(out);
    }
}