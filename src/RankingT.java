import java.util.List;

public class RankingT extends MultipleChoiceT{
	//Methods
	//Init
	public RankingT(List<String> choices, int expentry, String prompt, List <String> ca) {
		super(choices, expentry, prompt, ca);
	}	
	
	@Override
	public void instructions()
	{
		if (expentry > 1)
			Output.out("Please enter " + expentry + " numbers (highest/most perferred choice first)") ;
		else
			Output.out("Please enter one number (highest/most perferred choice first)") ;
	}
}
