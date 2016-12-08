import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FreeformT extends Freeform{
	List <String> ca = new ArrayList <String> ();
	
	//Methods
	//Init (pass empty List if Essay (charlimit = 0)
	public FreeformT(int charlimit, String prompt, int expentry, List <String> ca) {
		super(charlimit, prompt, expentry);
		//Short Answer
		if (this.charlimit > 0)
			this.ca = ca ;
	}

	@Override
	public void displayFull ()
	{
		display() ;
		if (this.charlimit > 0)
		{
			Output.out ("Correct answer(s):") ;
			for (int i = 0; i < this.ca.size(); i++)
			{
				Output.out(this.ca.get(i));
			}
		}
	}
	
	@Override
	public void modify()
	{
		pMod() ;
		if (!type.equals("e"))
		{
			clMod() ;
			caMod() ;
		}
		eeMod() ;
	}
	
	public void caMod()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		out("Would you like to modify the correct answers?\n1. Yes\n2. No") ;
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
			out("Please enter the correct answers") ;
			List <String> ca = new ArrayList <String> () ;
			out("There are " + String.valueOf(expentry) + " expected answer(s)");
			out("Please enter each correct answer") ;
			for (int i = 0; i < expentry; i++)
			{
				try {
					ca.add(br.readLine()) ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				}
			}
			this.ca = ca ;
		}
	}

	@Override
	public List <String> getCA()
	{
		return ca ;
	}
}
