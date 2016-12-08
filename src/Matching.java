import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Matching extends Question{
	protected List <String> col1 = new ArrayList <String> () ;
	protected List <String> col2 = new ArrayList <String> () ;
	
	//Methods
	//Init
	public Matching(List<String> col1, List<String> col2, String prompt) {
		super();
		this.col1 = col1;
		this.col2 = col2;
		this.prompt = prompt ;
		this.expentry = col1.size() ;
		type = "m" ;
	}
	
	@Override
	public void display ()
	{
		instructions() ;
		Output.out(this.prompt) ;
		/*
		System.out.printf("%-30.30s  %-30.30s%n", "Column A", "Column B");
		for (int i = 0; i < col1.size(); i++)
			System.out.printf("%-30.30s  %-30.30s%n", (i+1) + ". " + col1.get(i), (i+1) + ". " + col2.get(i));
			*/
		Output.out("Column A");
		for (int i = 0; i < col1.size(); i++)
			Output.out("A " + (i+1) + " " + col1.get(i));
		Output.out("Column B");
		for (int i = 0; i < col2.size(); i++)
			Output.out("B " + (i+1) + " " + col2.get(i));
	}
	
	@Override
	public void instructions()
	{
		Output.out("Please enter each matching column B answer") ;
	}
	
	@Override
	public void modify()
	{
		pMod() ;
		cMod() ;
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
			this.col1 = col1 ;
			this.col2 = col2 ;
			this.expentry = col1.size() ;
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
		while (selection < 1 || selection > col2.size()){
			try {
				selection = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (selection < 1 || selection > col2.size())
				out ("Please enter a number between 1 and " + col2.size());
		}
		return String.valueOf(selection) ;
	}

	//Ordered
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
				if (ts.get(j).compareRespO(nts))
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
