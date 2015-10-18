package turing.main;

import java.util.ArrayList;
import java.util.Stack;

import common.structs.State;
import common.structs.Symbol;
import pushdown.structs.PushdownData;
import pushdown.structs.AutomatonStatus;
import pushdown.structs.TransitionRule;
import util.TokenizedLines;

/**
 * @author jose
 *
 *  The machine itself. The only class that knows how to work with
 *  the MachineData it contains (e.g. apply transitions).
 */
public abstract class Turing {
    String debugStr = "";
    
	PushdownData data;
	TokenizedLines traceHist = new TokenizedLines();
	
	public Turing(PushdownData data){
		this.data = data;
		
		// TODO make sure all arguments are correct.
		
		// Make sure all acceptStates are in stateSet.
		for (State s : data.getAcceptStates()){
			if(!data.getStateSet().contains(s))
				System.err.println("An acceptance state is not in the defined state set.");
		}
	}
	
	/**
	 * Evaluates an input string. The main purpose of a logical automaton.
	 * @return	Boolean indicating acceptance of input string.
	 */
	public boolean evaluateString(String inputString){
		
	    ArrayList<TraceTrail> possibleTrails = new ArrayList<TraceTrail>();
	    ArrayList<TraceTrail> newTrails = new ArrayList<TraceTrail>();
	    
		// Add the initial configuration into the statuses array.
		Stack<Symbol> initialStack = new Stack<Symbol>();
		initialStack.push(data.getInitialStackSymbol());
		AutomatonStatus initialStatus = new AutomatonStatus(data.getInitialState(), inputString, initialStack);
		TraceTrail initialTrail = new TraceTrail();
		initialTrail.add(initialStatus);
		possibleTrails.add(initialTrail);
		
		// Iterate until all possibilities are exhausted.
		while (!possibleTrails.isEmpty()){

		    for (TraceTrail tt : possibleTrails){

    			ArrayList<TransitionRule> applicableTransitions = findApplicableTransitionRules(tt.getLast());
    			// Apply each of them to create a new status, that will be evaluated next round.
    			for (TransitionRule tr : applicableTransitions){
                    TraceTrail newTrail = new TraceTrail();
                    newTrail.addAll(tt);
    				AutomatonStatus newStatus = applyTransition(tt.getLast(), tr);
    				newTrail.add(newStatus);
    				// If this new status has an empty stack AND input string... accept.
    				if (newStatus.getCurrentStack().isEmpty() &&
    						newStatus.getRemainingInputString().isEmpty()){
    				    // Record winning trace into history.
    				    traceHist = MachineIO.traceTrailAsTokenizedLines(newTrail);
    					return true;
    				}
    				newTrails.add(newTrail);
    			}
    		}
    		possibleTrails.clear();
    		possibleTrails.addAll(newTrails);
    		newTrails.clear();
		}
		// If all possibilities have been exhausted and the string
		// hasn't been accepted, then the string is rejected. 
		return false;
	}
	
	// Special input string character and stack symbol.
	private final Character EPSILON_INPUT = '-';
	private final Symbol EPSILON_SYMBOL = new Symbol("-");
	
	/**
	 * @param ts A frozen state of the automaton variables.
	 * @return	All applicable transition rules for the given automaton status.
	 */
	public ArrayList<TransitionRule> findApplicableTransitionRules(AutomatonStatus ts){
	    
//	    debugStr = "";
	    
	    debugStr.concat("Finding applicable transitions for " + MachineIO.getStatusAsTokenizedLine(ts));
	    System.out.println("Finding applicable transitions for " + MachineIO.getStatusAsTokenizedLine(ts));
	    
	    String strNo = "Unapplicable transition ";
	    
		ArrayList<TransitionRule> applicableTransitions = new ArrayList<TransitionRule>();
		
		// Special case
		if (ts.getCurrentStack().isEmpty()) return applicableTransitions;
		
		// Normal
 		for (TransitionRule tr : data.getTransitionRules()){

//            String deny = strNo + AutomataIO.getTransitionAsTokenizedLine(tr) + "\n Reason: ";
 		   String deny = strNo + MachineIO.getTransitionAsTokenizedLine(tr) + "  Reason: ";
 		    
 			if (tr.getPrevState().getName().equals(ts.getCurrentState().getName())){
 			    // todo bien
 			} else {
 			    debugStr.concat(deny + "Different required state.");
 			    System.out.println(deny + "Different required state.");
 			    continue;
 			}
		    
 			if (tr.getRequiredStackSymbol().getName().equals(ts.getCurrentStack().peek().getName())){
 				// tudo bem
 			} else {
 			   debugStr.concat(deny + "Different required stack symbol.");
 			   System.out.println(deny + "Different required stack symbol.");
 			   continue;
 			}
 			
		    if (tr.getRequiredInputCharacter() == EPSILON_INPUT ||
		            (!ts.getRemainingInputString().isEmpty() &&
 			            tr.getRequiredInputCharacter() == ts.getRemainingInputString().charAt(0))){
		        // daijoubu
		    } else {
		        debugStr.concat(deny + "Different required input character.");
		        System.out.println(deny + "Different required input character.");
		        continue;
 			}
		    
		    debugStr.concat("Applicable transition " + MachineIO.getTransitionAsTokenizedLine(tr));
		    System.out.println("Applicable transition " + MachineIO.getTransitionAsTokenizedLine(tr));
            applicableTransitions.add(tr);
 		}
 		return applicableTransitions;
	}
	
	/**
	 * Given an automaton status and applicable transition rule,
	 * compute and return the resulting automaton status.
	 * 
	 * Note: The transition rule should have previously been deemed applicable
	 * for the given automaton status (no checks are made).
	 * @param as	Machine status.
	 * @param tr	Transition rule to be applied to the status.
	 * @return	New automaton status.
	 */
	public AutomatonStatus applyTransition(AutomatonStatus as, TransitionRule tr){
		
	 // Special case
	    // TODO: Remove this because it should never happen?
        if (as.getCurrentStack().isEmpty()){
            System.err.println("Somehow, unapplicable transition was considered applicable.");
            System.exit(1);
        }
	    
        String oldString = as.getRemainingInputString();
	    
	    State newState = tr.getNextState();
		String newString = oldString.substring(1, oldString.length());
		
		Stack<Symbol> newStack = new Stack<Symbol>();
		
        for (Symbol s : as.getCurrentStack()){
		    newStack.push(s);
		}
        
        newStack.pop();

        
		if (!tr.getStackSymbolsToPush().get(0).getName().equals(EPSILON_SYMBOL.getName())){
		    for (Symbol s : tr.getStackSymbolsToPush()){
		        newStack.push(s);
		    }
		}
		
		AutomatonStatus newStatus = new AutomatonStatus(newState, newString, newStack);
		
		System.out.print(MachineIO.getStatusAsTokenizedLine(as) + "->");
		System.out.println(MachineIO.getStatusAsTokenizedLine(newStatus));
		return newStatus;
	}

	/*
	 * Getters and setters.
	 */
	
    public TokenizedLines getTraceHist() {
        return traceHist;
    }

    public String getDebugStr() {
        return debugStr;
    }
}