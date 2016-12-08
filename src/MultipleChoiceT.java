import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceT extends MultipleChoice{
	List <String> ca = new ArrayList <String> ();
	
	//Methods
	//Init
	public MultipleChoiceT(List<String> choices, int expentry, String prompt, List <String> ca) {
		super(choices, expentry, prompt);
		this.ca = ca ;	
	}
	
	@Override
	public void displayFull ()
	{
		display() ;
		Output.out("Correct Answer(s):") ;
		for (int i = 0; i < ca.size(); i++)
			Output.out(ca.get(i)) ;
	}
	
	@Override
	public void modify()
	{
		pMod() ;
		cMod() ;
		caMod() ;
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
			out("There are " + String.valueOf(this.expentry) + " expected answers");
			out("Please enter each correct answer") ;
			for (int i = 0; i < this.expentry; i++)
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
