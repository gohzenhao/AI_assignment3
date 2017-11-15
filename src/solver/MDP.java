package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import problem.ProblemSpec;
import problem.VentureManager;

public class MDP {
	
	
	private ProblemSpec problemSpec;
	private VentureManager ventureManager;
	private HashMap<Integer,Integer> generateState = new HashMap<Integer,Integer>();
	private List<Integer> currentAction;
	private HashMap<List<Integer>,List<Integer>> allStates = new HashMap<List<Integer>,List<Integer>>();
	private HashMap<List<Integer>,List<Integer>> allActions = new HashMap<List<Integer>,List<Integer>>();
	private HashMap<List<Integer>,List<List<Integer>>> actionsForStates = new HashMap<List<Integer>,List<List<Integer>>>();

	
	public MDP(ProblemSpec inProblem){		
		problemSpec = inProblem;
//		ventureManager = problemSpec.getVentureManager();
		ventureManager = new VentureManager("bronze");
	}
	
	public void generateStates(){
		generateActions();
		System.out.println("Problem loaded : "+this.problemSpec.isModelLoaded());		
		System.out.println("Generating states... ");		
		int maxFunds = ventureManager.getMaxManufacturingFunds();
		List<Integer> validState = new ArrayList<Integer>();
		//First Venture
		for(int i=0;i<maxFunds+1;i++){
			//Second Venture
			for(int u=0;u<maxFunds+1;u++){				
				if(ventureManager.getNumVentures()==2){					
					if((i+u)<=maxFunds){
						validState.add(i);
						validState.add(u);
						allStates.put(validState, validState);
						generateActionsForState(validState);
						validState = new ArrayList<Integer>();							
					}
				}
				else{
					//Third Venture
					for(int e=0;e<maxFunds+1;e++){
						if((i+u+e)<=maxFunds){
							validState.add(i);
							validState.add(u);
							validState.add(e);
							allStates.put(validState, validState);
							generateActionsForState(validState);
							validState = new ArrayList<Integer>();
						}
					}
				}				
			}
		}
		
	}
	
	/**
	 * generate all possible actions for different venture manager
	 */
	public void generateActions()
	{
		Integer maxAdditionalFunding = ventureManager.getMaxAdditionalFunding();
		List<Integer> validAction = new ArrayList<Integer>();
		for(int i=0;i<maxAdditionalFunding+1;i++){
			//Second Venture
			for(int u=0;u<maxAdditionalFunding+1;u++){				
				if(ventureManager.getNumVentures()==2){					
					if((i+u)<=maxAdditionalFunding){
						validAction.add(i);
						validAction.add(u);
						allActions.put(validAction, validAction);
						validAction = new ArrayList<Integer>();					
					}
				}
				else{
					//Third Venture
					for(int e=0;e<maxAdditionalFunding+1;e++){
						if((i+u+e)<=maxAdditionalFunding){
							validAction.add(i);
							validAction.add(u);
							validAction.add(e);
							allActions.put(validAction, validAction);
							validAction = new ArrayList<Integer>();
						}
					}
				}				
			}
		}
	}
	
	public void generateActionsForState(List<Integer> validState){
		int v1,v2,v3=0;
		List<List<Integer>> allPossibleActions = new ArrayList<List<Integer>>(); 
		v1 = validState.get(0);
		v2 = validState.get(1);
		String name = ventureManager.getName();
		if(name.equals("gold") || name.equals("platinum"))
			v3 = validState.get(2);
		//loop through allActions
		Iterator it = allActions.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pair = (HashMap.Entry)it.next();
	        List<Integer> action = (ArrayList<Integer>) pair.getValue();
	        int a1,a2,a3,totalFund;
	        a1 = action.get(0);
	        a2 = action.get(1);
	        totalFund = a1+a2+v1+v2;
			if(name.equals("gold") || name.equals("platinum"))
			{
				a3 = action.get(2);
				totalFund = a1+a2+a3+v1+v2+v3;
			}
			if(totalFund <= ventureManager.getMaxAdditionalFunding())
				allPossibleActions.add(action);
	    }
	    actionsForStates.put(validState, allPossibleActions);
	}
	
	public static void main(String[] args) throws IOException{
		
		String filePath = "C:\\Users\\User-PC\\eclipse-workspace\\AI-ass3\\testcases\\bronze1.txt";
		
		ProblemSpec problem = new ProblemSpec(filePath);
		
		MDP mdp = new MDP(problem);
		
		mdp.generateStates();
		mdp.generateActions();
		System.out.println(mdp.actionsForStates.size());
		System.out.println("All possible states : ");
		Iterator it = mdp.allActions.entrySet().iterator();
	    while (it.hasNext()) {
	        HashMap.Entry pair = (HashMap.Entry)it.next();
	        List<Integer> state = (ArrayList<Integer>) pair.getValue();
	        System.out.println(state);
	        System.out.println("Possible actions:");	        
	        List<List<Integer>> actions= mdp.actionsForStates.get(state);
	        System.out.println(actions.size());
		    for(int i=0;i<actions.size();i++)
		    {
		    	System.out.println("Action "+(i+1)+":"+actions.get(i));
		    }		        
	    }
    
		
//		System.out.println("All possible actions : ");
//		System.out.println(mdp.allActions.toString());
//		System.out.println(mdp.allStates.size());
//		System.out.println(mdp.allActions.size());
		
	}

}
