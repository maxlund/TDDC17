package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.Random;
import java.util.*;

class MyAgentState
{
    public int[][] world = new int[30][30];
    public int initialized = 0;
    final int UNKNOWN   = 0;
    final int WALL      = 1;
    final int CLEAR     = 2;
    final int DIRT      = 3;
    final int HOME      = 4;
    final int ACTION_NONE           = 0;
    final int ACTION_MOVE_FORWARD   = 1;
    final int ACTION_TURN_RIGHT     = 2;
    final int ACTION_TURN_LEFT      = 3;
    final int ACTION_SUCK   = 4;
    
    public int agent_x_position = 1;
    public int agent_y_position = 1;
    public int agent_last_action = ACTION_NONE;
    
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public int agent_direction = EAST;
    
    MyAgentState()
    {
    for (int i=0; i < world.length; i++)
        for (int j=0; j < world[i].length ; j++)
        world[i][j] = UNKNOWN;
    world[1][1] = HOME;
    agent_last_action = ACTION_NONE;
    }
    // Based on the last action and the received percept updates the x & y agent position
    public void updatePosition(DynamicPercept p)
    {
    Boolean bump = (Boolean)p.getAttribute("bump");

    if (agent_last_action==ACTION_MOVE_FORWARD && !bump)
    {
        switch (agent_direction) {
        case MyAgentState.NORTH:
        agent_y_position--;
        break;
        case MyAgentState.EAST:
        agent_x_position++;
        break;
        case MyAgentState.SOUTH:
        agent_y_position++;
        break;
        case MyAgentState.WEST:
        agent_x_position--;
        break;
        }
    }
        
    }
    
    public void updateWorld(int x_position, int y_position, int info)
    {
    world[x_position][y_position] = info;
    }
    
    public void printWorldDebug()
    {
    for (int i=0; i < world.length; i++)
    {
        for (int j=0; j < world[i].length ; j++)
        {
        if (world[j][i]==UNKNOWN)
            System.out.print(" ? ");
        if (world[j][i]==WALL)
            System.out.print(" # ");
        if (world[j][i]==CLEAR)
            System.out.print(" . ");
        if (world[j][i]==DIRT)
            System.out.print(" D ");
        if (world[j][i]==HOME)
            System.out.print(" H ");
        }
        System.out.println("");
    }
    }
}

class MyAgentProgram implements AgentProgram {

    private int initnialRandomActions = 10;
    private Random random_generator = new Random();
    
    // Here you can define your variables!
    public int iterationCounter = 100;
    public MyAgentState state = new MyAgentState();
    
    public Vector<Integer> mySequence = new Vector<Integer>();
    
    private void addChildren(Queue<Node> frontier, Vector<Coordinates> visited, Node current, int x_pos, int y_pos)
    {
        Vector<Integer> tempSequence = (Vector<Integer>)current.sequence.clone();
        tempSequence.addElement(state.ACTION_MOVE_FORWARD);
        Node childForward = new Node(x_pos, y_pos, current.direction, tempSequence);

        frontier.add(childForward);
        int direction = childForward.direction;
        
        direction++;
        direction = direction % 4;
        Vector<Integer> tempSequence2 = (Vector<Integer>)childForward.sequence.clone();
        tempSequence2.addElement(state.ACTION_TURN_RIGHT);
        frontier.add(new Node(childForward.x, childForward.y, direction, tempSequence2));

        direction = childForward.direction;
        direction--;
        direction = direction % 4;
        if (state.agent_direction < 0) 
            state.agent_direction += 4;

        Vector<Integer> tempSequence3 = (Vector<Integer>)childForward.sequence.clone();
        tempSequence3.addElement(state.ACTION_TURN_LEFT);
        frontier.add(new Node(childForward.x, childForward.y, direction, tempSequence3));
        visited.add(new Coordinates(childForward.x, childForward.y));
    }

    private Vector<Integer> breadth_first_search(int agent_x_position, int agent_y_position, int agent_direction)
    {
    Queue<Node> frontier = new LinkedList<Node>();
    Vector<Integer> sequence = new Vector<Integer>();
    Node start = new Node(agent_x_position, agent_y_position, agent_direction, sequence);
    frontier.add(start);
    
    Vector<Coordinates> visited = new Vector<Coordinates>();
    
    while (frontier.peek() != null)
    {
        Node current = frontier.remove();
        if (state.world[current.x][current.y] == state.UNKNOWN)
        {
        return current.sequence;
        }

        boolean isVisited = false;
        int x_pos = current.x;
        int y_pos = current.y;
        
        switch (current.direction) {
        case MyAgentState.NORTH:
            if (state.world[current.x][current.y - 1] != state.WALL)
        {
            y_pos--;
        }
        break;
        case MyAgentState.EAST:
        if (state.world[current.x + 1][current.y] != state.WALL)
        {
            x_pos++;
        }
        break;
        case MyAgentState.SOUTH:
        if (state.world[current.x][current.y + 1] != state.WALL)
        {
            y_pos++;
        }
        break;
        case MyAgentState.WEST:
        if (state.world[current.x - 1][current.y] != state.WALL)
        {
            x_pos--;
        }
        break;
        }
        
        for (Coordinates n : visited)
        {
            if (n.x == x_pos && n.y == y_pos)
                isVisited = true;
        }
        
        if (!isVisited)
            addChildren(frontier, visited, current, x_pos, y_pos);
            
    }

    Vector<Integer> failedSearchSeq = new Vector<Integer>();
    failedSearchSeq.addElement(4);
    return failedSearchSeq;
    }

    // moves the Agent to a random start position
    // uses percepts to update the Agent position - only the position, other percepts are ignored
    // returns a random action
    private Action moveToRandomStartPosition(DynamicPercept percept) {
    int action = random_generator.nextInt(6);
    initnialRandomActions--;
    state.updatePosition(percept);
    if(action==0) {
        state.agent_direction = ((state.agent_direction-1) % 4);
        if (state.agent_direction<0) 
        state.agent_direction += 4;
        state.agent_last_action = state.ACTION_TURN_LEFT;
        return LIUVacuumEnvironment.ACTION_TURN_LEFT;
    } else if (action==1) {
        state.agent_direction = ((state.agent_direction+1) % 4);
        state.agent_last_action = state.ACTION_TURN_RIGHT;
        return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
    } 
    state.agent_last_action=state.ACTION_MOVE_FORWARD;
    return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
    }
    
    
    @Override
    public Action execute(Percept percept) {
        
    // DO NOT REMOVE this if condition!!!
        if (initnialRandomActions>0) {
        return moveToRandomStartPosition((DynamicPercept) percept);
        } else if (initnialRandomActions==0) {
        // process percept for the last step of the initial random actions
        initnialRandomActions--;
        state.updatePosition((DynamicPercept) percept);
        System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
        state.agent_last_action=state.ACTION_SUCK;
        return LIUVacuumEnvironment.ACTION_SUCK;
        }
        
        // This example agent program will update the internal agent state while only moving forward.
        // START HERE - code below should be modified!
        
        
        System.out.println("x=" + state.agent_x_position);
        System.out.println("y=" + state.agent_y_position);
        System.out.println("dir=" + state.agent_direction);
        

        // mySequence = breadth_first_search(state.agent_x_position, state.agent_y_position, state.agent_direction);
        
    
    iterationCounter--;
        
    if (iterationCounter==0)
        return NoOpAction.NO_OP;

    DynamicPercept p = (DynamicPercept) percept;
    Boolean bump = (Boolean)p.getAttribute("bump");
    Boolean dirt = (Boolean)p.getAttribute("dirt");
    Boolean home = (Boolean)p.getAttribute("home");
    System.out.println("percept: " + p);
        
    // State update based on the percept value and the last action
    state.updatePosition((DynamicPercept)percept);
    if (bump) {
        switch (state.agent_direction) {
        case MyAgentState.NORTH:
        state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
        break;
        case MyAgentState.EAST:
        state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
        break;
        case MyAgentState.SOUTH:
        state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
        break;
        case MyAgentState.WEST:
        state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
        break;
        }
    }

    if (dirt)
        state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
    else
        state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
        
    state.printWorldDebug();
        
        
    // Next action selection based on the percept value
    if (dirt)
    {
        System.out.println("DIRT -> choosing SUCK action!");
        state.agent_last_action=state.ACTION_SUCK;
        return LIUVacuumEnvironment.ACTION_SUCK;
    } 
    else
    {
        if (mySequence.isEmpty())
        {
            mySequence = breadth_first_search(state.agent_x_position, state.agent_y_position, state.agent_direction);
        }
        int last_action = mySequence.remove(0);
        state.agent_last_action = last_action;
        if (last_action == 1)
        {
        	if (state.agent_direction == MyAgentState.NORTH)
        	{
        		state.agent_y_position--;
        	}
	    	if (state.agent_direction == MyAgentState.SOUTH)
	    	{
	    		state.agent_y_position++;
	    	}
	    	if (state.agent_direction == MyAgentState.EAST)
	    	{
	    		state.agent_x_position++;
	    	}
	    	if (state.agent_direction == MyAgentState.WEST)
	    	{
	    		state.agent_x_position--;
	    	}
            return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
        }
        if (last_action == 2) 
        {
            state.agent_direction = ((state.agent_direction+1) % 4);
            return LIUVacuumEnvironment.ACTION_TURN_RIGHT; 
        }
        if (last_action == 3) 
        {
            state.agent_direction = ((state.agent_direction-1) % 4);
            if (state.agent_direction<0) 
            {
                state.agent_direction += 4;
            }
            return LIUVacuumEnvironment.ACTION_TURN_LEFT;
        }
    }
    

    return LIUVacuumEnvironment.ACTION_SUCK;
    }
}


public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    super(new MyAgentProgram());
    }
}

class Node {
    public int x, y, direction;
    public Vector<Integer> sequence;
    Node(int x_, int y_, int dir, Vector<Integer> seq) {
    this.x = x_;
    this.y = y_;
    this.direction = dir;
    this.sequence = seq;
    }
}

class Coordinates {
    public int x, y;
    Coordinates(int x_, int y_)
    {
    this.x = x_;
    this.y = y_;
    }
}