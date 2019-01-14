/* @author Anshika Trivedi
 * 
 * @description CS 542 PROJECT FALL 2016 - Implement Dijkstra's Algorithm
 * 
 * SEAT NUMBER: 86
 * CWID: A20387275 
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MyDijkstra {

	private int numberOfRouters;

	public MyDijkstra()
	{
		// empty constructor to initialize object of class MyDijkstra
	}

	public MyDijkstra(int numberOfRouters )
	{
		this.numberOfRouters = numberOfRouters;		// parameterized constructor to set number of routers
	}

	public static void main(String[] arg) throws IOException
	{
		int source = 0, destination = 0, check = 0, fileReadStatus = 0;

		int category;
		Scanner userInput = new Scanner(System.in);
		MyDijkstra myAlgo1 = new MyDijkstra();


		System.out.println("*** Link-State Routing Protocol ***");
		int[][] adjacency_matrix = new int[0][0];
		int[][] newMatrixTopology = new int[6][6];
		int[] distance = new int[5];

		while(true){								// User-Menu for different operations


			System.out.println("\n1. Create Network Topology\n2. Build Connection Table\n3. Shortest Path to Destination Router\n4. Modify Topology By Deleting Router\n5. Modify Topology when router is Down\n6. Best Router for Broadcast\n7. Exit");
			System.out.println("\nPlease Enter your Choice !!");

			category = userInput.nextInt();

			switch(category)
			{
			case 1:
				//Case 1: Accept and read the network topology in a 2-d matrix.
				System.out.print("\n\nInput original network topology matrix data file:\n");
				String filename = userInput.next();
				System.out.println("\nFilename is : " + filename);

				System.out.println("\n\t\tNetwork Topology\n");

				adjacency_matrix = myAlgo1.readFile(filename);



				if(adjacency_matrix != null){

					fileReadStatus = 1;	
				}
				// if correct file exists set flag

				break;
			case 2:
				//Case 2: Select source router and display connection table with Interfaces.
				if(fileReadStatus == 1){
					System.out.print("\n\nSelect the Source Router (0 - "+ (myAlgo1.numberOfRouters-1)+")");
					source = userInput.nextInt();
					System.out.println("Source Router : " + source);

					check = 1;

					System.out.println("Connection Table for Source Router: " + source);
					System.out.println("\t\t Destination -> Immediate Interface");

					distance = myAlgo1.computeDijkstra(adjacency_matrix, source, 0, check);
				}
				else {
					System.out.println("File not found. Please select Choice 1 and Enter the correct filename !!");
				}


				break;
			case 3:
				//Case 3: Select destination router and display optimal path with minimum distance.
				if(fileReadStatus == 1){
					System.out.print("\n\nSelect the Destination Router (0 - "+(myAlgo1.numberOfRouters-1)+")");
					destination = userInput.nextInt();
					System.out.println("Destination Router : " + destination);

					check = 2;					

					distance = myAlgo1.computeDijkstra(adjacency_matrix, source, destination, check);
				}
				else {
					System.out.println("File not found. Please select Choice 1 and Enter the correct filename !!");
				}



				break;
			case 4:
				//Case: 4 Select the router to be deleted. Display new topology matrix.
				if(fileReadStatus == 1){
					System.out.print("\n\nEnter the router to be deleted (0 - "+(myAlgo1.numberOfRouters-1)+")");
					int deleteRouter = userInput.nextInt();

					int newNumberOfRouters = myAlgo1.numberOfRouters-1;		

					newMatrixTopology = myAlgo1.deleteRouter(adjacency_matrix, deleteRouter, myAlgo1.numberOfRouters);

					System.out.println("\nNew Topology After Deleting the Router :"+ deleteRouter);
					for(int k =0 ;k< newNumberOfRouters;k++) {
						for (int l =0 ;l< newNumberOfRouters;l++) {
							System.out.print(newMatrixTopology[k][l]);
							System.out.print("\t");
						}
						System.out.println();
					}					
				}
				else {
					System.out.println("File not found. Please select Choice 1 and Enter the correct filename !!");
				}




				break;
			case 5:
				//Case 5: Select a router and disable inactive links and display new topology and perform dijkstra's algorithm
				if(fileReadStatus == 1){
					System.out.print("\n\nEnter the router which is Down (0 - "+(myAlgo1.numberOfRouters-1)+")");
					int downRouter = userInput.nextInt();

					newMatrixTopology = myAlgo1.inactiveRouter(adjacency_matrix, downRouter, myAlgo1.numberOfRouters);

					System.out.println("\nNew Topology After disabling a Router");
					for(int k =0 ;k< myAlgo1.numberOfRouters;k++) {
						for (int l =0 ;l< myAlgo1.numberOfRouters;l++) {
							System.out.print(newMatrixTopology[k][l]);
							System.out.print("\t");
						}
						System.out.println();
					}	

					for ( int i = 0; i < myAlgo1.numberOfRouters; i++)
					{
						for ( int j = 0; j < myAlgo1.numberOfRouters; j++)
						{					
							if (i == j)
							{
								newMatrixTopology[i][j] = 0;
								continue;
							}
							if (newMatrixTopology[i][j] == -1)
							{
								newMatrixTopology[i][j] = 0;
							}
						}
					}

					System.out.print("\n\nSelect the new Source Router (0 - "+(myAlgo1.numberOfRouters-1)+")");
					source = userInput.nextInt();

					while(source == downRouter){
						System.out.println("Router " + downRouter + " is down. Please select another source router!! ");
						source = userInput.nextInt();
					}

					System.out.print("\n\nSelect the Destination Router : (0 - "+(myAlgo1.numberOfRouters-1)+")");
					destination = userInput.nextInt();

					while(destination == downRouter){
						System.out.println("Router " + downRouter + " is down. Please select another destination router!! ");
						destination = userInput.nextInt();
					}

					System.out.println("New Source Router : " + source);
					System.out.println("New Destination Router : " + destination);

					check = 2;

					distance = myAlgo1.computeDijkstra(newMatrixTopology, source, destination, check);

				}
				else {
					System.out.println("File not found. Please select Choice 1 and Enter the correct filename !!");
				}


				break;
			case 6:
				//Case 6: Compute the BEST Router and it minimum cost in the entire topology.
				if(fileReadStatus == 1){
					System.out.println("\n\nBest Router in the input Topology");

					check = 5;			


					ArrayList<int[]> storeDistance = new ArrayList<int[]>(); 
					int[] currentVertexDistance = new int[myAlgo1.numberOfRouters];
					int[] summation = new int[myAlgo1.numberOfRouters];
					int summationIndex = 0;

					// to store minimum distance array for each router with respect to other routers in Arraylist
					for(int i=0; i< myAlgo1.numberOfRouters; i++){
						System.out.println("Source is :" + i);
						distance = myAlgo1.computeDijkstra(adjacency_matrix, i, destination, check);
						storeDistance.add(distance);
					}

					//calculating sum of minimum distances of each router and storing in array
					for(int j=0;j<storeDistance.size();j++){
						currentVertexDistance = storeDistance.get(j);
						int sum=0;
						for(int k=0; k<myAlgo1.numberOfRouters;k++){							

							sum = sum + currentVertexDistance[k];
						}

						summation[summationIndex] = sum;		
						System.out.println("Minimum Cost is : " + summation[summationIndex] + " for the Router " + summationIndex);
						summationIndex++;
					}

					//to find the minimum cost out of all summations and declare the best router
					int min = summation[0];
					int index =0;
					for (int i = 0; i < myAlgo1.numberOfRouters; i++) {
						if (summation[i] < min) {
							min = summation[i];
							index = i;
						}
					}
					System.out.println("\n\nBest Router for Broadcast is " + index + " with shortest distance " + min);
				}
				else {
					System.out.println("File not found. Please select Choice 1 and Enter the correct filename !!");
				}


				break;
			case 7:
				//Case 7: Terminate the program
				System.out.println("\n\nExit CS542-04 2016 Fall project. Good Bye!");
				System.exit(0);
				break;


			}
		}
	}

	private int[][] readFile(String filename) throws IOException {
		// TODO Auto-generated method stub
		int i,j;
		int[][] inputMatrix = new int[100][100];
		String[] st;
		BufferedReader in = null;


		try {
			in = new BufferedReader(new FileReader("input/"+filename));	//reading files in specified directory
			//read the input file and store it in 2-d matrix
			st = in.readLine().trim().split(" ");
			int y = 1;
			numberOfRouters = st.length;

			for ( i = 0;i < numberOfRouters; i++) {

				int x = 0;
				for ( j = 0;j < numberOfRouters; j++) {
					inputMatrix[i][j] = Integer.parseInt(st[x]);
					x++;					
				}
				y++;
				if(y <= numberOfRouters){
					st = in.readLine().trim().split(" ");
				}

			}
			
			//displaying the input topology
			for(int k =0 ;k< numberOfRouters;k++) {
				for (int l =0 ;l< numberOfRouters;l++) {
					System.out.print(inputMatrix[k][l]);
					System.out.print("\t");
				}
				System.out.println();
			}

			for ( i = 0; i < numberOfRouters; i++) {
				for ( j = 0; j < numberOfRouters; j++) {					
					if (i == j)
					{
						inputMatrix[i][j] = 0;
						continue;
					}
					if (inputMatrix[i][j] == -1)
					{
						inputMatrix[i][j] = 0;
					}
				}
			}
		}catch (FileNotFoundException e){
			System.out.println("File " + filename + " not found. Please select Choice 1 and Enter the correct filename !!");
			return null;
		}  
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.close();

		System.out.println("\nRouters ( 0 to "+ (numberOfRouters-1)+")");
		return inputMatrix;
	}
	
	//function to implement Dijkstr's Algorithm
	private int[] computeDijkstra(int[][] adjacency_matrix, int source, int destination, int check) {
		// TODO Auto-generated method stub
		
		// The output array. distance[i] will hold the shortest distance from src to all routers
		int distance[] = new int[numberOfRouters]; 

		// nodeVisited[i] will true if vertex i is included in shortest path tree or shortest distance from src to i is finalized
		Boolean nodeVisited[] = new Boolean[numberOfRouters];
		int[] precede = new int[20];

		// Initialize all distances as INFINITE and nodeVisited[] as false
		for (int i = 0; i < numberOfRouters; i++)
		{

			distance[i] = Integer.MAX_VALUE;
			precede[i] = Integer.MAX_VALUE;
			nodeVisited[i] = false;
		}

		// Distance of source vertex from itself is always 0
		distance[source] = 0;

		// Find shortest path for all vertices
		for (int count = 0; count < numberOfRouters-1; count++)
		{
			// Pick the minimum distance vertex from the set of vertices not yet processed. u is always equal to source in first iteration.
			int u = minDistance(distance, nodeVisited);

			// Mark the vertex as visited
			nodeVisited[u] = true;

			// Update the distance costs of the next vertices of the current vertex.
			for (int v = 0; v < numberOfRouters; v++)

				// Update distance[v] only if is not in nodeVisited, there is an edge from u to v, and total weight of path from src to
				// v through u is smaller than current value of distance[v]
				if (!nodeVisited[v] && adjacency_matrix[u][v]!=0 && distance[u] != Integer.MAX_VALUE && distance[u]+adjacency_matrix[u][v] < distance[v]){

					precede[v] = u;
					distance[v] = distance[u] + adjacency_matrix[u][v];
				}
		}

		//function to print immediate interfaces
		if(check ==1){

			for(int i=0;i<numberOfRouters;i++){
				if(i == source)
					System.out.println("\t\tHop to go "+source+"\t-> -");
				else
					printHops(source, i, check,distance, adjacency_matrix, precede);
			}

		}
		//function to print paths and their costs
		if(check == 2){
			printHops(source, destination, check,distance, adjacency_matrix, precede);

		}
		//function to print best router in topology.
		if(check == 5){
			printShortestPath(distance, numberOfRouters);
			return distance;
		}

		return null;

	}
	
	//function to print minimum costs with respect to other routers
	private int printShortestPath(int[] dist, int V) {
		// TODO Auto-generated method stub
		int sum = 0;

		System.out.println("Destination Distance from Source");
		for (int i = 0; i < V; i++){
			sum = sum + dist[i];
			System.out.println(i+" \t\t "+dist[i]);
		}

		return sum;
	}

	//function to compute immediate hops and calculate minimum cost for source and destination along with the optimal path
	private void printHops(int s, int d, int check, int[] distance, int[][] adjacency_matrix, int[] precede) {
		// TODO Auto-generated method stub

		int[] path = new int[5];

		int j;
		int finall = 0;
		int i=d;
		path[finall]=d;
		finall++;


		while(precede[i]!=s)
		{
			j=precede[i];
			i=j;
			path[finall]=i;
			finall++;
		}

		path[finall]=s;


		if(check == 1){
			System.out.println("\t\tHop to go "+d+"\t-> "+path[finall-1]);
		}
		
		//to print shortest path with minimum cost.
		if(check == 2)
		{
			System.out.println("\nShortest Path:\n\n");

			for(i=finall;i>0;i--)
				System.out.print(path[i] + " -> ");

			System.out.print(d);
			System.out.println("\nFor total cost = "+distance[d]);
		}

	}
	
	//function to calculate minimum distance between two adjacent vertex
	private int minDistance(int[] distance, Boolean[] nodeVisited) {
		// TODO Auto-generated method stub
		// Initialize min value
		int min = Integer.MAX_VALUE, min_index=-1;

		for (int v = 0; v < numberOfRouters; v++){
			if (nodeVisited[v] == false && distance[v] <= min)
			{
				min = distance[v];
				min_index = v;
			}
		}
		return min_index;
	}
	
	//function to delete router
	private int[][] deleteRouter(int[][] adjacency_matrix, int deleteRouter, int numberOfRouters) {
		// TODO Auto-generated method stub

		int updatedTopology[][] = new int[numberOfRouters-1][numberOfRouters-1];

		int p = 0;
		for( int i = 0; i < numberOfRouters; ++i)
		{
			if ( i == deleteRouter)
				continue;
			int q = 0;
			for( int j = 0; j < numberOfRouters ; ++j)
			{
				if ( j == deleteRouter)
					continue;
				updatedTopology[p][q] = adjacency_matrix[i][j];
				++q;
			}

			++p;
		}

		int newNumberOfRouters = numberOfRouters-1;
		for ( int i = 0; i < newNumberOfRouters; i++)
		{
			for ( int j = 0; j < newNumberOfRouters; j++)
			{					
				if (i == j)
				{
					updatedTopology[i][j] = 0;
					continue;
				}
				if (updatedTopology[i][j] == 0)
				{
					updatedTopology[i][j] = -1;
				}
			}
		}
		return updatedTopology;
	}
	
	//function for deactivating routers
	private int[][] inactiveRouter(int[][] adjacency_matrix, int downRouter, int numberOfRouters2) {
		// TODO Auto-generated method stub
		int updatedTopology[][] = new int[numberOfRouters][numberOfRouters];

		for(int i = 0; i < numberOfRouters; i++){
			for( int j = 0; j < numberOfRouters ; ++j){
				updatedTopology[i][j] = adjacency_matrix[i][j];
			}
		}

		for( int i = 0; i < numberOfRouters; i++)
		{
			if ( updatedTopology[i][downRouter] > 0){
				updatedTopology[i][downRouter] = -1;
			}           
		}

		for( int j = 0; j < numberOfRouters; j++)
		{
			if ( updatedTopology[downRouter][j] > 0){
				updatedTopology[downRouter][j] = -1;
			}           
		}

		for ( int i = 0; i < numberOfRouters; i++)
		{
			for ( int j = 0; j < numberOfRouters; j++)
			{					
				if (i == j)
				{
					updatedTopology[i][j] = 0;
					continue;
				}
				if (updatedTopology[i][j] == 0)
				{
					updatedTopology[i][j] = -1;
				}
			}
		}

		return updatedTopology;
	}
}
