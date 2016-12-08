import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Survey {
	//Components
	protected List <Question> questions = new ArrayList <Question> () ;
	protected String filename = "" ;
	protected String type = "s" ;
	protected Response responses ;
	
	//Methods
	//Add Question
	public void addQ (String selection, String prompt)
	{
		//Query for question components
		if (selection.equals("mc"))
		{
			List <String> choices = requestChoices() ;
			int expentry = requestEntryNum() ;
			questions.add(new MultipleChoice(choices, expentry, prompt)) ;
		}
		else if (selection.equals("tf"))
		{
			List <String> choices = new ArrayList <String> () ;
			choices.add("true") ;
			choices.add("false") ;
			questions.add(new TF(choices,1, prompt)) ;
		}
		else if (selection.equals("sa"))
		{
			int charlimit = requestCharLimit() ;
			int expentry = requestEntryNum() ;
			questions.add(new Freeform(charlimit,prompt,expentry)) ;
		}
		else if (selection.equals("e"))
		{
			int expentry = requestEntryNum() ;
			questions.add(new Freeform(0,prompt,expentry)) ;
		}
		else if (selection.equals("r"))
		{
			List <String> choices = requestChoices() ;
			questions.add(new Ranking(choices, choices.size(),prompt)) ;
		}
		else if (selection.equals("m"))
		{
			List <String> col1 = new ArrayList <String> () ;
			List <String> col2 = new ArrayList <String> () ;
			do
			{
				out("Column 1:") ;
				col1 = requestChoices() ;
				out("Column 2:") ;
				col2 = requestChoices() ;
				if (col1.size() != col2.size())
					out("Columns must be the same length") ;
			}while ((col1.size() != col2.size()) || (col1.size() == 0)) ;
			questions.add(new Matching(col1,col2,prompt)) ;
		}
	}

	public void addQ (Question q)
	{
		questions.add(q) ;
	}
	
	public int requestEntryNum ()
	{
		int expentry = 0 ;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		out("How many user answers are required for this question?") ;
		while (expentry < 1)
		{
			try {
				expentry = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
		}
		return expentry ;
	}
	
	public int requestCharLimit ()
	{
		int charlimit = 0 ;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		out("What is the character limit for this question's answer?") ;
		while (charlimit < 1)
		{
			try {
				charlimit = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
		}
		return charlimit ;
	}
	
	public List <String> requestChoices ()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List <String> choices = new ArrayList <String> () ;
		out("Please add your choices, or a \"q\" to stop") ;
		String choice = "" ;
		int count = 1 ;
		while (!(choice.equals("q")))
		{
			try {
				System.out.print(count+". ");
				choice = br.readLine() ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			}
			if (!(choice.equals("q")))
				choices.add(choice) ;
			count++ ;
		}
		return choices ;
	}
	
	//Set filename (for loaded Surveys)
	public void setFN (String fn)
	{
		this.filename = fn ;
	}
	
	//Display
	public void display()
	{
		out("Survey\n") ;
		for (int i = 0; i < questions.size(); i++)
		{
			questions.get(i).display() ;
			out("") ;
		}
	}
	
	public void displayFull(){}
	
	//Modify
	public void modify()
	{
		//Edit current questions
		int quit = 0 ;
		while(quit !=2)
		{
			//Selection
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			out("Select the number of the question to modify") ;
			for(int i = 0; i < questions.size(); i++)
			{
				out(i+1 + ". " + questions.get(i).prompt) ;
			}
			int select = 0 ;
			while (select < 1 || select > questions.size())
			{
				try {
					select = Integer.parseInt(br.readLine()) ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				} catch(NumberFormatException nfe){
		            out ("Input not a number") ;
		        }
				if (select < 1 || select > questions.size())
					out("Not a valid choice") ;
			}
			
			//Modification
			questionDisp(questions.get(select-1)) ;
			questions.get(select-1).modify() ;
			
			//Continue
			quit = 0 ;
			out("1. Continue modifying") ;
			out("2. Finish modifying") ;
			while (quit < 1 || quit > 2)
			{
				try {
					quit = Integer.parseInt(br.readLine()) ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				} catch(NumberFormatException nfe){
		            out ("Input not a number") ;
		        }
				if (quit < 1 || quit > 2)
					out("Select 1 or 2") ;
			}
		}
	}
	
	public void questionDisp(Question q)
	{
		q.display() ;
	}
	
	//Take
	public void take()
	{
		List <List <String> > answers = new ArrayList <List <String> > () ;
		for (int i = 0; i < questions.size(); i++)
		{
			//Take question
			questions.get(i).display();
			answers.add(questions.get(i).requestAnswer()) ;
			out("") ;
		}
		responses = new Response(answers) ;
		//Save
		if (!saveR())
			out("Saving responses failed") ;
	}
	
	//Tabulate
	public void tabulate()
	{
		List <Response> resps = new ArrayList <Response> () ;
		String toks [] = this.filename.split("\\.") ;
		String name = toks[0] + "resp" ; //subfolder
		File dir = new File(name) ;
		String files [] = dir.list() ;
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].contains("resp"))
				resps.add(loadR(name+File.separator+files[i])) ;
		}
		//Tabulate
		for (int i = 0; i < questions.size(); i++)
		{
			questions.get(i).display();
			out("");
			questions.get(i).tabulate(i, resps); 
			out("\n");
		}
	}
	
	public Response loadR(String fn)
	{
		File inFile = new File (fn);
		String filecontents = "" ;
	    Scanner sc;
		try {
			sc = new Scanner (inFile);
			while (sc.hasNextLine())
		    {
		      String line = sc.nextLine();
		      filecontents = filecontents+line ;
		    }
		    sc.close();
		} catch (FileNotFoundException e) {
			out("File did not exist") ;
		}
		
		JsonParser obj = new JsonParser();
    	JsonElement root = obj.parse(filecontents);
    	JsonObject rootobj = root.getAsJsonObject();
    	
    	//Parse responses
    	List <List <String>> strresp = new ArrayList <List <String>> () ;
    	
    	JsonArray resps = rootobj.get("responses").getAsJsonArray() ; 
    	for (int i = 0; i < resps.size(); i++)
    	{
    		strresp.add(new ArrayList <String> ()) ;
    		JsonArray tempj = resps.get(i).getAsJsonArray() ;
    		for (int j = 0; j < tempj.size(); j++)
    		{
    			strresp.get(i).add(tempj.get(j).getAsString()) ;
    		}   		
    	}
    	//Parse grade
    	double grade = rootobj.get("grade").getAsDouble() ; 	
    	Response resp = new Response(strresp) ;
    	resp.setGrade(grade);
		return resp ;
	}
	
	public static void out(String s)
	{
		System.out.println(s);
	}

	public boolean saveR()
	{
		Gson gson = new Gson() ;
		String responsesJSON = gson.toJson(this.responses) ;
		
		String toks [] = this.filename.split("\\.") ;
		String name = toks[0] + "resp" ; //subfolder
		File dir = new File(name) ;
		if(!dir.exists()) //create if not there
			dir.mkdirs() ;
		try {
			int count = 0 ;
			String files [] = dir.list() ;
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].contains("resp"))
					count++ ;
			}
			name = name+"/resp"+String.valueOf(count+1)+".json" ; //filename & numbering & extension
			File outFile = new File (name);
		    FileWriter fWriter = new FileWriter (outFile);
		    PrintWriter pWriter = new PrintWriter (fWriter);
		    pWriter.println(responsesJSON);
		    fWriter.close();
		    pWriter.close();
		    return true ;
		} catch (FileNotFoundException e) {
			out("Unable to open provided filename for writing") ;
			return false ;
		} catch (IOException e) {
			out("IO Error when writing to filename") ;
			return false ;
		} 
	}
}
