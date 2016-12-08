import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Driver {
	public static SFac sfac = new SFac() ;
	public static TFac tfac = new TFac() ;
	public static Evaluation eval = new Evaluation() ;
	
	public static void main(String[] args)
	{
		out("Welcome to the Evaluation System") ;
		menu() ;
	}
	
	public static void menu ()
	{
		Survey surv = null ;
		//Init first (do not allow main menu requests if no survey/test created
		char type = selectType() ;
		if (String.valueOf(type).equals("q"))
			return ;
		char init = init() ;
		if (String.valueOf(init).equals("q"))
			return ;
		surv = takeaction(type,init,surv) ;
		
		//Further actions (allow any request now that survey/test created)
		char action = 'a' ;
		while (!(String.valueOf(action).equals("q")))
		{
			if (String.valueOf(type).equals("s"))
				out("Survey menu:");
			else
				out("Test menu:");
			action = action() ;
			if (String.valueOf(action).equals("q"))
				return ;
			surv = takeaction(type,action,surv) ;
		}
	}
	
	//Request Survey or Test
	public static char selectType()
	{
		char type = 's' ;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		mainmenu() ;
		int i = 0 ;
		while (i < 1 || i > 3){
			try {
				i = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (i < 1 || i > 3)
				out ("Not a valid selection");
		}
		switch(i)
		{
		case 1:
			out("Survey menu:");
			break ;
		case 2:
			out("Test menu:") ;
			type = 't' ;
			break; 
		case 3:
			out("Quitting");
			return 'q';
		default: //should not be hit due to while above
			out("Not a valid selection");
			return 'q';
		}
		return type ;
	}
	
	//Request initial action (Create, Load)
	public static char init()
	{
		char init = 'c' ;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		clmenu() ;
		int i = 0 ;
		while (i < 1 || i > 3){
			try {
				i = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (i < 1 || i > 3)
				out ("Not a valid selection");
		}
		switch(i)
		{
		case 1:
			init = 'c' ;
			break ;
		case 2:
			init = 'l' ;
			break; 
		case 3:
			out("Quitting");
			return 'q' ;
		default: //should not be hit due to while above
			out("Not a valid selection");
			return 'q' ;
		}
		return init ;
	}
	
	//Request filename for Load
	public static String filename()
	{
		//Show all survs/tests in pwd, request selected
		File dir = new File("Documents");
		if (!dir.exists())
		{
			dir.mkdir() ;
			return null ;
		}
		else
		{
			String files [] = dir.list() ;
			List <String> jsonfiles = new ArrayList <String> () ;
			int count = 0 ;
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].endsWith(".json"))
				{
					count++ ;
					jsonfiles.add(files[i]) ;
					out(count + ". " + files[i]) ;
				}
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int i = 0 ;
			while (i < 1 || i > count){
				out("Please select a survey/test") ;
				try {
					i = Integer.parseInt(br.readLine()) ;
				} catch (IOException e) {
					out ("Error reading input from user") ;
				} catch(NumberFormatException nfe){
		            out ("Input not a number") ;
		        }
				if (i < 1 || i > count)
					out ("Not a valid selection");
			}
			String name = dir.toString() + File.separator + jsonfiles.get(i-1) ;
			return name ;	
		}
	}
	
	//Create sequential filename
	public static String filenamecreate(char type)
	{
		String name = "" ;
		if (String.valueOf(type).equals("s"))
			name = "Survey" ;
		else
			name = "Test" ;
		//Show all survs/tests in pwd, request selected
		File dir = new File("Documents");
		if (!dir.exists())
		{
			dir.mkdir() ;
			name = name+"1" ;
		}
		else
		{
			int count = 0 ;
			String files [] = dir.list() ;
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].contains(name))
					count++ ;
			}
			name = name+String.valueOf(count+1) ;
		}
		name = dir.toString() + File.separator + name + ".json" ;
		return name ;	
	}

	//Request further Action (Create, Load, Display, Modify, Take, Tabulate)
	public static char action()
	{
		char action = 'a';
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		fullmenu() ;
		
		int i = 0 ;
		while (i < 1 || i > 8){
			try {
				i = Integer.parseInt(br.readLine()) ;
			} catch (IOException e) {
				out ("Error reading input from user") ;
			} catch(NumberFormatException nfe){
	            out ("Input not a number") ;
	        }
			if (i < 1 || i > 8)
				out ("Not a valid selection");
		}
		switch(i)
		{
		case 1:
			action = 'c' ;
			break; 
		case 2:
			action = 'l' ;
			break;
		case 3:
			action = 'd' ;
			break;
		case 4:
			action = 's' ;
			break ;
		case 5:
			action = 'm' ;
			break ;
		case 6:
			action = 't' ; //take
			break ;
		case 7:
			action = 'b' ; //tabulate
			break ;
		case 8:
			out("Quitting");
			return 'q' ;
		default: //should not be hit due to while above
			out("Not a valid selection");
			return 'q' ;
		}
		return action ;
	}
	
	//Evaluate action
	public static Survey takeaction(char evalt, char action, Survey surv)
	{
		//Non-specific
		//Save
		if (String.valueOf(action).equals("s"))
		{
			if (!surv.filename.equals(""))
				eval.saveEval(surv, surv.filename) ;
			else
			{
				String fn = filenamecreate(evalt) ;
				eval.saveEval(surv, fn) ;
			}
		}
		//Modify
		else if (String.valueOf(action).equals("m"))
			eval.modify(surv) ;
		//Take
		else if (String.valueOf(action).equals("t"))
		{
			if (!surv.filename.equals(""))
				eval.saveEval(surv, surv.filename) ;
			else
			{
				String fn = filenamecreate(evalt) ;
				eval.saveEval(surv, fn) ;
			}
			surv.take();
		}
		//Tabulate
		else if (String.valueOf(action).equals("b"))
			surv.tabulate();
		//Specific
		else if (String.valueOf(evalt).equals("s"))
		{
			//Create survey
			if (String.valueOf(action).equals("c"))
			{
				surv = eval.createEval(sfac) ;
			}
			//Display
			else if (String.valueOf(action).equals("d"))
			{
				surv.display();
			}
			//Load survey
			else if (String.valueOf(action).equals("l"))
			{
				boolean flag = false ;
				String fn = "";
				while (!flag)
				{
					fn = filename() ;
					if (fn != null)
					{
						File file = new File(fn) ;
						if (file.exists())
						{
							surv = eval.loadEvalS(sfac, fn) ;
							if (surv != null)
								flag = true ;
							else
								out("Error creating survey") ;
						}
						else
							out("File did not exist") ;
					}
					else
						out("No files available for load in Documents directory") ;
				}
				
			}
		}
		else
		{
			//Create test
			if (String.valueOf(action).equals("c"))
			{
				surv = eval.createEval(tfac) ;
			}
			//Display
			else if (String.valueOf(action).equals("d"))
			{
				surv.displayFull();
			}
			//Load test
			else if (String.valueOf(action).equals("l"))
			{
				boolean flag = false ;
				String fn = "";
				while (!flag)
				{
					fn = filename() ;
					if (fn != null)
					{
						File file = new File(fn) ;
						if (file.exists())
						{
							surv = eval.loadEvalT(tfac, fn) ;
							if (surv != null)
								flag = true ;
							else
								out("Error creating test") ;
						}
						else
							out("File did not exist") ;
					}
					else
						out("No files available for load in Documents directory") ;
				}
			}
		}
		return surv ;
	}
	
	public static void mainmenu()
	{
		out("1. Survey") ;
		out("2. Test") ;
		out("3. Quit") ;
		out("Please select a number:") ;
		out("") ;
	}
	
	public static void clmenu()
	{
		out("1. Create") ;
		out("2. Load") ;
		out("3. Quit") ;
		out("Please select a number:\n") ; 
		out("");
	}
	
	public static void fullmenu()
	{
		out("1. Create") ;
		out("2. Load") ;
		out("3. Display") ;
		out("4. Save") ;
		out("5. Modify") ;
		out("6. Take") ;
		out("7. Tabulate") ;
		out("8. Quit") ;
		out("Please select a number:\n") ; 
		out("");
	}
	
	public static void out(String s)
	{
		System.out.println(s); 
	}
}
