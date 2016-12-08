import java.util.ArrayList;
import java.util.List;

public class SFac {
	public Survey makeEval ()
	{
		return new Survey() ;
	}
	
	public MultipleChoice makeMC (List <String> choices, int expentry, String prompt)
	{
		return new MultipleChoice(choices, expentry, prompt) ;
	}
	
	public TF makeTF (String prompt)
	{
		List <String> choices = new ArrayList <String> () ;
		choices.add("true") ;
		choices.add("false") ;
		int expentry = 1 ;
		return new TF(choices, expentry, prompt) ;
	}
	
	public Ranking makeRank (List <String> choices, String prompt)
	{
		return new Ranking(choices, choices.size(), prompt) ;
	}
	
	public Freeform makeFF (int expentry, String prompt, int charlimit)
	{
		return new Freeform(charlimit, prompt, expentry) ;
	}
	
	public Matching makeMatch (List <String> col1, List <String> col2, String prompt)
	{
		return new Matching(col1, col2, prompt) ;
	}
}
