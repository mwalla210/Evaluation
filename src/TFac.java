import java.util.ArrayList;
import java.util.List;

public class TFac extends SFac{
	@Override
	public Test makeEval ()
	{
		return new Test() ;
	}
	
	public MultipleChoice makeMC (List <String> choices, int expentry, String prompt, List <String> ca)
	{
		return new MultipleChoiceT(choices, expentry, prompt, ca) ;
	}
	
	public TF makeTF (String prompt, List <String> ca)
	{
		List <String> choices = new ArrayList <String> () ;
		choices.add("true") ;
		choices.add("false") ;
		int expentry = 1 ;
		return new TFT(choices, expentry, prompt, ca) ;
	}
	
	public RankingT makeRank (List <String> choices, String prompt, List <String> ca)
	{
		return new RankingT(choices, choices.size(), prompt, ca) ;
	}
	
	public Freeform makeFF (int expentry, String prompt, int charlimit, List <String> ca)
	{
		return new FreeformT(charlimit, prompt, expentry, ca) ;
	}
	
	public Matching makeMatch (List <String> col1, List <String> col2, String prompt, List <String> ca)
	{
		return new MatchingT(col1, col2, prompt, ca) ;
	}
}
