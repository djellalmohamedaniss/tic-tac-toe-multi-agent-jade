import tictactoeGame.Mark;
import tictactoeGame.MinMaxAlgorithm;
import tictactoeGame.TicTacToe;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        TicTacToe t = new TicTacToe(Mark.X);
        int turn = new Random().nextInt(2);
        int [] minimaxState = new int[3];
        while(!t.isGameOver())
        {
            // X plays
            if ( turn == 0 )
            {
                /*MinMaxAlgorithm m = new MinMaxAlgorithm(Mark.X,Mark.O,t.board);
                minimaxState = m.minimax(3,Mark.X);
                System.out.println("X plays : " + minimaxState[1] + " , " + minimaxState[2]);*/
                int line = in.nextInt();
                int column = in.nextInt();
                //System.out.println("X plays : " + line + " , " + column);
                t.addMark(Mark.X,line,column);
                turn = 1;
            }
            else
            {
                MinMaxAlgorithm m = new MinMaxAlgorithm(Mark.O,Mark.X,t.board);
                minimaxState = m.minimax(4,Mark.O);
                //System.out.println("O plays : " + minimaxState[1] + " , " + minimaxState[2]);
                t.addMark(Mark.O,minimaxState[1],minimaxState[2]);
                turn = 0;
            }
            t.showBoard();


        }

        System.out.println(t.winner());
    }
}
