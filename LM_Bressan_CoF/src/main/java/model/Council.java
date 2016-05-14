package model;

import java.util.*;

/**
 * 
 */
public class Council {

	/**
	 * Queue of councillors,I used LinkedList Queue type
	 */
	private Queue<Councillor> councillors = new LinkedList();

	/**
	 * Default constructor
	 */
	public Council() {
		int i;
		for (i = 0; i < 4; i++) {
			Councillor councillor = new Councillor();
			this.councillors.add(councillor);
		}

	}

	public void addCouncillor() {
		Councillor councillor = new Councillor();
		this.councillors.add(councillor);
	}

	public void removeCoucillor() { // Remove a councillor
		this.councillors.poll();
	}

	public void getCouncillor() {// Return a councillor
    Iterator <Councillor> x= this.councillors.iterator();
		while (x.hasNext())
			System.out.println(x.next());
		System.out.println("PAUSA");
		int i;
		Councillor temp;
		for(i=0;i<4;i++){
		temp=this.councillors.poll();
		System.out.println(temp.getColor());
		}
		return;

	}
	 

	public static void main(String [ ] args)
	{
	      Council council=new Council();
	      council.getCouncillor();
	}

}