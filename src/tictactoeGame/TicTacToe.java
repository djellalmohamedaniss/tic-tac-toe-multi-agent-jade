package tictactoeGame;

import java.io.Serializable;

public class TicTacToe implements Serializable {

    public Mark [][]board;
    public Mark nextPlayer;

    public TicTacToe(Mark nextPlayer)
    {
        board = new Mark[3][3];
        this.nextPlayer = nextPlayer;
        this.fillWithEmpty();
    }

    public TicTacToe(Mark [][] board)
    {
        this.board = copyBoard(board);
    }

    private void fillWithEmpty()
    {
        for (int row = 0; row < this.board.length; row++) {
            for (int col = 0; col < this.board[row].length; col++) {
                board[row][col] = Mark.Empty;
            }
        }
    }

    public void addMark(Mark mark, int line, int column)
    {
        if ( line > 2 || line < 0 || column > 2 || column < 0 )
            return;
        if ( this.board[line][column] == Mark.Empty )
        {
            board[line][column] = mark;
        }
    }

    public Mark winner()
    {
        return markWon(Mark.X) ? Mark.X : markWon(Mark.O) ? Mark.O : Mark.Empty;
    }

    public boolean isGameOver()
    {
        return gameIsDraw() || markWon(Mark.X) || markWon(Mark.O);
    }

    public boolean markWon(Mark mark)
    {
        return lineCross(mark,0) || lineCross(mark,1) || lineCross(mark,2)
                || columnCross(mark,0) || columnCross(mark,1) || columnCross(mark,2)
                || diagonals(mark);
    }

    public boolean gameIsDraw()
    {
        for(Mark[] line : this.board)
        {
            for (Mark column : line )
            {
                if ( column == Mark.Empty)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public int emptyCases()
    {
        int count = 0;
        for(Mark[] line : this.board)
        {
            for (Mark column : line )
            {
                if ( column == Mark.Empty)
                {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean lineCross(Mark mark,int line)
    {
        return this.board[line][0] ==mark && this.board[line][1] == mark &&  this.board[line][2] == mark;
    }

    private boolean columnCross(Mark mark,int column)
    {
        return this.board[0][column] ==mark && this.board[1][column] == mark &&  this.board[2][column] == mark;
    }

    private boolean diagonals(Mark mark)
    {
        return (this.board[0][0] == mark && this.board[1][1] == mark &&  this.board[2][2] == mark)
                || (this.board[0][2] == mark && this.board[1][1] == mark &&  this.board[2][0] == mark);
    }

    private Mark[][] copyBoard(Mark [][] board)
    {
        Mark [][] cb = new Mark[3][3];
        for (int row = 0; row < 3;row++)
        {
            System.arraycopy(board[row], 0, cb[row], 0, 3);
        }
        return cb;
    }

    public void showBoard()
    {
        for(Mark[] line : this.board)
        {
            for(Mark mark : line)
            {
                if ( mark == Mark.Empty )
                {
                    System.out.print("  | ");
                }
                else
                {
                    System.out.print(mark + " | ");
                }
            }
            System.out.println("\n");
        }
    }

}
