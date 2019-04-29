import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

//        String s = "NOT (a IMPL c) OR (a IMPL b)";

        ModelFinder mf = new ModelFinder(s);
        mf.countrExist();
    }
}
