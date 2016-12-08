import java.util.List;

public class TF extends MultipleChoice{		
	//Methods
	//Init
	public TF(List<String> choices, int expentry, String prompt) {
		super(choices, expentry, prompt);
		type = "tf" ;
	}
	
	@Override
	public void modify()
	{
		pMod() ;
	}
}
