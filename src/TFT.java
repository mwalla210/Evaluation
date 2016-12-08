import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TFT extends TF{
	List <String> ca = new ArrayList <String> ();
	
	//Methods
	//Init
	public TFT(List<String> choices, int expentry, String prompt,  List <String> ca) {
		super(choices, expentry, prompt);
		this.ca = ca ;
	}
	
	public void displayFull ()
	{
		display() ;
		Output.out("Correct Answer: " + ca.get(0)) ;
	}
	
	@Override
	public void modify()
	{
		pMod() ;
		swapca() ;
	}
	
	public void swapca()
	{
		out("Would you like to switch the correct answer?\n1. Yes\n2. No") ;
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
			String newS = "" ;
			if (this.ca.get(0).equals("1"))
				newS = "2" ;
			else
				newS = "1" ;
			this.ca.clear() ;
			this.ca.add(newS) ;
		}
	}
	
	@Override
	public List <String> getCA()
	{
		return ca ;
	}
}
