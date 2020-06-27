package agents;

import jade.core.AID;
import jade.core.Agent;

import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import tictactoeGame.Mark;
import tictactoeGame.State;
import tictactoeGame.TicTacToe;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public class RefereeAgent extends Agent {

    public TicTacToe game;

    protected void setup() {
        System.out.println("starting "+getLocalName()+" for tic tac toe");
        Mark firstPlayer = new Random().nextInt(2) == 0 ? Mark.X : Mark.O;
        this.game = new TicTacToe(firstPlayer);
        System.out.println("player " + firstPlayer + " starts the game!");
        FSMBehaviour refereeAgent = new FSMBehaviour();
        refereeAgent.registerFirstState(new CheckGameState(this.game), "CheckGameState");
        refereeAgent.registerState(new WaitForPlayerToBeReady(), "WaitForPlayerToBeReady");
        refereeAgent.registerState(new AskPlayerToPlay(this.game), "AskPlayerToPlay");
        refereeAgent.registerState(new UpdateBoard(this.game), "UpdateBoard");
        refereeAgent.registerState(new EndGame(this.game), "EndGame");

        refereeAgent.registerTransition("CheckGameState","EndGame",0);
        refereeAgent.registerTransition("CheckGameState","WaitForPlayerToBeReady",1);
        refereeAgent.registerDefaultTransition("WaitForPlayerToBeReady","AskPlayerToPlay");
        refereeAgent.registerDefaultTransition("AskPlayerToPlay","UpdateBoard");
        refereeAgent.registerDefaultTransition("UpdateBoard","CheckGameState");


        addBehaviour(refereeAgent);
    }
}

class CheckGameState extends OneShotBehaviour
{

    private final TicTacToe game;
    int transition = 1;

    public CheckGameState(TicTacToe game)
    {
        this.game = game;
    }

    private String getPlayerName()
    {
        return "player" + this.game.nextPlayer;
    }

    @Override
    public void action() {
        if ( this.game.isGameOver() )
        {
            System.out.println("the game is over");
            try {
                ACLMessage message = new ACLMessage(ACLMessage.INFORM);
                message.addReceiver(new AID("player"+Mark.X,AID.ISLOCALNAME));
                message.addReceiver(new AID("player"+Mark.O,AID.ISLOCALNAME));
                message.setContentObject(State.FINISHED);
                myAgent.send(message);
                transition = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            // call the agent who will play
            String currentPlayer = this.getPlayerName();
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(new AID(currentPlayer,AID.ISLOCALNAME));
            try {
                message.setContentObject(State.IN_GAME);
                myAgent.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int onEnd() {
        return transition;
    }
}

class WaitForPlayerToBeReady extends OneShotBehaviour
{
    @Override
    public void action() {
        ACLMessage receivedMessage = myAgent.blockingReceive();
        System.out.println("receiving message from " +  receivedMessage.getSender().getName() + " that he's ready!");
    }
}

class AskPlayerToPlay extends OneShotBehaviour
{
    public TicTacToe game;

    AskPlayerToPlay(TicTacToe game)
    {
        this.game = game;
    }

    private String getPlayerName()
    {
        return "player" + this.game.nextPlayer;
    }

    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID(this.getPlayerName(),AID.ISLOCALNAME));
        try {
            message.setContentObject(this.game);
            System.out.println("sending game state to " + this.getPlayerName());
            myAgent.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class UpdateBoard extends OneShotBehaviour
{
    private final TicTacToe game;

     UpdateBoard (TicTacToe game)
     {
         this.game = game;
     }

    @Override
    public void action() {
        ACLMessage receivedMessage = myAgent.blockingReceive();
        System.out.println("receiving message from " +  receivedMessage.getSender().getName() + " that he played!");
        try {
            int[] move = (int[]) receivedMessage.getContentObject();
            System.out.println("playing line " + move[1] + ", column " + move[2]);
            this.game.addMark(this.game.nextPlayer,move[1],move[2]);
            this.game.nextPlayer = this.game.nextPlayer == Mark.X ? Mark.O : Mark.X;
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

    }
}

class EndGame extends OneShotBehaviour
{

    private final TicTacToe game;

    public EndGame(TicTacToe game)
    {
        this.game = game;
    }

    @Override
    public void action() {
        System.out.println("the winner is " + game.winner());
        this.game.showBoard();
        myAgent.doDelete();
    }
}
