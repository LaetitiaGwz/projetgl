import java.lang.Math ;

public class Test_Math_deca{

	public static void main(String args[]){

		System.out.println("debut du test pour ulp\n");
		int nbErreur = 0 ;
		int nb = 0 ;		
		for (float x = new Float(-Math.pow(2,10)) ; x<Math.pow(2,10); x = x + (float)Math.pow(2,-13)){ 
		// avec un pas de pow(2,-14), il y a un problème pour 1024 + pas = 1024 -> boucle infini
			nb ++ ;
			if (Math.ulp(x) != Math_deca.ulp(x)) {
				System.out.println("Erreur pour x="+x);
				System.out.println("Math.ulp donne : " + Math.ulp(x));
				System.out.println("Math_deca donne : " + Math_deca.ulp(x));
				System.out.println();
				nbErreur ++;
			}
		}
		System.out.println("fin du test pour ulp. Erreur rencontré : " + nbErreur + "sur " +nb+" tests.\n");
		

		System.out.println("début du test de tan \n");
		nbErreur = 0 ;
		nb = 0 ;
		float diffRelative = (float) 0.0 ;
		float diff = (float) 0.0 ;
		for (float x = new Float(-Math.PI) ; x<Math.PI; x = x + (float)Math.pow(2,-13)){ 
			nb ++ ;
			diffRelative = Math.abs(((float)Math_deca.tan(x) - (float)Math.tan(x))/(float)Math.tan(x));
			diff = diffRelative*(float)Math.tan(x) ;
			//if ( diff >= 10*Math_deca.ulp((float)Math.tan(x))){ 
			if ( diffRelative >= Math.pow(10,-3)) {
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.tan : "+Math_deca.tan(x));
				// System.out.println("Math.tan : "+Math.tan(x));
				// System.out.println("différence relative: " + diffRelative);
				// System.out.println();
				nbErreur ++;
			}
		}		
		System.out.println("fin du test de tan. Erreur rencontré : " + nbErreur + "sur " +nb+" tests.\n");
		
		nbErreur = 0 ;
		nb = 0 ;
		System.out.println("début du test de sqrt \n");
		for(float x = (float) 0 ; x < 100 ; x = x + (float) Math.pow(2,-13)){
			diffRelative = Math.abs(((float)Math.sqrt(x) - Math_deca.sqrt(x))/(float)Math.sqrt(x));
			diff = diffRelative*(float)Math.sqrt(x);
			nb ++ ;
			//if ( diffRelative >= Math.pow(10,-6)) {
			if ( diff >= 3*Math_deca.ulp((float)Math.sqrt(x))){
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.sqrt : "+Math_deca.sqrt(x));
				// System.out.println("Math.sqrt : "+Math.sqrt(x));
				// System.out.println("différence relative: " + diffRelative);
				// System.out.println();
				nbErreur ++;
			}
		}
		System.out.println("fin du test de sqrt. Erreur rencontré : " + nbErreur + " sur " +nb+ "tests.\n");

		nbErreur = 0 ;
		nb = 0 ;
		System.out.println("début du test de cos\n");
		for(float x = (float) 0 ; x < Math.PI ; x = x + (float) Math.pow(2,-13)){
			diffRelative = Math.abs(((float)Math.cos(x) - Math_deca.cos(x))/(float)Math.cos(x));
			diff = diffRelative*(float)Math.cos(x);
			nb ++ ;
			if ( diffRelative >= Math.pow(10,-3)) {
			//if ( diff >= 3*Math_deca.ulp((float)Math.cos(x))){
				System.out.println("Erreur pour x="+x);
				System.out.println("Math_deca.sqrt : "+Math_deca.cos(x));
				System.out.println("Math.sqrt : "+Math.cos(x));
				System.out.println("différence relative: " + diffRelative);
				System.out.println();
				nbErreur ++;
			}
		}
		System.out.println("fin du test de cos. Erreur rencontré : " + nbErreur + " sur " +nb+ "tests.\n");

		nbErreur = 0 ;
		nb = 0 ;
		System.out.println("début du test de sin\n");
		for(float x = (float) 0 ; x < Math.PI ; x = x + (float) Math.pow(2,-13)){
			diffRelative = Math.abs(((float)Math.sin(x) - Math_deca.sin(x))/(float)Math.sin(x));
			diff = diffRelative*(float)Math.sin(x);
			nb ++ ;
			if ( diffRelative >= Math.pow(10,-3)) {
			//if ( diff >= 3*Math_deca.ulp((float)Math.sin(x))){
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.sqrt : "+Math_deca.sin(x));
				// System.out.println("Math.sqrt : "+Math.sin(x));
				// System.out.println("différence relative: " + diffRelative);
				// // System.out.println();
				nbErreur ++;
			}
		}
		System.out.println("fin du test de sin. Erreur rencontré : " + nbErreur + " sur " +nb+ "tests.\n");


	}
}