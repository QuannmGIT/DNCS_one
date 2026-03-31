package StoreManagement.debug;

import java.util.Scanner;

public class test {
    public static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        // Check for edge cases: no dot, or dot is the first character
        if (lastIndexOfDot == -1 || lastIndexOfDot == 0 || lastIndexOfDot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(getFileExtension(input));
        System.out.println(input);
        if (input.endsWith(".png"))
            System.out.println("oke");
        else
            System.out.println("no");
        scanner.close();
    }
}