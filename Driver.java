import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Driver {
	

	
	public static void main(String[] args){
		
		File inputFile = new File("C:\\Users\\gohzenhao\\workspace\\Assignment3\\bronze1.txt");

		FileReader fr = null;
		BufferedReader br = null;
		int counter= 0;
		int numOfVentures = 0;
		int matrixCounter = 0;
		
		
		String customerType = null;
		double discountFactor = 0;
		int numberOfFortnight = 0;
		int maxFund = 0;
		int additionalFund = 0;
		List<Integer> productPrice = new ArrayList<Integer>();
		List<Integer> fundAtTheBeginning = new ArrayList<Integer>();
		List<double[][]> matrices = new ArrayList<double[][]>();
		double[][] matrix = null;
		
		
		try {
			if(inputFile.exists())
			{

				fr = new FileReader(inputFile);
				br = new BufferedReader(fr);
				
				String line = br.readLine();
				try
				{
					while(line!=null)
					{
						counter++;
						if(counter==1){
							customerType = line;
							
							maxFund = 3;
							additionalFund = 3;
							numOfVentures = 2;
							
							//This part is not working --------
								if(customerType=="bronze"){
									System.out.println("why");
									maxFund = 3;
									additionalFund = 3;
									numOfVentures = 2;
								}
								else if(customerType=="silver"){
									maxFund = 5;
									additionalFund = 4;
									numOfVentures = 2;
								}
								else if(customerType=="gold"){
									maxFund = 6;
									additionalFund = 4;
									numOfVentures = 3;
								}
								else if(customerType=="platinum"){
									maxFund = 8;
									additionalFund = 5;
									numOfVentures = 3;
								}
							//End ----------------------------------------
							line = br.readLine();		
						}
						else if(counter==2){
							discountFactor = Double.parseDouble(line);
							line = br.readLine();
							
							
						}
						else if(counter==3){
							numberOfFortnight = Integer.parseInt(line);
							line = br.readLine();
							
						}
						else if(counter==4){
							StringTokenizer st = new StringTokenizer(line," ");
							for(int i=0;i<numOfVentures;i++){
								int price = Integer.parseInt(st.nextToken());
								productPrice.add(price);
							}
							line = br.readLine();
							
						}
						else if(counter ==5){
							StringTokenizer st = new StringTokenizer(line," ");
							line = br.readLine();
							for(int i=0;i<numOfVentures;i++){
								int fund = Integer.parseInt(st.nextToken());
								fundAtTheBeginning.add(fund);
							}
							
						}
						else{
							if(matrixCounter == 0){
								matrix = new double[maxFund+1][maxFund+1];
							}
								
							System.out.println(line);
								StringTokenizer st = new StringTokenizer(line," ");
									for(int i=0;i<maxFund+1;i++){
										double probability = Double.parseDouble(st.nextToken());
										matrix[matrixCounter][i] = probability;
									
									}
									matrixCounter++;
									if(matrixCounter>maxFund){
										matrixCounter = 0;
										matrices.add(matrix);
									}
							
							line = br.readLine();
							
						}
					}

				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
				finally
				{
					try// if the file was opened, close it
					{
					if (fr != null)
					fr.close();
					}
					catch (IOException ioe)
					{
						System.out.println(ioe.getMessage());
					}

				}

			}
	}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try// if the file was opened, close it
			{
				if (fr != null)
				fr.close();
			}
			catch (IOException ioe)
			{
				System.out.println(ioe.getMessage());
			}

}
		
		System.out.println("File input info : ");
		System.out.println("Customer type : "+customerType);
		System.out.println("Discount factor : "+discountFactor);
		System.out.println("Number of fortnight tested : "+numberOfFortnight);
		System.out.println("Max reserve fund : "+maxFund);
		System.out.println("Max top up fund : "+additionalFund);
		System.out.println("Product price for venture 1 : "+productPrice.get(0)+" and venture 2 : "+productPrice.get(1));
		System.out.println("Fund for venture 1 : "+fundAtTheBeginning.get(0)+" and venture 2 : "+fundAtTheBeginning.get(1));
		System.out.println("Number of matrix : "+matrices.size());
}
}
