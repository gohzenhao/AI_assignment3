package solver;

/**
 * COMP3702 A3 2017 Support Code
 * v1.0
 * last updated by Nicholas Collins 19/10/17
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import problem.VentureManager;
import problem.Matrix;
import problem.ProblemSpec;

public class MySolver implements FundingAllocationAgent {
	
	private ProblemSpec spec = new ProblemSpec();
	private VentureManager ventureManager;
    private List<Matrix> probabilities;
    public HashMap<List<Integer>,List<Integer>> bestAction = new HashMap<List<Integer>,List<Integer>>();
    public MDP mdp;
	
	public MySolver(ProblemSpec spec) throws IOException {
	    this.spec = spec;
		ventureManager = spec.getVentureManager();
        probabilities = spec.getProbabilities();
        this.mdp =  new MDP(spec);
	}
	
	public void doOfflineComputation() {
		
		HashMap<List<Integer>,Double> valueForStates = new HashMap<List<Integer>,Double>();
		double discountFactor = this.spec.getDiscountFactor();
		
	    mdp.generateStates();
	    mdp.generateActionsForEachState();
	    for(int i=0;i<mdp.allStates.size();i++){
	    	valueForStates.put(mdp.allStates.get(i), 0.0);
	    }
	    
	    mdp.generateRewards();
	    mdp.generateMatrix();
	    
	    Matrix probV1 = mdp.newProb.get(0);
	    Matrix probV2 = mdp.newProb.get(1);
	    for(int k=0;k<3;k++){
	    	for(int j=0;j<mdp.allStates.size();j++){
	    		
	    		if(this.ventureManager.getName().equals("bronze") || this.ventureManager.getName().equals("silver")){
		    	    List<Integer> currentState = mdp.allStates.get(j);
		    	    List<List<Integer>> possibleActions = mdp.actionsForAState.get(currentState);
		    	    double currentRewardV1 = mdp.rewardForAState.get(currentState).get(0);
		    	    double currentRewardV2 = mdp.rewardForAState.get(currentState).get(1);
	
		    	    List<Integer> highestValueAction = new ArrayList<Integer>();
		    	    double highestValue = 0;
		    		
				    for(int i=0;i<possibleActions.size();i++){
				    	List<Integer> newState = new ArrayList<Integer>();
				    	List<Integer> currentAction = possibleActions.get(i);
				    	int v1 = currentAction.get(0) + currentState.get(0);
				    	int v2 = currentAction.get(1) + currentState.get(1);
				    	newState.add(v1);
				    	newState.add(v2);
				    	double newValueV1 = 0;
				    	double newValueV2 = 0;
				    	for(int u=0;u<v1+1;u++){
				    		
				    		double prob = probV1.get(v1, u);
				    		double value = valueForStates.get(newState);
	//			    		System.out.println("--"+value);
				    		
				    		newValueV1 += prob*value;
				    		
				    		
				    	}
				    	
				    	for(int u=0;u<v2+1;u++){
				    		
				    		double prob = probV2.get(v2, u);
				    		double value = valueForStates.get(newState);
				    		
				    		newValueV2 += prob*value;
				    		
				    		
				    	}
				    	
				    	double totalValue = newValueV1*newValueV2;
				    	if(totalValue>highestValue){
				    		highestValue = totalValue;
				    		highestValueAction = currentAction;
				    	}
				    	
				    }
				    
				    double valueIteration = (currentRewardV1+currentRewardV2) + discountFactor*highestValue;
				    valueForStates.put(currentState,valueIteration);
				    this.bestAction.put(currentState, highestValueAction);
				    System.out.println("Current reward after value iteration for state : "+currentState+" is : "+valueForStates.get(currentState));
			    }
	    		else{
	    			Matrix probV3 = mdp.newProb.get(2);
	    			List<Integer> currentState = mdp.allStates.get(j);
		    	    List<List<Integer>> possibleActions = mdp.actionsForAState.get(currentState);
		    	    double currentRewardV1 = mdp.rewardForAState.get(currentState).get(0);
		    	    double currentRewardV2 = mdp.rewardForAState.get(currentState).get(1);
		    	    double currentRewardV3 = mdp.rewardForAState.get(currentState).get(2);
	
		    	    List<Integer> highestValueAction = new ArrayList<Integer>();
		    	    double highestValue = 0;
		    		
				    for(int i=0;i<possibleActions.size();i++){
				    	List<Integer> newState = new ArrayList<Integer>();
				    	List<Integer> currentAction = possibleActions.get(i);
				    	int v1 = currentAction.get(0) + currentState.get(0);
				    	int v2 = currentAction.get(1) + currentState.get(1);
				    	int v3 = currentAction.get(2) + currentState.get(2);
				    	newState.add(v1);
				    	newState.add(v2);
				    	newState.add(v3);
				    	double newValueV1 = 0;
				    	double newValueV2 = 0;
				    	double newValueV3 = 0;
				    	for(int u=0;u<v1+1;u++){
				    		
				    		double prob = probV1.get(v1, u);
				    		double value = valueForStates.get(newState);
	//			    		System.out.println("--"+value);
				    		
				    		newValueV1 += prob*value;
				    		
				    		
				    	}
				    	
				    	for(int u=0;u<v2+1;u++){
				    		
				    		double prob = probV2.get(v2, u);
				    		double value = valueForStates.get(newState);
				    		
				    		newValueV2 += prob*value;
				    		
				    		
				    	}
				    	
				    	for(int u=0;u<v3+1;u++){
				    		
				    		double prob = probV3.get(v3, u);
				    		double value = valueForStates.get(newState);
				    		
				    		newValueV3 += prob*value;
				    		
				    		
				    	}
				    	
				    	double totalValue = newValueV1*newValueV2*newValueV3;
				    	if(totalValue>highestValue){
				    		highestValue = totalValue;
				    		highestValueAction = currentAction;
				    	}
				    	
				    }
				    
				    double valueIteration = (currentRewardV1+currentRewardV2+currentRewardV3) + discountFactor*highestValue;
				    valueForStates.put(currentState,valueIteration);
				    this.bestAction.put(currentState, highestValueAction);
	    			
	    		}
	    	}
	    }
	    
//	    double valueIteration = (currentRewardV1+currentRewardV2) + discountFactor*highestValue;
//	    valueForStates.put(currentState,valueIteration);
//	    
//	    System.out.println("Current reward after value iteration for state : "+currentState+" is : "+valueForStates.get(currentState));
	    
//	    System.out.println("After running value iteration for a few times");
//	    for(int i=0;i<mdp.allStates.size();i++){
//	    	System.out.println("Best action for state : "+mdp.allStates.get(i)+" is : "+bestAction.get(mdp.allStates.get(i)));
//	    }
	    
	    
	    
	}
	
	public List<Integer> generateAdditionalFundingAmounts(List<Integer> manufacturingFunds,
														  int numFortnightsLeft) {
		// Example code that allocates an additional $10 000 to each venture.
		// TODO Replace this with your own code.

//		List<Integer> additionalFunding = new ArrayList<Integer>();
//		System.out.println("Hey hey");
//		int totalManufacturingFunds = 0;
//		for (int i : manufacturingFunds) {
//			totalManufacturingFunds += i;
//		}
//		
//		int totalAdditional = 0;
//		for (int i = 0; i < ventureManager.getNumVentures(); i++) {
//			if (totalManufacturingFunds >= ventureManager.getMaxManufacturingFunds() ||
//			        totalAdditional >= ventureManager.getMaxAdditionalFunding()) {
//				additionalFunding.add(0);
//			} else {
//				additionalFunding.add(1);
//				totalAdditional ++;
//				totalManufacturingFunds ++;
//			}
//		}
		
		List<Integer> additionalFunding = this.bestAction.get(manufacturingFunds);

		return additionalFunding;
	}

}
