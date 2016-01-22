package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import org.apache.commons.lang.Validate;

/**
 * Created by florentin on 12/01/16.
 * Classe permettant de gérer l'état des registres
 */
public class RegisterManager {

	private boolean tableRegistre[];

	private int GB; // Global Pointer
	private int LB;
	private int LBfixe;
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
	public void setLB(int i){
		this.LB=i;
	}
	public void setLBfixe(int i){
		this.LBfixe=i;
	}
	public void initialiseLB(int i){
		this.LBfixe=i;
		this.LB=i;
	}
	public RegisterManager(int nbRegistre){ //nbRegistre >4 <17
		this.tableRegistre=new boolean[nbRegistre];
		for(int i=0; i<nbRegistre;i++){
			this.tableRegistre[i]=false;
		}
		this.GB = 1;
	}

	public int getTailleTable(){ // pour recupérer la dimension
		return this.tableRegistre.length;
	}

	public boolean getEtatRegistre(int registre){
			return this.tableRegistre[registre];

	}

	public int getLastregistre(){
		int i=2;
		while(i < tableRegistre.length && this.tableRegistre[i]){
			i++;
		}
		return i;
	}

	public GPRegister getGBRegister(){
		Validate.isTrue(!noFreeRegister());
		int i=2;
		while(this.tableRegistre[i]){
			i++;
		}
		return Register.getR(i);
	}

	public void setEtatRegistreTrue(int registre){
			this.tableRegistre[registre]=true;
	}

	public void setEtatRegistreFalse(int registre){
		this.tableRegistre[registre]=false;
	}

	public void resetTableRegistre(){
		for(int i=2; i<this.tableRegistre.length;i++){
			this.tableRegistre[i]=false;
		}
	}

	public boolean noFreeRegister(){
		return tableRegistre[getTailleTable()-1];
	}
}
