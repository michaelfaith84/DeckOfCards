/*

 */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String textInput;

        CasinoBlackJack game = new CasinoBlackJack();
        System.out.println("Welcome to Casino Black Jack!");

        while (true) {
            System.out.print("Would you like to read the rules? y/n ");
            textInput = input.next().toLowerCase();
            if (textInput.equals("y")) {
                CasinoBlackJack.printRules();
                break;
            } else if (textInput.equals("n")) {
                break;
            }
        }

        System.out.println();

        game.playGame();

        while (true) {
            System.out.println();
            System.out.print("Would you like to play another round? y/n ");
            textInput = input.next().toLowerCase();

            if (textInput.equals("y")) {
                System.out.println();
                game.playGame();

            } else if (textInput.equals("n")) {
                break;
            }
        }
    }
}