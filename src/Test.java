import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Test extends Survey{
	//Components
	public Test()
	{
		type = "t" ;
	}
	
	//Overrides
	//Add Question
	@Override
	public void addQ (String selection, String prompt)
	{
		//Query for question components
		if (selection.equals("mc"))
		{
			List <String> choices = requestChoices() ;
			int expentry = requestEntryNum() ;
			List <String> ca = requestCA(expentry) ;
			questions.add(new MultipleChoiceT(choices, expentry, prompt, ca)) ;
		}
		else if (selection.equals("tf"))
		{
			List <String> choices = new ArrayList <String> () ;
			choices.add("true") ;
			choices.add("false") ;
			List <String> ca = requestTF() ;
			questions.add(new TFT(choices, 1, prompt, ca)) ;
		}
		else if (selection.equals("sa"))
		{
			int charlimit = requestCharLimit() ;
			int expentry = requestEntryNum() ;
			List <String> ca = requestCAs(expentry) ;
			questions.add(new FreeformT(charlimit, prompt, expentry, ca)) ;
		}
		else if (selection.equals("e"))
		{
			int expentry = requestEntryNum() ;
			List <String> ca = new ArrayList <String> () ;
			questions.add(new FreeformT(0, prompt, expentry, ca)) ;
		}
		else if (selection.equals("r"))
		{
			List <String> choices = requestChoices() ;
			List <String> ca = requestCA(choices.size()) ;
			questions.add(new RankingT(choices, choices.size(), prompt, ca)) ;
		}
		else if (selection.equals("m"))
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
			System.out.printf("%-30.30s  %-30.30s%n", "Column A", "Column B");
			//out("Column A 			Column B") ;
			for (int i = 0; i < col1.size(); i++)
				System.out.printf("%-30.30s  %-30.30s%n", (i+1) + ". " + col1.get(i), (i+1) + ". " + col2.get(i));
			List <String> ca = requestCA(col1.size()) ;
			questions.add(new MatchingT(col1,col2,prompt,ca)) ;
		}
	}
	
	public List <String> requestCA (int expentry)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List <String> ca = new ArrayList <String> () ;
		out("There are " + String.valueOf(expentry) + " expected answer(s)");
		out("Please enter the number of each correct answer") ;
		for (int i = 0; i < expentry; i++)
		{
			try {
				ca.add(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			}
		}
		return ca ;
	}
	
	public List <String> requestCAs (int expentry)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List <String> ca = new ArrayList <String> () ;
		out("There are " + String.valueOf(expentry) + " expected answer(s)");
		out("Please enter each correct answer") ;
		for (int i = 0; i < expentry; i++)
		{
			try {
				ca.add(br.readLine().toUpperCase()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			}
		}
		return ca ;
	}
	
	public List <String> requestTF ()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		List <String> ca = new ArrayList <String> () ;
		out("Please enter \"1\" for true or \"2\" for false") ;
		String s = "" ;
		while(!s.equals("true") && !s.equals("false"))
		{
			try {
				s = br.readLine() ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			}
		}
		ca.add(s) ;
		return ca ;
	}
	
	//Display
	@Override
	public void display()
	{
		out("Test\n") ;
		for (int i = 0; i < questions.size(); i++)
		{
			questions.get(i).display() ;
			out("") ;
		}
	}
	
	@Override
	public void displayFull ()
	{
		out("Test\n") ;
		for (int i = 0; i < questions.size(); i++)
		{
			questions.get(i).displayFull() ;
			out("") ;
		}
	}
	
	//Override for modification display mode
	@Override
	public void questionDisp(Question q)
	{
		q.displayFull() ;
	}
	
	@Override
	//Take
	public void take()
	{
		List <List <String> > answers = new ArrayList <List <String> > () ;
		for (int i = 0; i < questions.size(); i++)
		{
			//Take question
			questions.get(i).display();
			answers.add(questions.get(i).requestAnswer()) ;
			out("") ;
		}
		Response resp = new Response(answers) ;
		String s = resp.compare(questions) ;
		if (!s.equals("sizeerror"))
		{
			out("Grade: " + String.valueOf(resp.getGrade())) ;
			String toks [] = s.split("\\.") ;
			if (Integer.parseInt(toks[2]) < questions.size())
				out((questions.size()-Integer.parseInt(toks[2])) + " question(s) still need grading\n") ;
			responses = resp ;
			//Save
			if (!saveR())
				out("Saving responses failed") ;
		}
		else
			out("Error with resp/questions list sizes; answers not recorded") ;
	}
	
	//Tabulate?
}
