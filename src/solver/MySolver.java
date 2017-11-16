package solver;

/**
 * COMP3702 A3 2017 Support Code
 * v1.0
 * last updated by Nicholas Collins 19/10/17
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import problem.VentureManager;
import problem.Matrix;
import problem.ProblemSpec;

public class MySolver implements FundingAllocationAgent {
	
	private ProblemSpec spec = new ProblemSpec();
	private VentureManager ventureManager;
    private List<Matrix> probabilities;
    private double discountFactor = spec.getDiscountFactor();
    private MDP mdp;
    private HashMap<List<Integer>,List<List<Integer>>> actionsForStates = new HashMap<List<Integer>,List<List<Integer>>>();
    private HashMap<List<Integer>,Double> rewards = new HashMap<List<Integer>,Double>();
    private HashMap<List<Integer>,List<Double>> statesNextStatesProb = new HashMap<List<Integer>,List<Double>>();
	private HashMap<List<Integer>,List<List<Integer>>> statesNextStates = new HashMap<List<Integer>,List<List<Integer>>>();
    private HashMap<List<Integer>,Double> valueTable = new HashMap<List<Integer>,Double>();
    private HashMap<List<Integer>,List<Integer>> optimalAction = new HashMap<List<Integer>,List<Integer>>();
	
	public MySolver(ProblemSpec spec) throws IOException {
	    this.spec = spec;
		ventureManager = spec.getVentureManager();
        probabilities = spec.getProbabilities();
        mdp =  new MDP(spec);
        actionsForStates = mdp.getActionsForStates();
        rewards = mdp.getReward();
        statesNextStates = mdp.getStatesNextStates();
        statesNextStatesProb = mdp.getStatesNextStatesProb();
	}
	
	public void doOfflineComputation() {
	    // TODO Write your own code here.
		Iterator it = mdp.getStates().entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pair = (HashMap.Entry)it.next();
	        List<Integer> state = (ArrayList<Integer>) pair.getValue();
	        valueTable.put(state, 0.0);
	    }
	    for(int i=0; i<10000;i++)
	    {
	    	it = this.valueTable.entrySet().iterator();
	    	//for each state
		    while (it.hasNext()) {
		        HashMap.Entry pair = (HashMap.Entry)it.next();
		        List<Integer> state = (ArrayList<Integer>) pair.getKey();
		        List<List<Integer>> actions = actionsForStates.get(state);
		        //for each action
		        for(int j=0;j<actions.size();j++)
		        {
		        	List<Integer> stateXaction = new ArrayList<Integer>();
		        	for(int k=0;k<state.size();k++)
		        	{
		        		stateXaction.add(state.get(k)+actions.get(j).get(k));
		        	}
		        	List<Double> nextStatesProb = statesNextStatesProb.get(stateXaction);
		        	List<List<Integer>> nextStates = statesNextStates.get(stateXaction);
		        	//for each next state
		        	double futureValue = 0;
		        	for(int l=0;l<nextStates.size();l++)
		        	{
		        		futureValue += nextStatesProb.get(l)*valueTable.get(nextStates.get(l));
		        	}
		        	double actionValue = rewards.get(stateXaction)+ discountFactor*futureValue;
		        	if(actionValue>valueTable.get(state))
		        	{
		        		valueTable.put(state,actionValue);
		        		optimalAction.put(state, actions.get(j));
		        	}
		        	
		        	
		        }
		    }
	    }
//	    Iterator it1 = this.optimalAction.entrySet().iterator();
//    	//for each state
//	    while (it1.hasNext()) {
//	        HashMap.Entry pair = (HashMap.Entry)it1.next();
//	        List<Integer> state = (ArrayList<Integer>) pair.getKey();
//	        List<Integer> optimalAction= this.optimalAction.get(state);
//	        System.out.println(state+" "+optimalAction);
//	    }
	}
	
	public List<Integer> generateAdditionalFundingAmounts(List<Integer> manufacturingFunds,
														  int numFortnightsLeft) {
		// Example code that allocates an additional $10 000 to each venture.
		// TODO Replace this with your own code.

		List<Integer> additionalFunding = optimalAction.get(manufacturingFunds);
		return additionalFunding;
	}


}
