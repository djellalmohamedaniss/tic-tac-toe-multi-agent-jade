package tictactoeGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinMaxAlgorithm {

    Mark player;
    Mark opponent;
    TicTacToe game;

    public MinMaxAlgorithm(Mark player,Mark opponent,Mark [][]board)
    {
        this.player = player;
        this.opponent = opponent;
        this.game = new TicTacToe(board);
    }

    public int[] minimax(int depth, Mark mySeed) {
        // Generate possible next moves in a List of int[2] of {row, col}.
        List<int[]> nextMoves = generateMoves();
        if ( nextMoves.size() == 9 ) return new int[] {1, new Random().nextInt(3), new Random().nextInt(3)};

        // mySeed is maximizing; while oppSeed is minimizing
        int bestScore = (this.player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if ( this.game.isGameOver() || depth == 0) {
            // Game over or depth reached, evaluate score
            if ( this.game.winner() == this.player )
                bestScore = this.game.emptyCases() + 1;
            else if ( this.game.winner() == this.opponent )
            {
                bestScore = -(this.game.emptyCases() + 1);
            }
            else
            {
                bestScore = 0;
            }
        }
        else {

            for (int[] move : nextMoves) {
                // Try this move for the current "player"
                game.board[move[0]][move[1]] = mySeed;
                if (player == mySeed) {  // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, this.player)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // oppSeed is minimizing player
                    currentScore = minimax(depth - 1, this.opponent)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // Undo move
                game.board[move[0]][move[1]] = Mark.Empty;
            }
        }
        return new int[] {bestScore, bestRow, bestCol};
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>();

        // If game over, i.e., no next move
        if (this.game.markWon(player) || this.game.markWon(opponent) ) {
            return nextMoves;   // return empty list
        }

        // Search for empty cells and add to the List
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (this.game.board[row][col] == Mark.Empty) {
                    nextMoves.add(new int[] {row, col});
                }
            }
        }
        return nextMoves;
    }
}
