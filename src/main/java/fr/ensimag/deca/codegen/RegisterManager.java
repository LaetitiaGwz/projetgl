package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.apache.commons.lang.Validate;

import java.util.Random;

/**
 * Created by florentin on 12/01/16.
 * Classe permettant de gérer l'état des registres
 */
public class RegisterManager {

	// Table des registres : true = utilisé et false = libre
	private boolean tableRegistre[];

	private int GB; // Global Pointer
	private int LB; // Local Pointer

	public int getGB(){
        return this.GB;
    }
    public void incrementGB(){
        this.GB ++;
    }
	public int getLB(){
		return this.LB;
	}
	public void incrementLB(){
		this.LB ++;
	}
	public void initialiseLB(){
		this.LB=1;
	}


	public RegisterManager(int nbRegistre){ //nbRegistre >4 <17
		this.tableRegistre = new boolean[nbRegistre];
		for(int i=0; i<nbRegistre;i++){
			this.tableRegistre[i] = false;
		}
		this.GB = 1;
		this.LB=1;
	}
	public boolean[] getTableRegistre(){
		return this.tableRegistre;
	}

	public void setTableRegistre(boolean table[]){
		this.tableRegistre = table;
	}

	public int getTailleTable(){ // pour recupérer la dimension
		return this.tableRegistre.length;
	}

	public boolean getEtatRegistre(int registre){
			return this.tableRegistre[registre];
	}

	/**
	 * @return le dernier registre utilisé
     */
	public int getLastregistre(){
		int i=2;
		while((i < tableRegistre.length -1) && this.tableRegistre[i]){
			i++;
		}
		return i;
	}

	/**
	 * Méthode à appeller pour obtenir un registre libre
	 * @return le premier registre libre, ou la taille du tableau
	 * si tous les registres sont utilisés
     */
	public GPRegister getGBRegister(){
		int i=2;
		while((i < tableRegistre.length - 1) && this.tableRegistre[i]){
			i++;
		}
		tableRegistre[i]=true;
		return Register.getR(i);
	}

	private int getGBRegisterInt(){
		Random rand = new Random();
		int nombreAleatoire = rand.nextInt(getTailleTable() - 2) + 2;
		return  nombreAleatoire;
	}

	/**
	 * Récupère un registre pour un push si un registre est déjà pris.
	 * @param nonVoulue le registre déja pris
	 * @return le registre voulu
     */
	public int getGBRegisterInt(int nonVoulue){
		int nombreAleatoire = getGBRegisterInt();
		while(nombreAleatoire==nonVoulue){
			nombreAleatoire=getGBRegisterInt();
		}
		return  nombreAleatoire;
	}

	/**
	 * Remet à zéro la table des registres. Après appel
	 * à cette méthode, tous les registres sont libres et
	 * peuvent êtres utilisés.
	 */
	public void resetTableRegistre(){
		for(int i=2; i<this.tableRegistre.length;i++){
			this.tableRegistre[i]=false;
		}
	}

	/**
	 * @return vrai si il n'y a plus de registre libre
     */
	public boolean noFreeRegister(){
		return tableRegistre[getTailleTable()-1];
	}
}
