package solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import problem.Matrix;
import problem.ProblemSpec;
import problem.VentureManager;

public class MDP {
	
	
	public ProblemSpec problemSpec;
	public HashMap<Integer,Integer> generateState = new HashMap<Integer,Integer>();
	public List<Integer> currentAction;
	public HashMap<Integer,List<Integer>> allStates = new HashMap<Integer,List<Integer>>();
	public HashMap<List<Integer>,List<List<Integer>>> actionsForAState = new HashMap<List<Integer>,List<List<Integer>>>();
	public List<Integer> currentState;
	
	public HashMap<List<Integer>,List<Double>> rewardForAState = new HashMap<List<Integer>,List<Double>>();
	
	public List<Matrix> newProb = new ArrayList<Matrix>();
	
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
	
	public void generateActionsForEachState(){
		
		VentureManager ventureManager = this.problemSpec.getVentureManager();
		
		int maxAdditionalFund = ventureManager.getMaxAdditionalFunding();
		
		int maxFund = ventureManager.getMaxManufacturingFunds();
		
		int size = this.allStates.size();
		
		List<List<Integer>> validAction = new ArrayList<List<Integer>>();
		
		for(int i=0;i<size;i++){
			
			this.currentState = this.allStates.get(i);
			
			for(int u=0;u<size;u++){
				
				List<Integer> pendingState = this.allStates.get(u);
				
				if(ventureManager.getName().equals("bronze")){
						
					int currentFund = this.currentState.get(0)+this.currentState.get(1);
					int allocatedFund = pendingState.get(0)+pendingState.get(1);
					boolean validation1 = (this.currentState.get(0) + pendingState.get(0))<maxFund+1;
					boolean validation2 = (pendingState.get(1) + this.currentState.get(1))<maxFund+1;
					boolean validation3 = (pendingState.get(0) + pendingState.get(1))<maxAdditionalFund+1;
					boolean validation4 = (currentFund+allocatedFund)<maxFund+1;
					if(validation1 && validation2 && validation3 && validation4){
						validAction.add(pendingState);
					}
				}
				
				else if(ventureManager.getName().equals("silver")){
					
					int currentFund = this.currentState.get(0)+this.currentState.get(1);
					int allocatedFund = pendingState.get(0)+pendingState.get(1);
					boolean validation1 = (this.currentState.get(0) + pendingState.get(0))<maxFund+1;
					boolean validation2 = (pendingState.get(1) + this.currentState.get(1))<maxFund+1;
					boolean validation3 = (pendingState.get(0) + pendingState.get(1))<maxAdditionalFund+1;
					boolean validation4 = (currentFund+allocatedFund)<maxFund+1;
					if(validation1 && validation2 && validation3 && validation4){
						validAction.add(pendingState);
					}
				}
				
				else if(ventureManager.getName().equals("gold")){
					
					int currentFund = this.currentState.get(0)+this.currentState.get(1)+this.currentState.get(2);
					int allocatedFund = pendingState.get(0)+pendingState.get(1)+pendingState.get(2);
					boolean validation1 = (this.currentState.get(0) + pendingState.get(0))<maxFund+1;
					boolean validation2 = (pendingState.get(1) + this.currentState.get(1))<maxFund+1;
					boolean validation3 = (this.currentState.get(2) + pendingState.get(2))<maxFund+1;
					boolean validation4 = (pendingState.get(0) + pendingState.get(1) + pendingState.get(2))<maxAdditionalFund+1;
					boolean validation5 = (currentFund+allocatedFund)<maxFund+1;
					if(validation1 && validation2 && validation3 && validation4 && validation5){
						validAction.add(pendingState);
					}
				}
				
				else if(ventureManager.getName().equals("platinum")){
					
					int currentFund = this.currentState.get(0)+this.currentState.get(1)+this.currentState.get(2);
					int allocatedFund = pendingState.get(0)+pendingState.get(1)+pendingState.get(2);
					boolean validation1 = (this.currentState.get(0) + pendingState.get(0))<maxFund+1;
					boolean validation2 = (pendingState.get(1) + this.currentState.get(1))<maxFund+1;
					boolean validation3 = (this.currentState.get(2) + pendingState.get(2))<maxFund+1;
					boolean validation4 = (pendingState.get(0) + pendingState.get(1) + pendingState.get(2))<maxAdditionalFund+1;
					boolean validation5 = (currentFund+allocatedFund)<maxFund+1;
					if(validation1 && validation2 && validation3 && validation4 && validation5){
						validAction.add(pendingState);
					}
				}
			}
			
			this.actionsForAState.put(currentState,validAction);
			validAction = new ArrayList<List<Integer>>();
		}
		
		
		
		
		
		

	}
	
	public void generateMatrix(){
		
		for(int i=0;i<this.problemSpec.getVentureManager().getNumVentures();i++){
			Matrix currentMatrix = this.problemSpec.getProbabilities().get(i);
			int maxFunds = this.problemSpec.getVentureManager().getMaxManufacturingFunds();
			double[][] matrixData = new double[maxFunds+1][maxFunds+1];
			
			for(int r=0;r<maxFunds+1;r++){
				
				
				for(int c=0;c<maxFunds+1;c++){
					

					if(c>r){
						matrixData[r][c] = 0.0;
					}
					else if(c>0 && c<=r){
						matrixData[r][c] = currentMatrix.get(r,r-c);
					}
					else if(c==0){
						double totalProb = 0;
						for(int u=r;u<maxFunds+1;u++){
							totalProb += currentMatrix.get(r,u);
						}
						matrixData[r][c] = totalProb;
					}
					
				}
				
				
			}

			this.newProb.add(new Matrix(matrixData));
			System.out.println("DONE!");
			
		}
	}
	
//	public void generateRewards(){
//		
//		double profit = 0;
//		double loss = 0;
//		double immediateReward = 0;
////		List<Double> immediateRewards = new ArrayList<Double>();
//		for(int k=0;k<this.allStates.size();k++){
//			List<Integer> currentState = this.allStates.get(k); 
//			immediateReward = 0;
////			immediateRewards = new ArrayList<Double>();
//			for(int u=0;u<this.problemSpec.getVentureManager().getNumVentures();u++){
//				
//				int venture1InitialFunds = currentState.get(u);
//			    for(int i=1;i<this.problemSpec.getVentureManager().getMaxManufacturingFunds()+1;i++){
//			    	
//			    	profit += Math.min(i, venture1InitialFunds) * this.problemSpec.getProbabilities().get(u).get(venture1InitialFunds, i)
//			    			*this.problemSpec.getSalePrices().get(u);
//			    	
//			    }
//			    
//			    for(int i=venture1InitialFunds+1;i<this.problemSpec.getVentureManager().getMaxManufacturingFunds()+1;i++){
//			    	loss += (i-venture1InitialFunds)*this.problemSpec.getProbabilities().get(u).get(venture1InitialFunds, i);
//
//
//			    }
//			    profit = profit*0.6;
//			    loss = loss*0.25;
//			    immediateReward +=  (profit - loss);
////			    immediateRewards.add(immediateReward);
//			    
//			    profit = 0;
//			    loss = 0;
//			}
//			 this.rewardForAState.put(currentState,immediateReward);
//		}
		
	public void generateRewards(){
		
		double profit = 0;
		double loss = 0;
		double immediateReward = 0;
		List<Double> immediateRewards = new ArrayList<Double>();
		for(int k=0;k<this.allStates.size();k++){
			List<Integer> currentState = this.allStates.get(k); 
			immediateReward = 0;
			immediateRewards = new ArrayList<Double>();
			for(int u=0;u<this.problemSpec.getVentureManager().getNumVentures();u++){
				
				int venture1InitialFunds = currentState.get(u);
			    for(int i=1;i<this.problemSpec.getVentureManager().getMaxManufacturingFunds()+1;i++){
			    	
			    	profit += Math.min(i, venture1InitialFunds) * this.problemSpec.getProbabilities().get(u).get(venture1InitialFunds, i)
			    			*this.problemSpec.getSalePrices().get(u);
			    	
			    }
			    
			    for(int i=venture1InitialFunds+1;i<this.problemSpec.getVentureManager().getMaxManufacturingFunds()+1;i++){
			    	loss += (i-venture1InitialFunds)*this.problemSpec.getProbabilities().get(u).get(venture1InitialFunds, i);


			    }
			    profit = profit*0.6;
			    loss = loss*0.25;
			    immediateReward =  (profit - loss);
			    immediateRewards.add(immediateReward);
			    
			    profit = 0;
			    loss = 0;
			}
			 this.rewardForAState.put(currentState,immediateRewards);
		}

	    
	}
	
//	public static void main(String[] args) throws IOException{
//		
//		String filePath = "C:\\Users\\gohzenhao\\workspace\\Assignment3\\bronze1.txt";
//		
//		ProblemSpec problem = new ProblemSpec(filePath);
//		
//		MDP mdp = new MDP(problem);
//		
//		mdp.generateStates();
//		
////		System.out.println("All possible states : ");
////		
////		for(int i=0;i<mdp.hashMapCounter;i++){
////			System.out.println(mdp.allStates.get(i));
////		}
//		
//		System.out.println("Size of possible states : "+mdp.allStates.size());
//		
//		mdp.generateActionsForEachState();
//		
//		for(int i=0;i<mdp.hashMapCounter;i++){
//			
//			System.out.println("Possible actions for state : "+mdp.allStates.get(i));
//			List<List<Integer>> validAction = mdp.actionsForAState.get(mdp.allStates.get(i));
//			for(int u=0;u<validAction.size();u++){
//				System.out.println(validAction.get(u));
//			}
//		}
//		
//		for(int i=0;i<problem.getVentureManager().getMaxManufacturingFunds();i++){
//			
//			
//			for(int u=0;u<problem.getVentureManager().getMaxManufacturingFunds();u++){
//				
//				System.out.println(problem.getProbabilities().get(0).get(i, u));
//			}
//		}
//		
//		
//	}

}
