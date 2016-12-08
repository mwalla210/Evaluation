import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Freeform extends Question{
	protected int charlimit ;
	
	//Methods
	//Init (pass charlimit = 0 if Essay)
	public Freeform(int charlimit, String prompt, int expentry){
		super();
		this.prompt = prompt ;
		this.charlimit = charlimit;
		type = "sa" ;
		if (charlimit <= 1)
		{
			type = "e" ;
			this.gradeable = false ;
		}
		if (expentry < 1)
			throw new IllegalArgumentException("Must expect at least one answer") ;
		this.expentry = expentry ;
	}
	
	@Override
	public void instructions()
	{
		if (expentry > 1)
			Output.out("Please enter " + expentry + " answers") ;
		else
			Output.out("Please enter one answer") ;
		if (charlimit >= 1)
			Output.out("Character limit: " + charlimit + " characters") ;
	}

	@Override
	public void modify()
	{
		pMod() ;
		if (!type.equals("e"))
		{
			clMod() ;
		}
		eeMod() ;
	}
	
	public void clMod()
	{
		out("Would you like to modify the character limit?\n1. Yes\n2. No") ;
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
			out("Please enter the character limit") ;
			int charlimit = 0 ;
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
			this.charlimit = charlimit ;
		}
	}
	
	@Override
	public String processAnswer()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String answer = "" ;
		if (this.charlimit > 0)
		{
			while (answer.length() < 1 || answer.length() > charlimit)
			{
				try {
					answer = br.readLine() ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				}
				if (answer.length() < 1)
					out("Answer required") ;
				if (answer.length() > charlimit)
					out("Answer too long, please use " + (answer.length()-this.charlimit) + " less character(s)") ;
			}
		}
		else
		{
			try {
				answer = br.readLine() ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			}
		}
		return answer ;
	}

	@Override
	public void tabulate(int index, List <Response> resps)
	{
		if (charlimit > 0)
			tabSA(index, resps) ;
		else
			tabE(index, resps) ;
	}
	
	public void tabE(int index, List <Response> resps)
	{
		for (int i = 0; i < resps.size(); i++)
		{
			for (int j = 0; j < resps.get(i).responses.get(index).size(); j++)
				out("Response "+(i+1)+"."+(j+1)+": "+resps.get(i).responses.get(index).get(j)) ;
		}
	}
	
	//Unordered
	public void tabSA(int index, List <Response> resps)
	{
		List <Tabset> ts = new ArrayList <Tabset> () ;
		for (int i = 0; i < resps.size(); i++) //for each response set
		{
			Tabset nts = new Tabset(resps.get(i).responses.get(index), 1) ;
			boolean flag = false ;
			for (int j = 0; j < ts.size(); j++)
			{
				if (ts.get(j).compareRespU(nts))
				{
					ts.get(j).up(); //count++
					flag = true ;
				}
			}
			if(!flag) //not found
				ts.add(nts) ;
		}
		out("Reply sets:");
		for (int i = 0; i < ts.size(); i++)
			ts.get(i).out();
	}
}
