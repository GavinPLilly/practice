public class fizzbuzz {
    public static void run(int fizz, int buzz, int length) {
        for(int i = 1; i <= length; i++) {
            if(i % fizz == 0) {
                if(i % buzz == 0) {
                    System.out.println("fizzbuzz");
                }
                else {
                    System.out.println("fizz");
                }
            }
            else if(i % buzz == 0) {
                System.out.println("buzz");
            }
            else {
                System.out.println(i);
            }
        }
    }
    public static void main(String[] args) {
        fizzbuzz.run(3, 5, 100);
    }
}
