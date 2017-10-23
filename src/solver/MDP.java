package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import problem.ProblemSpec;
import problem.VentureManager;

public class MDP {
	
	
	public ProblemSpec problemSpec;
	public HashMap<Integer,Integer> generateState = new HashMap<Integer,Integer>();
	public List<Integer> currentAction;
	public HashMap<Integer,List<Integer>> allStates = new HashMap<Integer,List<Integer>>();
	public HashMap<Integer,List<Integer>> actionsForAState = new HashMap<Integer,List<Integer>>();
	
	public int hashMapCounter = 0;
	
	
	
	
	
	
	public MDP(ProblemSpec inProblem){
		
		this.problemSpec = inProblem;
		
	}
	
	public void generateStates(){
		
		VentureManager ventureManager = this.problemSpec.getVentureManager();
		
		System.out.println("Problem loaded : "+this.problemSpec.isModelLoaded());
		
		System.out.println("Generating states... ");
		
		int maxFunds = ventureManager.getMaxManufacturingFunds();
		
		
		List<Integer> fundsForFirstVenture = new ArrayList<Integer>();
		List<Integer> validState = new ArrayList<Integer>();
		for(int i=0;i<maxFunds+1;i++){
			fundsForFirstVenture.add(i);
			
		}
		
		//First Venture
		for(int i=0;i<maxFunds+1;i++){
			//Second Venture
			for(int u=0;u<maxFunds+1;u++){
				
				if(this.problemSpec.getVentureManager().getNumVentures()==2){
					
					if((fundsForFirstVenture.get(i)+u)<=maxFunds){
						validState.add(fundsForFirstVenture.get(i));
						validState.add(u);
						allStates.put(hashMapCounter, validState);
						generateActionsForEachState(hashMapCounter,validState);
						validState = new ArrayList<Integer>();
						hashMapCounter++;
						
					}
				}
				else{
					//Third Venture
					for(int e=0;e<maxFunds+1;e++){
						if((fundsForFirstVenture.get(i)+u+e)<=maxFunds){
							validState.add(fundsForFirstVenture.get(i));
							validState.add(u);
							validState.add(e);
							allStates.put(hashMapCounter, validState);
							validState = new ArrayList<Integer>();
							hashMapCounter++;
						}
					}
				}
				
			}

		}
		
		
	}
	
	public void generateActionsForEachState(int counter,List<Integer> inState){
		
//		int size = inState.size();
//		
//		for(int i=0;i<size;i++){
//			
//			int currentFund = inState.get(i);
//			
//		}
//		
		

	}
	
	public static void main(String[] args) throws IOException{
		
		String filePath = "C:\\Users\\gohzenhao\\workspace\\Assignment3\\bronze1.txt";
		
		ProblemSpec problem = new ProblemSpec(filePath);
		
		MDP mdp = new MDP(problem);
		
		mdp.generateStates();
		
		System.out.println("All possible states : ");
		
		for(int i=0;i<mdp.hashMapCounter;i++){
			System.out.println(mdp.allStates.get(i));
		}
		
	}

}
