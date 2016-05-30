package controller.Client;

import controller.RMIServerInt;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * 
 */
public class Client {

	Connector connector;
	RMIServerInt rmiServerInt;
	
	/**
	 * Default constructor
	 */
	public Client() {
		Scanner input = new Scanner(System.in);
		System.out.println("come vuoi connetterti?\n1)RMI\n2)Socket");
		switch (input.nextInt()){
			case 1:
				try {
					connector=new Connector();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				try {
					rmiServerInt= (RMIServerInt) Naming.lookup("rmi://127.0.0.1/registry");
					rmiServerInt.connect(connector);
				} catch (NotBoundException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}

				break;
			case 2:
				break;
		}

	}

	/**
	 * @return
	 */
	public boolean hasBuiltLastEmporium() {
		// TODO implement here
		return false;
	}
	

	/**
	 * @return
	 */
	public void performActions() {
		// TODO implement here

	}

	/**
	 * @return
	 */
	public void mainActions() {
		// TODO implement here

	}

	/**
	 * @return
	 */
	public void quickActions() {
		// TODO implement here

	}





	/**
	 * @return
	 */
	public void buildEmporiumWithPermitTile() {
		// TODO implement here

	}

	/**
	 * @return
	 */
	public void buildEmporiumWithKingsHelp() {
		// TODO implement here

	}

	




	/**
	 * @return
	 */
	public void performAdditionalMainAction() {
		// TODO implement here

	}

}