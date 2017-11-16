package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import problem.Matrix;
import problem.ProblemSpec;
import problem.VentureManager;

public class MDP {
	
	
	private ProblemSpec problemSpec;
	private VentureManager ventureManager;
	private String name;
	private List<Matrix> probabilities;
	private HashMap<Integer,Integer> generateState = new HashMap<Integer,Integer>();
	private List<Integer> currentAction;
	private HashMap<List<Integer>,List<Integer>> allStates = new HashMap<List<Integer>,List<Integer>>();
	private HashMap<List<Integer>,List<Integer>> allActions = new HashMap<List<Integer>,List<Integer>>();
	private HashMap<List<Integer>,List<List<Integer>>> actionsForStates = new HashMap<List<Integer>,List<List<Integer>>>();
	private HashMap<List<Integer>,Double> rewards = new HashMap<List<Integer>,Double>();
	private List<Matrix> transitionFunction=new ArrayList<Matrix>();
	private HashMap<List<Integer>,List<Double>> statesNextStatesProb = new HashMap<List<Integer>,List<Double>>();
	private HashMap<List<Integer>,List<List<Integer>>> statesNextStates = new HashMap<List<Integer>,List<List<Integer>>>();

	
	public MDP(ProblemSpec inProblem){		
		problemSpec = inProblem;
		ventureManager = problemSpec.getVentureManager();
		name = ventureManager.getName();
		probabilities = problemSpec.getProbabilities();
		transitionFunction();
		generateStates();
	}
	
	/**
	 * generate all states given a particular venture manager
	 */
	public void generateStates(){
		generateActions();
//		System.out.println("Problem loaded : "+this.problemSpec.isModelLoaded());		
//		System.out.println("Generating states... ");		
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
						generateReward(validState);
						probabilitiesOfNextStates(validState);
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
							generateReward(validState);
							probabilitiesOfNextStates(validState);
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
	
	/**
	 * generate possible actions of a particular state
	 * @param validState state
	 */
	public void generateActionsForState(List<Integer> validState){
		int v1,v2,v3=0;
		List<List<Integer>> allPossibleActions = new ArrayList<List<Integer>>(); 
		v1 = validState.get(0);
		v2 = validState.get(1);
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
			if(totalFund <= ventureManager.getMaxManufacturingFunds())
				allPossibleActions.add(action);
	    }
	    actionsForStates.put(validState, allPossibleActions);
	}
	
	/**
	 * calculate R(s,a)
	 * @param afterAdditionalFundAllocation s+a
	 */
	public void generateReward(List<Integer> afterAdditionalFundAllocation)
	{
		int v1,v2,v3;
		List<Double> p1,p2,p3;
		double sellPrice1, sellPrice2, sellPrice3;
		v1 = afterAdditionalFundAllocation.get(0);
		v2 = afterAdditionalFundAllocation.get(1);
		p1 = probabilities.get(0).getRow(v1);
		p2 = probabilities.get(1).getRow(v2);
		sellPrice1 = problemSpec.getSalePrices().get(0);
		sellPrice2 = problemSpec.getSalePrices().get(1);
//		System.out.println(v1);
//		System.out.println(v2);
//		System.out.println(p1);
//		System.out.println(p2);
//		System.out.println(sellPrice1);
//		System.out.println(sellPrice2);
		double expectedReward=0;
		//possible order-i for venture-1 with v1 fund 
		for(int i=0;i<p1.size();i++)
		{
			//possible order-u for venture-2 with v2 fund 
			for(int u=0;u<p2.size();u++)
			{				
				double EU = 0;
				double probability = (double)Math.round(p1.get(i)*p2.get(u) * 100) / 100;
				// compute profit from sales
				int sold1,sold2,sold3,missed1,missed2,missed3;
				double profit=0;
				sold1 = Math.min(i, v1);
				sold2 = Math.min(u, v2);
//				System.out.println(sold1 +" "+ sold2);
//				System.out.println((sold1* sellPrice1+ sold2*sellPrice2)*0.6);
				profit +=  (double)Math.round((sold1* sellPrice1+ sold2*sellPrice2)*0.6 * 100) / 100;
	            // compute missed opportunity penalty
	            missed1 = i - sold1;
	            missed2 = u - sold2;
//	            System.out.println(missed1 +" "+ missed2);
//				System.out.println((missed1 * sellPrice1 + missed2 * sellPrice2) * 0.25);
	            profit -= (double)Math.round((missed1 * sellPrice1 + missed2 * sellPrice2) * 0.25 * 100) / 100;
	            EU = probability * profit;
	            EU = (double) Math.round(EU*10000)/10000;
//	            System.out.println("EU = "+probability+" * "+profit+" = "+EU);
				if(name.equals("gold") || name.equals("platinum"))
				{
					v3 = afterAdditionalFundAllocation.get(2);
					p3 = probabilities.get(2).getRow(v3);
					sellPrice3 = problemSpec.getSalePrices().get(2);
					//possible order-e for venture-3 with v3 fund 
					for(int e=0;e<p3.size();e++)
					{
						double profit1 = 0;
						probability = (double)Math.round(p1.get(i)*p2.get(u)*p3.get(e) * 100d) / 100d;
						sold3 = Math.min(e,v3);
						missed3 = e - sold3;
						profit1 = (double)Math.round((profit + sold3 * sellPrice3 * 0.6 - missed3 * sellPrice3 * 0.25 )* 100) / 100;
						EU = probability * profit1;
						EU = (double) Math.round(EU*10000)/10000;
					}
				}
				expectedReward += EU;
//				System.out.println(expectedReward);
			}
		}
//		System.out.println("Expected Reward: "+expectedReward);
		rewards.put(afterAdditionalFundAllocation, expectedReward);	
	}

	/**
	 * generate transitionFunction
	 */
	public void transitionFunction()
	{
		//matrix of each venture
		for(int i=0;i<probabilities.size();i++)
		{
			Matrix mat = probabilities.get(i);
			double[][] data = new double[mat.size()][mat.size()];
			//matrix-i row u
			for(int u=0;u<mat.size();u++)
			{
				//matrix-i col e
				for(int e=0;e<mat.size();e++)
				{
					double probForCol = 0;
					System.out.print(mat.get(u, e)+" ");
					if(e==0)
					{
						for(int k=u;k<mat.size();k++)
						{
							probForCol +=mat.get(u, k);
						}
					}
					else if(e>0 && e<=u)
						probForCol = mat.get(u, u-e);
					else
						probForCol = 0;
					data[u][e] = (double) Math.round(probForCol*1000)/1000;
				}
			}
			Matrix tran = new Matrix(data);
			transitionFunction.add(tran);
		}
	}
	
	/**
	 * to get all next states a particular state will get into with the probabilities
	 * @param afterAdditionalFundAllocation current state
	 */
	public void probabilitiesOfNextStates(List<Integer> afterAdditionalFundAllocation)
	{
		List<Double> nextStatesProb = new ArrayList<Double>();
		List<List<Integer>> nextStates = new ArrayList<List<Integer>>();
		List<List<Double>> venturesProb = new ArrayList<List<Double>>();
		for(int i=0;i<afterAdditionalFundAllocation.size();i++)
		{
			venturesProb.add(transitionFunction.get(i).getRow(afterAdditionalFundAllocation.get(i)));
		}
		for(int i=0;i<venturesProb.get(0).size();i++)
		{
			if(venturesProb.size()==2)
			{
				for(int j=0;j<venturesProb.get(1).size();j++)
				{
					double prob = venturesProb.get(0).get(i)*venturesProb.get(1).get(j);
					List<Integer> nextState=new ArrayList<Integer>();
					if(prob != 0)
					{
						nextState.add(i);
						nextState.add(j);
						nextStates.add(nextState);
						nextStatesProb.add((double)(Math.round(prob*1000))/1000);
					}
				}	
			}
			else
			{
				for(int j=0;j<venturesProb.get(1).size();j++)
				{
					for(int k=0;k<venturesProb.get(2).size();k++)
					{
						double prob = venturesProb.get(0).get(i)*venturesProb.get(1).get(j)*venturesProb.get(2).get(k);
						List<Integer> nextState=new ArrayList<Integer>();
						if(prob != 0)
						{
							nextState.add(i);
							nextState.add(j);
							nextState.add(k);
							nextStates.add(nextState);
							nextStatesProb.add((double)(Math.round(prob*1000))/1000);
						}
					}
				}
				
			}
		}
		statesNextStates.put(afterAdditionalFundAllocation, nextStates);
		statesNextStatesProb.put(afterAdditionalFundAllocation, nextStatesProb);
	}
	public HashMap<List<Integer>,List<List<Integer>>> getActionsForStates()
	{
		return actionsForStates;
	}
	
	public HashMap<List<Integer>,List<Integer>> getStates()
	{
		return allStates;
	}
	
	public HashMap<List<Integer>,Double> getReward()
	{
		return rewards;
	}
	
	public HashMap<List<Integer>,List<Double>> getStatesNextStatesProb()
	{
		return statesNextStatesProb;
	}
	
	public HashMap<List<Integer>,List<List<Integer>>> getStatesNextStates()
	{
		return statesNextStates;
	}
	
	
	
//	public static void main(String[] args) throws IOException{
//		
//		String filePath = "C:\\Users\\User-PC\\eclipse-workspace\\AI-ass3\\testcases\\platinum1.txt";
//		
//		ProblemSpec problem = new ProblemSpec(filePath);
//		
//		MDP mdp = new MDP(problem);
//		
//		mdp.generateStates();
//		mdp.generateActions();
//		System.out.println(mdp.actionsForStates.size());
//		System.out.println("All possible states : ");
//		Iterator it = mdp.allActions.entrySet().iterator();
//	    while (it.hasNext()) {
//	        HashMap.Entry pair = (HashMap.Entry)it.next();
//	        List<Integer> state = (ArrayList<Integer>) pair.getValue();
//	        System.out.println(state + " Expected Utility: "+mdp.rewards.get(state));
//	        System.out.println("Probability\t\t\tNext State");
//	        List<Double> nextStatesProb = mdp.statesNextStatesProb.get(state);
//	        List<List<Integer>> nextStates = mdp.statesNextStates.get(state);
//	        for(int i=0;i<nextStates.size();i++)
//	        {
//	        	System.out.println(nextStatesProb.get(i)+"\t\t\t"+nextStates.get(i));
//	        }
//		    
//		        
//	        System.out.println("Possible actions:");	        
//	        List<List<Integer>> actions= mdp.actionsForStates.get(state);
//	        System.out.println(actions.size());
//		    for(int i=0;i<actions.size();i++)
//		    {
//		    	System.out.println("Action "+(i+1)+":"+actions.get(i));
//		    }		    
//	    }
//	    System.out.println("Transtion Function");
//	    for(int i=0;i<mdp.transitionFunction.size();i++)
//		{
//	    	System.out.println(mdp.transitionFunction.get(i).toString());
//		}
//    
//		
//		System.out.println("All possible actions : ");
//		System.out.println(mdp.allActions.toString());
////		System.out.println(mdp.allStates.size());
//		System.out.println(mdp.allActions.size());
//		
//	}

}
