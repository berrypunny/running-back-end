import java.util.Scanner;
import java.io.*;

public class DataParser { 
	public static final int WEEK = 7; //not sure if this is constant
	public static final int HEAURISTIC = 5; 
	/** 
	 *  returns true if week is complete, false otherwise
     *
	 */ 
	public boolean weekComplete(){
		int counter = 0; //counter variable checks how many lines 
		File file = new File("progress.txt");

    	try {

        	Scanner sc = new Scanner(file);

        	while (sc.hasNextLine()) {
            	string i = sc.nextLine();
            	System.out.println(i); //just to count
            	counter++; 
        	}
        	sc.close();
    	} 
    	catch (FileNotFoundException e) {
       	 	e.printStackTrace();
    	}

    	if(counter == WEEK){
    		return true; 
    	}
    	else{
    		return false; 
    	}
	}
	/*
	 *returns weekly progress bar from Progress.txt or is it .json? 
	 */
	public Day[] getProgress(){
		Day[] dayArray = new Day[WEEK]; 
		int index = 0; 
		File file = new File("progress.txt"); 
		try {

        	Scanner sc = new Scanner(file);

        	while (sc.hasNextLine()) {
            	string i = sc.nextLine();
            	Scanner finder = new Scanner(i);
            	dayArray[index].length = finder.nextDouble();
            	dayArray[index].time = finder.nextInt(); 
            	dayArray[index].heartrate = finder.nextInt();  
            	index++; 
        	}
        	sc.close();
    	} 
    	//is this where i parse the json 
    	catch (FileNotFoundException e) {
       	 	e.printStackTrace();
    	}

    	return dayArray; 
	}
	/*
	 * writes day info into text file
	 */ 
	public void writeWeek(Day[] d){
		File outFile = new File("progress.txt")
		FileWriter fWriter = new FileWriter(outFile); 
		PrintWriter pWriter = new PrintWriter(fWriter); 
		for(int i = 0; i < d.length; i++){
			pWriter.println(d[i].toString()); 	//will d have a toString method
		}
		pWriter.close(); 
	}
	/*
	 * writes in new heuristic info to txt file 
	 */ 
	public void writeHeuristicVal(double[] d){
		File outFile = new File("heuristic.txt")
		FileWriter fWriter = new FileWriter(outFile); 
		PrintWriter pWriter = new PrintWriter(fWriter); 
		for(int i = 0; i < d.length; i++){
			pWriter.println(d[i].toString()); 	
		}
		pWriter.close(); 
	}
	/*
	 * writes in new heurestic info, function type for EV curve
	 */
	public void writeHeuristicVal(String a){
		File outFile = new File("heuristic.txt")
		FileWriter fWriter = new FileWriter(outFile); 
		PrintWriter pWriter = new PrintWriter(fWriter); 
		
		pWriter.println(a); 	
		
		pWriter.close(); 
	}
	/*
	 * gets the heurestic info from json or text?  
	 */ 
	public double[] getHeuresticVal(){
		double[] hArray = new double[HEAURISTIC]; 
		int index = 0; 
		File file = new File("progress.txt"); 
		try {

        	Scanner sc = new Scanner(file);

        	while (sc.hasNextLine()) {
            	double i = sc.nextDouble();
            	hArray[index] = i; //can be easily changed once we establish day class
            	//everything probably increments of 2 digits? 
            	index++; 
        	}
        	sc.close();
    	} 
    	catch (FileNotFoundException e) {
       	 	e.printStackTrace();
    	}
    	return dayArray; 
	} 
	/*
	 * get heurestic string for function type
	 */ 
	public String getHeuresticString(){

	}



}