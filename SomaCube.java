import java.util.*;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Arrays;
import java.lang.Math;
import java.io.*;

public class SomaCube {

	public SomaCube() throws IOException {
		//Here we setup whatever we need
		//We numbered the cube positions from 0 to 26
	    cubeNumber = new int[26];       // CubeNumber DS for 27 positions in 3*3*3 Soma cube
	    //we number the pieces from 1 to 7
	    pieceNumber = new int[6];       // PieceNumber DS for 7 pieces
	    theCube = new BitSet(27);
	    pieceAvail = new BitSet(7);
	    
	    cubeCoordinate = new Hashtable();
	    cubeBinary = new Hashtable();
	    //fillHashTables();		//Fill hashtable
	    piece_OrientList = new List[7];
	    for (int i = 0; i < 7; i++)
	    	piece_OrientList[i] = new LinkedList();      // allocates memory for 10 linked lists
	    
	    //fill_AllOrienLists();					   //Here, we filled up the linked lists for different orientation
	    Solution = new BitSet[7];      // allocates memory for 7 BitSets
	    counter = new int[7];
	    //Debug!!!
	    for (int i = 0; i < 7; i++)
	    	counter[i] = 0;
	    numOrient = new int[7];
	    for (int i = 0; i < 7; i++)
	    	numOrient[i] = 0;
	    solCounter = 0;
	    file = new File("solutions.txt");
	    output = new BufferedWriter(new FileWriter(file));
	    
	    S = new int[28];		//Results

	    //super();
		// TODO Auto-generated constructor stub
	}
	//Declaration for 3*3*3 Soma cube
	public int[] cubeNumber;               // declares an array of integers for cubeNumber
	public int[] pieceNumber;              // declares an array of integers for PieceNumber
	
	//Note that cubeNumer k = k-1th bits in the Cube 
	public BitSet theCube;			//indices in the range 0 through 26
	public BitSet pieceAvail;		//indices in the range 0 through 6

	public Hashtable cubeCoordinate;		//A Hashtable which stores the coordinate-cubeNumber mapping
	public Hashtable cubeBinary;			//A HashTable representing binary coding of Cubes
	
	public List[] piece_OrientList;     		 // declares an array of linked lists
	
   	public BitSet[] Solution;              //declares an array of BitSets to store solutions
   	public int[] counter;					//For debugging !
   	public int[] numOrient;					//For debug!
   	public int solCounter;				   //number of non-isomorph solutions
	public Writer output = null;
    public File file;
    public int S[];							//results!
	
	private void fillHashTables(){
		//Setup the cubeNumber->Coordinates mapping for Soma!
		cubeCoordinate.put("1", new int[] {1, -1, -1});
		cubeCoordinate.put("2", new int[] {1,  0, -1});
		cubeCoordinate.put("3", new int[] {1,  1, -1});
		cubeCoordinate.put("4", new int[] {0, -1, -1});
		cubeCoordinate.put("5", new int[] {0,  0, -1});
		cubeCoordinate.put("6", new int[] {0,  1, -1});
		cubeCoordinate.put("7", new int[] {-1, -1, -1});
		cubeCoordinate.put("8", new int[] {-1,  0, -1});
		cubeCoordinate.put("9", new int[] {-1,  1, -1});
		
		cubeCoordinate.put("10", new int[] {1, -1, 0});
		cubeCoordinate.put("11", new int[] {1, 0, 0});
		cubeCoordinate.put("12", new int[] {1, 1, 0});
		cubeCoordinate.put("13", new int[] {0, -1, 0});
		cubeCoordinate.put("14", new int[] {0, 0, 0});
		cubeCoordinate.put("15", new int[] {0, 1, 0});
		cubeCoordinate.put("16", new int[] {-1, -1, 0});
		cubeCoordinate.put("17", new int[] {-1, 0, 0});
		cubeCoordinate.put("18", new int[] {-1, 1, 0});
		
		cubeCoordinate.put("19", new int[] {1, -1, 1});
		cubeCoordinate.put("20", new int[] {1, 0, 1});
		cubeCoordinate.put("21", new int[] {1, 1, 1});
		cubeCoordinate.put("22", new int[] {0, -1, 1});
		cubeCoordinate.put("23", new int[] {0, 0, 1});
		cubeCoordinate.put("24", new int[] {0, 1, 1});
		cubeCoordinate.put("25", new int[] {-1, -1, 1});
		cubeCoordinate.put("26", new int[] {-1, 0, 1});
		cubeCoordinate.put("27", new int[] {-1, 1, 1});
		
		//Setup Binary reprsesentation for Cubes
		BitSet cube;
		for(int i = 1; i <= 27; i++){
			cube = new BitSet(27);
			cube.set(i);
			cubeBinary.put(Integer.toString(i), cube);
		}
	}

	private void fillPieceOrientationList(int[][] pieceOrient, int[] howFar, List pieceOrientList ){
		//How to find all of the possiblities for a specific orientation
        int[] factors = {1, 1, 1};
        int[][] temp = new int[4][3];
        boolean Matched = true;
        BitSet bits = new BitSet(27);
        bits.clear();
        Object t = new Object();
        
        for(int i = 0; i <= 2; i++){
        	if(howFar[i] != 0)
        		factors[i] = howFar[i]/Math.abs(howFar[i]);	
        }
            
        for(int i = 0; factors[0]*i <= Math.abs(howFar[0]); i = i + factors[0] )
        	for(int j = 0; factors[1]*j <= Math.abs(howFar[1]); j = j + factors[1] )
        		for(int k = 0; factors[2]*k <= Math.abs(howFar[2]); k = k + factors[2] ){
        			for(int s = 0; s <= 3; s++){
        				temp[s][0] = i + pieceOrient[s][0];
        				temp[s][1] = j + pieceOrient[s][1];
        				temp[s][2] = k + pieceOrient[s][2];
        			}
        			bits = new BitSet(27);
        			bits.clear();
        			for(int z = 0; z <= 3; z++){
                		for (Enumeration e = cubeCoordinate.keys() ; e.hasMoreElements() && Matched;) {
                			t = e.nextElement();
                			if(Arrays.equals(temp[z], (int[])cubeCoordinate.get(t))){
                				bits.or((BitSet) cubeBinary.get(t));
                				//System.out.println("matched with: " + (BitSet) cubeBinary.get(t));
                				Matched = false;
                			}
                		}
                		Matched = true;
        			}
        			pieceOrientList.add(bits);
        			//System.out.println("Orientation: " + (BitSet) bits);	//Orientations
        		}
	}
    private void fill_AllOrienLists(){
		//List of non-isomorph orientation for Piece #1
		int[][][] Piece1 =
				{{{-1, -1, -1}, {0, -1, -1}, {1, -1, -1}, {1, 0, -1}}, {{-1, 1, -1}, {0, 1, -1}, {1, 1, -1}, {1, 0, -1}}};//one orientation of Piece #1
		//Corresponding freedom movement for every orientation of Piece #1
		int[][] howFar1 = {{0, 1, 0}, {0, -1, 0}};//{{0, 1, 2}};
		//Number of non-isomorph orientations for Piece #1
		int piece_1_Orient_num = 1;
		//Exhaust all of the possible orientation for Piece #1 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_1_Orient_num; i++)
			 fillPieceOrientationList(Piece1[i], howFar1[i], piece_OrientList[0]);
		
        //List of non-isomorph orientation for Piece #2
		int[][][] Piece2 = 
				{{{0, -1, -1}, {-1, -1, -1}, {-1, 0, -1}, {-1, -1, 0}}, {{-1, -1, -1}, {-1, 0, -1}, {0, 0, -1}, {-1, 0, 0}},
				 {{-1, 0, -1}, {0, 0, -1}, {0, -1, -1}, {0, 0, 0}}, {{-1, -1, -1}, {0, -1, -1}, {0, 0, -1}, {0, -1, 0}},
				 {{-1, -1, -1}, {0, -1, 0}, {-1, -1, 0}, {-1, 0, 0}}, {{-1, 0, -1}, {-1, -1, 0}, {-1, 0, 0}, {0, 0, 0}},
				 {{0, 0, -1}, {-1, 0, 0}, {0, 0, 0}, {0, -1, 0}}, {{0, -1, -1}, {-1, -1, 0}, {0, -1, 0}, {0, 0, 0}}};//one orientation of Piece #2
		//Corresponding freedom movement for every orientation of Piece #2
		int[][] howFar2 = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, 
						   {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
		//Number of non-isomorph orientations for Piece #2
		int piece_2_Orient_num = 8;
		//Exhaust all of the possible orientation for Piece #2 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_2_Orient_num; i++)
			 fillPieceOrientationList(Piece2[i], howFar2[i], piece_OrientList[1]);

        //List of non-isomorph orientation for Piece #3
		int[][][] Piece3 = 
						{{{0, -1, -1}, {-1, -1, -1}, {-1, 0, -1}, {-1, 0, 0}}, {{-1, -1, -1}, {-1, 0, -1}, {0, 0, -1}, {0, 0, 0}},
						 {{-1, 0, -1}, {0, 0, -1}, {0, -1, -1}, {0, -1, 0}}, {{-1, -1, -1}, {0, -1, -1}, {0, 0, -1}, {-1, -1, 0}},
						 {{-1, -1, 0}, {-1, 0, 0}, {-1, 0, -1}, {0, 0, -1}}, {{-1, 0, 0}, {0, 0, 0}, {0, 0, -1}, {0, -1, -1}},
						 {{-1, -1, -1}, {0, -1, -1}, {0, -1, 0}, {0, 0, 0}}, {{0, -1, 0}, {-1, -1, 0}, {-1, -1, -1}, {-1, 0, -1}},
						 {{-1, -1, -1}, {-1, -1, 0}, {-1, 0, 0}, {0, 0, 0}}, {{-1, 0, -1}, {-1, 0, 0}, {0, 0, 0}, {0, -1, 0}},
						 {{0, 0, -1}, {0, 0, 0}, {0, -1, 0}, {-1, -1, 0}}, {{0, -1, -1}, {0, -1, 0}, {-1, -1, 0}, {-1, 0, 0}}};//one orientation of Piece #3
		//Corresponding freedom movement for every orientation of Piece #3
		int[][] howFar3 =  {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1},
							{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1},
							{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
		//Number of non-isomorph orientations for Piece #3
		int piece_3_Orient_num = 12;
		//Exhaust all of the possible orientation for Piece #3 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_3_Orient_num; i++)
			 fillPieceOrientationList(Piece3[i], howFar3[i], piece_OrientList[2]);

        //List of non-isomorph orientation for Piece #4
		int[][][] Piece4 = 
						{{{-1, -1, -1}, {-1, 0, -1}, {0, 0, -1}, {-1, -1, 0}}, {{-1, 0, -1}, {0, 0, -1}, {0, -1, -1}, {-1, 0, 0}},
						{{-1, -1, -1}, {0, -1, -1}, {0, 0, -1}, {0, 0, 0}}, {{-1, -1, -1}, {-1, 0, -1}, {0, -1, -1}, {0, -1, 0}},
						{{-1, -1, -1}, {0, -1, -1}, {-1, -1, 0}, {-1, 0, 0}}, {{-1, -1, -1}, {-1, 0, -1}, {-1, 0, 0}, {0, 0, 0}},
						{{-1, 0, -1}, {0, 0, -1}, {0, 0, 0}, {0, -1, 0}}, {{-1, -1, 0}, {0, -1, 0}, {0, -1, -1}, {0, 0, -1}},
						{{-1, 0, -1}, {-1, 0, 0}, {-1, -1, 0}, {0, -1, 0}}, {{-1, -1, 0}, {-1, 0, 0}, {0, 0, 0}, {0, 0, -1}},
						{{-1, 0, 0}, {0, 0, 0}, {0, -1, 0}, {0, -1, -1}}, {{-1, -1, -1}, {-1, -1, 0}, {0, -1, 0}, {0, 0, 0}}};//one orientation of Piece #4
		//Corresponding freedom movement for every orientation of Piece #4
		int[][] howFar4 = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1},
						   {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1},
						   {1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
		//Number of non-isomorph orientations for Piece #4
		int piece_4_Orient_num = 12;
		//Exhaust all of the possible orientation for Piece #4 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_4_Orient_num; i++)
			 fillPieceOrientationList(Piece4[i], howFar4[i], piece_OrientList[3]);
        //List of non-isomorph orientation for Piece #5
		int[][][] Piece5 = 
						{{{0, -1, -1}, {0, 0, -1}, {-1, 0, -1}, {-1, 1, -1}}, {{-1, -1, -1}, {0, -1, -1}, {0, 0, -1}, {1, 0, -1}},
						{{-1, -1, -1}, {-1, 0, -1}, {-1, 0, 0}, {-1, 1, 0}}, {{-1, -1, -1}, {0, -1, -1}, {0, -1, 0}, {1, -1, 0}},
						{{-1, -1, 0}, {-1, 0, 0}, {-1, 0, -1}, {-1, 1, -1}}, {{-1, -1, 0}, {0, -1, 0}, {0, -1, -1}, {1, -1, -1}},
						{{-1, -1, -1}, {-1, -1, 0}, {-1, 0, 0}, {-1, 0, 1}}, {{-1, -1, -1}, {-1, -1, 0}, {0, -1, 0}, {0, -1, 1}},
						{{-1, 0, -1}, {-1, 0, 0}, {-1, -1, 0}, {-1, -1 ,1}}, {{0, -1, -1}, {0, -1, 0}, {-1, -1, 0}, {-1, -1, 1}},
						{{-1, -1, -1}, {-1, 0, -1}, {0, 0, -1}, {0, 1, -1}}, {{-1, 0, -1}, {0, 0, -1}, {0, -1, -1}, {1, -1, -1}}};//one orientation of Piece #5
		//Corresponding freedom movement for every orientation of Piece #5
		int[][] howFar5 = {{1, 0, 2}, {0, 1, 2},  
						   {2, 0, 1}, {0, 2, 1}, 
						   {2, 0, 1}, {0, 2, 1},
						   {2, 1, 0}, {1, 2, 0}, 
						   {2, 1, 0}, {1, 2, 0},
						   {1, 0,2}, {0, 1, 2}};
		//Number of non-isomorph orientations for Piece #5
		int piece_5_Orient_num = 12;
		//Exhaust all of the possible orientation for Piece #5 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_5_Orient_num; i++)
			 fillPieceOrientationList(Piece5[i], howFar5[i], piece_OrientList[4]);

		//List of non-isomorph orientation for Piece #6
		int[][][] Piece6 = 
		{{{0, 0, -1},  {1, 0, -1},  {1, 1, -1},  {1, -1, -1}}, {{-1, -1, -1}, {0, -1, -1}, {1, -1, -1}, {0, 0, -1}},
		 {{0, 1, -1},  {0, 0, -1},  {0, -1, -1}, {1, 0, -1}},  {{-1, 0, -1},  {0, 0, -1},  {1, 0, -1},  {0, -1, -1}},
		 {{1, -1, -1}, {1, 0, -1},  {1, 1, -1},  {1, 0, 0}},   {{-1, -1, -1}, {0, -1, -1}, {1, -1, -1}, {0, -1, 0}},
		 {{0, -1, -1}, {-1, -1, 0}, {0, -1, 0}, {1, -1, 0}}, {{-1, 0, -1}, {-1, -1, 0}, {-1, 0, 0}, {-1, 1, 0}},
		 {{-1, -1, -1}, {-1, -1, 0}, {-1, -1, 1}, {-1, 0, 0}}, {{-1, -1, -1}, {-1, -1, 0}, {-1, -1, 1}, {0, -1, 0}},
		 {{-1, 0, -1}, {-1, 0, 0}, {-1, 0, 1}, {-1, -1, 0}}, {{0, -1, -1}, {0, -1, 0}, {0, -1, 1}, {-1, -1, 0}}};//one orientation of Piece #6
		
		//Corresponding freedom movement for every orientation of Piece #6
		int[][] howFar6 = {{-1, 0, 2}, {0, 1, 2}, {-1, 0, 2}, {0, 1 ,2}, 
						   {-2, 0, 1}, {0, 2, 1}, {0, 2, 1}, {2, 0, 1}, 
						   {2, 1, 0}, {1, 2, 0}, {2, 1, 0}, {1, 2, 0}};
		//Number of non-isomorph orientations for Piece #6
		int piece_6_Orient_num = 12;
		//Exhaust all of the possible orientation for Piece #6 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_6_Orient_num; i++)
			 fillPieceOrientationList(Piece6[i], howFar6[i], piece_OrientList[5]);

        //List of non-isomorph orientation for Piece #7
		int[][][] Piece7 = 
						{{{-1, 0, -1}, {-1, -1, -1}, {0, -1, -1}, {0, -1, -1}}, {{-1, -1, -1}, {-1, 0, -1}, {0, 0, -1}, {0, 0, -1}},
						{{-1, 0, -1}, {0, 0, -1}, {0, -1, -1}, {0, -1, -1}}, {{-1, -1, -1}, {0, -1, -1}, {0, 0, -1}, {0, 0, -1}},
						{{-1, -1, -1}, {-1, 0, -1}, {-1, 0, 0}, {-1, 0, 0}}, {{-1, -1, -1}, {0, -1, -1}, {0, -1, 0}, {0, -1, 0}},
						{{-1, 0, -1}, {-1, -1, -1}, {-1, -1, 0}, {-1, -1, 0}}, {{-1, -1, -1}, {0, -1, -1}, {-1, -1, 0}, {-1, -1, 0}},
						{{-1, -1, 0}, {-1, 0, 0}, {-1, 0, -1}, {-1, 0, -1}}, {{-1, -1, 0}, {0, -1, 0}, {0, -1, -1}, {0, -1, -1}},
						{{-1, -1, -1}, {-1, -1, 0}, {-1, 0, 0}, {-1 ,0 ,0}}, {{-1, -1, -1}, {-1, -1, 0}, {0, -1, 0}, {0, -1, 0}}};//one orientation of Piece #7
		//Corresponding freedom movement for every orientation of Piece #7
		int[][] howFar7 = {{1, 1, 2}, {1, 1, 2}, {1, 1, 2}, {1, 1, 2},
						   {2, 1, 1}, {1, 2, 1}, {2, 1, 1}, {1, 2, 1},
						   {2, 1, 1}, {1, 2, 1}, {2, 1, 1}, {1, 2, 1}};
		//Number of non-isomorph orientations for Piece #7
		int piece_7_Orient_num = 12;
		//Exhaust all of the possible orientation for Piece #7 and store them into 
		//the corresponding LinkedList in a binary format!
		for(int i = 0; i < piece_7_Orient_num; i++)
			 fillPieceOrientationList(Piece7[i], howFar7[i], piece_OrientList[6]);   
    }

    private void backTrack(int k) throws IOException{
    	BitSet tempCube = new BitSet(27);
    	BitSet tempPiece = new BitSet(27);
    	int f = 0;
 
    	tempCube.clear();
    	tempPiece.clear();
   		Iterator l = piece_OrientList[k].iterator();
        
   		while (l.hasNext()){
            	tempPiece = (BitSet) l.next();
            	Solution[k] = tempPiece;
            	tempCube = (BitSet) theCube.clone();	//temp stores the state of the space
            	tempCube.and(tempPiece);
            	if (tempCube.isEmpty()){
            	//We can place this piece
            		theCube.or(tempPiece);		//The new_space = piece U current_space
            		if (k == 6){
            			//Here is a solution, let's print it
            			solCounter++;		//number of non-isomorph solutions           			
            			String Sol = new String();
            			Sol += "--------------------------------------------------\n";
            			Sol += "Solution #"+ solCounter +" :\n";
            			
            	    	for(int z = 0; z < 7; z++){
            	        	for(int y = Solution[z].nextSetBit(0); y>=0; y=Solution[z].nextSetBit(y+1)) {
            	        			S[y] = z+1;		//piece # j
            	        	}
            	    	}
            	    	
        	        	for (int u = 0; u < 3; u++){
        	        		switch (u) {
        	                case 0:  Sol += "----Bottom Surface----\n"; break;
        	                case 1:  Sol += "----Middle Surface----\n"; break;
        	                default: Sol += "----Top Surface----\n";break;
        	        		}

        	        		Sol += "|--" + S[7 + 9*u] + "--|--" + S[8 + 9*u] + "--|--"+S[9 + 9*u]+ "--|\n";
        	         		Sol += "|--" + S[4 + 9*u] + "--|--" + S[5 + 9*u] + "--|--"+S[6 + 9*u]+ "--|\n";
        	        		Sol += "|--" + S[1 + 9*u] + "--|--" + S[2 + 9*u] + "--|--"+S[3 + 9*u]+ "--|\n";
        	        	}

//        	        	Sol += "------------------------------------------------\n";
        	        	Sol += "\n";
        	        	Sol += "Piece# x: {Occupied cubes by each piece!}\n";
            			for(int i = 0; i <= 6; i++){
            				f = i + 1;
            				Sol += "Piece# " + f +": " + Solution[i] + ",\n";
            			}	
//            			Sol += "\n";
            			output.write(Sol);
             		}
            		else{
            			backTrack(k + 1);
//            			counter[k+1]++;			//we recursively called the function once more
            		}
            		theCube.andNot(tempPiece);		//exclude the place piece           		
            	}//end if 
            }//we have exhausted all of the possible solutions for piece #k
    }
    
    public void finish() throws IOException{
    	output.close();
    }
    
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SomaCube a = new SomaCube();
		a.fillHashTables();
		a.fill_AllOrienLists();
		a.backTrack(0);
		System.out.println(a.solCounter);
		a.finish();    
	}
}
