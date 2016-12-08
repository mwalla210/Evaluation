import java.util.List;

public class Response {
	protected List <List <String> > responses ;
	private double grade ;
	
	public Response(List <List <String> > responses) {
		this.responses = responses ;
	}
	
	public Response(List <List <String> > responses, int grade) {
		this.responses = responses ;
		this.grade = grade ;
	}
	
	public void setGrade(double grade)
	{
		this.grade = grade ;
	}
	
	public double getGrade()
	{
		return this.grade ;
	}
	
	public String compare (List <Question> questions)
	{
		//Process
		int complete = 0, incomplete = 0 ;
		//Different amount of responses than questions or no responses or no questions
		if (responses.size() != questions.size() || responses.size() == 0 || questions.size() == 0)
			return "sizeerror" ;
		double count = 0 ;
		for (int i = 0; i < questions.size(); i++)
		{
			if (questions.get(i).gradeable)
			{
				if (eval(responses.get(i),questions.get(i)))
					complete++ ;
				else
					incomplete++ ;
				count++ ;
			}
		}
		//Grade
		//out ("Complete: " + complete + "; incomplete: " + incomplete + "; gradeable: " + count) ;
		this.grade = complete/count*100 ;
		//Return
		return String.valueOf(complete)+"."+String.valueOf(incomplete)+"."+String.valueOf(count) ;
	}
	
	public boolean eval (List <String> resp, Question q)
	{
		List <String> ca = q.getCA() ;
		//No correct answers or number of responses and number of correct answers unequal (neither should ever be hit)
		if (ca.size()==0 || resp.size() != ca.size())
		{
			out("getCA was 0 or resp/ca sizes didn't match") ;
			return false ;
		}
		if (q.type.equals("sa"))
		{
			for (int i = 0; i < resp.size(); i++)
			{
				if (!ca.contains(resp.get(i).toUpperCase()))
				{
					return false ;
				}
			}
		}
		else
		{
			//Ordered numbers required (match exactly)
			if (q.type.equals("r") || q.type.equals("m"))
			{
				for (int i = 0; i < resp.size(); i++)
				{
					if (!ca.get(i).equals(resp.get(i)))
					{
						return false ;
					}
				}
			}
			//Unordered numbers allowed (just check contains)
			else
			{
				for (int i = 0; i < resp.size(); i++)
				{
					if (!ca.contains(resp.get(i)))
					{
						return false ;
					}
				}
				for (int i = 0; i < ca.size(); i++)
				{
					if (!resp.contains(ca.get(i)))
					{
						return false ;
					}
				}
			}
		}
		return true ;
	}
	public static void out(String s)
	{
		System.out.println(s); 
	}
}
