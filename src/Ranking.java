import java.util.ArrayList;
import java.util.List;

public class Ranking extends MultipleChoice{
	//Methods
	//Init
	public Ranking(List<String> choices, int expentry, String prompt) {
		super(choices, expentry, prompt);
		type = "r" ;
	}
	
	@Override
	public void instructions()
	{
		if (expentry > 1)
			Output.out("Please enter " + expentry + " numbers (highest/most perferred choice first)") ;
		else
			Output.out("Please enter one number (highest/most perferred choice first)") ;
	}
	
	@Override
	public void modify()
	{
		pMod() ;
		cMod() ;
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
