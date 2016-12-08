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

public class Evaluation {
	//create
	public Survey createEval (SFac fac)
	{
		Survey s = fac.makeEval() ;
		//Creation behavior (query user for input, etc.)
		s = addQ(s) ;
		return s ;
	}
	
	//fill out evaluation
	public Survey addQ (Survey surv)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int selection = 0 ;
		while (selection != 2)
		{
			questiondisplay() ;
			selection =  0 ;
			//Input process, get adtl info, add to s
			while (selection < 1 || selection > 6){
				try {
					selection = Integer.parseInt(br.readLine()) ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				} catch(NumberFormatException nfe){
		            out ("Input not a number") ;
		        }
				if (selection < 1 || selection > 6)
					out ("Not a valid selection");
			}
			
			String prompt = "" ;
			while (prompt.equals(""))
			{
				out("Please provide a prompt") ;
				try {
					prompt = br.readLine() ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				}
			}
			
			String type = "" ;
			switch(selection)
			{
			case 1: //TF
				type = "tf" ;
				break; 
			case 2: //MC
				type = "mc" ;
				break;
			case 3: //SA
				type = "sa" ;
				break;
			case 4: //Essay
				type = "e" ;
				break ;
			case 5: //Rank
				type = "r" ;
				break ;
			case 6: //Match
				type = "m" ;
				break ;
			}
			surv.addQ(type, prompt);
			//Restart
			out("1. Add another question") ;
			out("2. Finished adding questions") ;
			selection = 0 ;
			while (selection < 1 || selection > 2){
				try {
					selection = Integer.parseInt(br.readLine()) ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				} catch(NumberFormatException nfe){
		            out ("Input not a number") ;
		        }
				if (selection < 1 || selection > 2)
					out ("Not a valid selection");
			}
		}
		return surv ;
	}
	
	//load
 	public Survey loadEvalS (SFac fac, final String name)
	{
		//Loading behavior (open, read file, document)
		File inFile = new File (name);
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
			return null ;
		}
		
		JsonParser obj = new JsonParser();
    	JsonElement root = obj.parse(filecontents);
    	JsonObject rootobj = root.getAsJsonObject(); // may be Json Array if it's an array, or other type if a primitive
		//Composition behavior (create objects, etc.)
		Survey s = fac.makeEval() ;
		if (!rootobj.get("type").getAsString().equals("s"))
		{
			out(rootobj.get("type").getAsString()) ;
			out("This was not a Survey file") ;
			return null ;
		}
		//filename
		s.setFN(rootobj.get("filename").getAsString());

		//Questions
		JsonArray questions = rootobj.get("questions").getAsJsonArray() ;
		for (int i = 0; i < questions.size(); i++)
		{
			JsonObject question = questions.get(i).getAsJsonObject() ;
			if (question.get("type").getAsString().equals("e") || question.get("type").getAsString().equals("sa"))
			{
				extractFF(fac,question,s) ;
			}
			else if (question.get("type").getAsString().equals("mc"))
			{
				extractMC(fac,question,s) ;
			}
			else if (question.get("type").getAsString().equals("tf"))
			{
				extractTF(fac,question,s) ;
			}
			else if (question.get("type").getAsString().equals("r"))
			{
				extractR(fac,question,s) ;
			}
			else if (question.get("type").getAsString().equals("m"))
			{
				extractM(fac,question,s) ;
			}
		}
		
		return s ;
	}
	
	public Test loadEvalT (TFac fac, final String name)
	{
		//Loading behavior (open, read file, document)
		File inFile = new File (name);
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
			return null ;
		}
		
		JsonParser obj = new JsonParser();
    	JsonElement root = obj.parse(filecontents);
    	JsonObject rootobj = root.getAsJsonObject(); // may be Json Array if it's an array, or other type if a primitive
		//Composition behavior (create objects, etc.)
		Test t = fac.makeEval() ;
		if (!rootobj.get("type").getAsString().equals("t"))
		{
			out("This was not a Test file") ;
			return null ;
		}
		//filename
		t.setFN(rootobj.get("filename").getAsString());
		
		//Questions
		JsonArray questions = rootobj.get("questions").getAsJsonArray() ;
		for (int i = 0; i < questions.size(); i++)
		{
			JsonObject question = questions.get(i).getAsJsonObject() ;
			if (question.get("type").getAsString().equals("e") || question.get("type").getAsString().equals("sa"))
			{
				extractFFT(fac,question,t) ;
			}
			else if (question.get("type").getAsString().equals("mc"))
			{
				extractMCT(fac,question,t) ;
			}
			else if (question.get("type").getAsString().equals("tf"))
			{
				extractTFT(fac,question,t) ;
			}
			else if (question.get("type").getAsString().equals("r"))
			{
				extractRT(fac,question,t) ;
			}
			else if (question.get("type").getAsString().equals("m"))
			{
				extractMT(fac,question,t) ;
			}
		}
		return t ;
	}
	
	//save
	public boolean saveEval (Survey surv, final String name)
	{
		surv.setFN(name);
		Gson gson = new Gson() ;
		String surveyJSON = gson.toJson(surv) ;
		
		try {
			File outFile = new File (name);
		    FileWriter fWriter = new FileWriter (outFile);
		    PrintWriter pWriter = new PrintWriter (fWriter);
		    pWriter.println(surveyJSON);
		    fWriter.close();
		    pWriter.close();
		} catch (FileNotFoundException e) {
			out("Unable to open provided filename for writing") ;
			return false ;
		} catch (IOException e) {
			out("IO Error when writing to filename") ;
			return false ;
		} 
		//out(surveyJSON) ;
		return true ;
	}
	
	//modify (display, add?, modify?)
	public Survey modify(Survey surv)
	{
		surv.display(); 
		//Add?
		out("Would you like to add questions?\n1. Yes\n2. No") ;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int choice = 0 ;
		while (choice < 1 || choice > 2){
			try {
				choice = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (choice < 1 || choice > 2)
				out ("Not a valid selection");
		}
		if(choice == 1)
			surv = addQ(surv) ;
		
		//Edit?
		out("Would you like to edit the current questions?\n1. Yes\n2. No") ;
		choice = 0 ;
		while (choice < 1 || choice > 2){
			try {
				choice = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (choice < 1 || choice > 2)
				out ("Not a valid selection");
		}
		if(choice == 1)
			surv.modify();
		return surv ;
	}	
	
	public static void questiondisplay()
	{
		out("1. New T/F question") ;
		out("2. New Multiple Choice question") ;
		out("3. New Short Answer question") ;
		out("4. New Essay question") ;
		out("5. New Ranking question") ;
		out("6. New Matching question") ;
		out("Please select a number") ;
		out("") ;
	}
	
	public List <String> extractChoices(JsonObject q, String choicename)
	{
		JsonArray choices = q.get(choicename).getAsJsonArray() ;
		List <String> strchoices = new ArrayList <String> ();
		for (int j = 0; j < choices.size(); j++)
			strchoices.add(choices.get(j).getAsString()) ;
		return strchoices ;
	}
	
	public void extractFF(SFac fac, JsonObject q, Survey s)
	{
		Freeform ff = fac.makeFF(q.get("expentry").getAsInt(), q.get("prompt").getAsString(), q.get("charlimit").getAsInt());
		s.addQ(ff);
	}
	
	public void extractFFT(TFac fac, JsonObject q, Test t)
	{
		List <String> ca = extractChoices(q, "ca") ;
		Freeform ff = fac.makeFF(q.get("expentry").getAsInt(), q.get("prompt").getAsString(), q.get("charlimit").getAsInt(), ca) ;
		t.addQ(ff);
	}
	
	public void extractMC (SFac fac, JsonObject q, Survey s)
	{
		List <String> strchoices = extractChoices(q, "choices") ;
		MultipleChoice mc = fac.makeMC(strchoices, q.get("expentry").getAsInt(), q.get("prompt").getAsString()) ;
		s.addQ(mc);
	}
	
	public void extractMCT (TFac fac, JsonObject q, Test t)
	{
		List <String> strchoices = extractChoices(q, "choices") ;
		List <String> ca = extractChoices(q, "ca") ;
		MultipleChoice mc = fac.makeMC(strchoices, q.get("expentry").getAsInt(), q.get("prompt").getAsString(), ca) ;
		t.addQ(mc);
	}
	
	public void extractTF (SFac fac, JsonObject q, Survey s)
	{
		TF tf = fac.makeTF(q.get("prompt").getAsString()) ;
		s.addQ(tf);
	}
	
	public void extractTFT (TFac fac, JsonObject q, Test t)
	{
		List <String> ca = extractChoices(q, "ca") ;
		TF tf = fac.makeTF(q.get("prompt").getAsString(), ca) ;
		t.addQ(tf);
	}
	
	public void extractR (SFac fac, JsonObject q, Survey s)
	{
		List <String> strchoices = extractChoices(q, "choices") ;
		Ranking r = fac.makeRank(strchoices, q.get("prompt").getAsString()) ;
		s.addQ(r);
	}
	
	public void extractRT (TFac fac, JsonObject q, Test t)
	{
		List <String> strchoices = extractChoices(q, "choices") ;
		List <String> ca = extractChoices(q, "ca") ;
		RankingT r = fac.makeRank(strchoices, q.get("prompt").getAsString(), ca) ;
		t.addQ(r);
	}
	
	public void extractM (SFac fac, JsonObject q, Survey s)
	{
		List <String> strchoices1 = extractChoices(q, "col1") ;
		List <String> strchoices2 = extractChoices(q, "col2") ;
		Matching m = fac.makeMatch(strchoices1, strchoices2, q.get("prompt").getAsString()) ;
		s.addQ(m);
	}
	
	public void extractMT (TFac fac, JsonObject q, Test t)
	{
		List <String> strchoices1 = extractChoices(q, "col1") ;
		List <String> strchoices2 = extractChoices(q, "col2") ;
		List <String> ca = extractChoices(q, "ca") ;
		Matching m = fac.makeMatch(strchoices1, strchoices2, q.get("prompt").getAsString(), ca) ;
		t.addQ(m);
	}
	
	public static void out(String s)
	{
		System.out.println(s); 
	}
}
