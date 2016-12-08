import java.util.List;

public class Tabset {
	List <String> resp ;
	int count ;
	public Tabset(List <String> resp, int count) {
		this.resp = resp ;
		for (int i = 0; i < this.resp.size(); i++)
			resp.set(i, resp.get(i).toUpperCase()) ;
		this.count = count ;
	}
	
	public void up(){
		count++ ;
	}
	
	//Ordered compare
	public boolean compareRespO (Tabset ts)
	{
		if (this.resp.size() != ts.resp.size())
		{
			return false;
		}
		for (int i = 0; i < this.resp.size(); i++)
		{
			if (!this.resp.get(i).equals(ts.resp.get(i).toUpperCase()))
				return false;
		}
		return true ;
	}
	
	//Unordered compare
	public boolean compareRespU (Tabset ts)
	{
		if (this.resp.size() != ts.resp.size())
			return false;
		for (int i = 0; i < this.resp.size(); i++)
		{
			if (!ts.resp.contains(this.resp.get(i).toUpperCase())) //in this, not in ts
				return false;
		}
		for (int i = 0; i < ts.resp.size(); i++)
		{
			if (!this.resp.contains(ts.resp.get(i).toUpperCase())) //in ts, not in this
				return false;
		}
		return true ;
	}

	public void out()
	{
		System.out.print("[");
		for (int i = 0; i < resp.size(); i++)
		{
			if(i+1 >= resp.size())
				System.out.print(resp.get(i));
			else
				System.out.print(resp.get(i)+", ");
		}
		System.out.print("]  ");
		System.out.println(":  "+count);
	}
}
