package fr.ensimag.deca.codegen;

/**
 * Created by florentin on 12/01/16.
 */
public class GestionRegistre {

	private boolean tableRegistre[];

	public GestionRegistre(int nbRegistre){ //nbRegistre >4 <17
		this.tableRegistre=new boolean[nbRegistre];
		for(int i=0; i<nbRegistre;i++){
			this.tableRegistre[i]=false;
		}
	}

	public int getTailleTable(){ // pour recupérer la dimension
		return this.tableRegistre.length;
	}

	public boolean getEtatRegistre(int registre){
			return this.tableRegistre[registre];

	}

	public void setEtatRegistre(int registre,boolean etat){
			this.tableRegistre[registre]=etat;
	}


}
