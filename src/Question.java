import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Question {
	public String prompt = "" ;
	public String type = "Question" ;
	public boolean gradeable = true ;
	protected int expentry ;
	
	//Default display
	public void display()
	{
		instructions() ;
		Output.out("Prompt: " + this.prompt) ;
	}
	
	//Stand-in Methods
	public void displayFull()
	{
	}
	
	public void instructions()
	{
	}
	
	public void modify ()
	{	
	}
	
	public List <String> getCA()
	{
		return new ArrayList <String> () ;
	}
	
	public String processAnswer()
	{
		return "" ;
	}
	
	public void tabulate(int index, List <Response> resps)
	{
	}
	
	//Prompt modification utility
	public void pMod()
	{
		out("Would you like to modify the prompt?\n1. Yes\n2. No") ;
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
		{
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
			this.prompt = prompt ;
		}
	}

	//Expected entries modification utility
	public void eeMod()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		out("Would you like to modify the number of expected entries?\n1. Yes\n2. No") ;
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
		{
			out("Please enter the number of expected entries") ;
			int expentry = 0 ;
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
			this.expentry = expentry ;
		}
	}
	
	//Request user answer for question
	public List <String> requestAnswer()
	{
		List <String> answers = new ArrayList <String> () ;
		for (int i = 0; i < this.expentry; i++)
		{
			answers.add(processAnswer()) ;
		}
		return answers ;
	}
	
	public static void out(String s)
	{
		System.out.println(s); 
	}
}
