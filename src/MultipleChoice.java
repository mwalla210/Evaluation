import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MultipleChoice extends Question{
	protected List <String> choices = new ArrayList <String> () ;
	
	//Methods
	//Init
	public MultipleChoice(List<String> choices, int expentry, String prompt) {
		super();
		this.choices = choices;
		if (expentry < 1)
			throw new IllegalArgumentException("Must expect at least one answer") ;
		if (expentry > choices.size())
			throw new IllegalArgumentException("Cannot expect more answers than choices") ;
		this.expentry = expentry;
		this.prompt = prompt ;
		type = "mc" ;
	}
	
	@Override
	public void display ()
	{
		instructions() ;
		Output.out(this.prompt) ;
		for (int i = 0; i < choices.size(); i++)
			Output.out((i+1) + ". " + choices.get(i)) ;
	}
	
	@Override
	public void instructions()
	{
		if (expentry > 1)
			Output.out("Please enter " + expentry + " numbers") ;
		else
			Output.out("Please enter one number") ;
	}
	
	@Override
	public void modify()
	{
		pMod() ;
		cMod() ;
		eeMod() ;
	}
	
	public void cMod()
	{
		out("Would you like to modify the choices?\n1. Yes\n2. No") ;
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
			List <String> choices = new ArrayList <String> () ;
			choices = requestChoices() ;
			this.choices = choices ;
		}
	}
	
	public List <String> requestChoices ()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List <String> choices = new ArrayList <String> () ;
		out("Please add your choices, or a \"q\" to stop") ;
		String choice = "" ;
		while (!(choice.equals("q")))
		{
			try {
				choice = br.readLine() ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			}
			if (!(choice.equals("q")))
				choices.add(choice) ;
		}
		return choices ;
	}
	
	@Override
	public String processAnswer()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int selection =  0 ;
		//Input process, get adtl info, add to s
		while (selection < 1 || selection > choices.size()){
			try {
				selection = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (selection < 1 || selection > choices.size())
				out ("Please enter a number between 1 and " + choices.size());
		}
		return String.valueOf(selection) ;
	}
	
	//Unordered
	@Override
	public void tabulate(int index, List <Response> resps)
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
