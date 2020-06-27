package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import tictactoeGame.Mark;
import tictactoeGame.MinMaxAlgorithm;
import tictactoeGame.State;
import tictactoeGame.TicTacToe;

import java.io.IOException;

public class SmartPlayAgent extends Agent {

    Mark mark = Mark.X;
    Mark opponent = Mark.O;

    protected void setup() {
        System.out.println("starting "+getLocalName()+" for tic tac toe");
        FSMBehaviour smartAgent = new FSMBehaviour();
        smartAgent.registerFirstState(new waitForReferee(), "waitForPlay");
        smartAgent.registerState(new readyToPlay(), "readyToPlay");
        smartAgent.registerState(new play(mark,opponent), "play");
        smartAgent.registerState(new end(), "gameEnded");

        smartAgent.registerDefaultTransition("play","waitForPlay");
        smartAgent.registerTransition("waitForPlay","gameEnded",1);
        smartAgent.registerTransition("waitForPlay","readyToPlay",0);
        smartAgent.registerDefaultTransition("readyToPlay","play");

        addBehaviour(smartAgent);
    }

}

class waitForReferee extends OneShotBehaviour
{

    int transition = 0;

    @Override
    public void action() {
        ACLMessage receivedMessage = myAgent.blockingReceive();
        try {
            if ( (State)receivedMessage.getContentObject() == State.FINISHED )
            {
                transition = 1;
            }
            else if ( (State)receivedMessage.getContentObject() == State.IN_GAME )
            {
                transition = 0;
                System.out.println("it's my turn " + myAgent.getName() +" , receiving message from " + receivedMessage.getSender().getName());
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onEnd() {
        return transition;
    }
}

class readyToPlay extends OneShotBehaviour
{

    @Override
    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.addReceiver(new AID("referee",AID.ISLOCALNAME));
        message.setContent("READY");
        myAgent.send(message);
    }
}

class play extends OneShotBehaviour
{

    private final Mark mark;
    private final Mark opponent;


    public play(Mark mark,Mark opponent)
    {
        this.opponent = opponent;
        this.mark = mark;
    }

    @Override
    public void action() {
        ACLMessage receivedMessage = myAgent.blockingReceive();
        try
        {
            TicTacToe t = (TicTacToe)receivedMessage.getContentObject();
            MinMaxAlgorithm m = new MinMaxAlgorithm(mark,opponent,t.board);
            int[] minimaxState = m.minimax(3, mark);
            System.out.println(t.nextPlayer + " plays : " + minimaxState[1] + " , " + minimaxState[2]);
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setContentObject(minimaxState);
            message.addReceiver(receivedMessage.getSender());
            myAgent.send(message);
        }
        catch (UnreadableException | IOException e)
        {
            System.out.println("you must send a tic tac toe object");
        }

    }
}

class end extends OneShotBehaviour
{

    @Override
    public void action() {
        myAgent.doDelete();
    }
}
