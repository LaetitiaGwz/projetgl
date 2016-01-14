import java.lang.Math ;

public class Test_Math_deca{

	 //couleur pour print.
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE = "\u001B[34m"; 
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";

	public static void main(String args[]){

		System.out.println(ANSI_GREEN+ "debut du test pour ulp\n" + ANSI_RESET);
		int nbErreur = 0 ;
		int nb = 0 ;		
		for (float x = new Float(-Math.pow(2,-5)) ; x<Math.pow(2,-5); x = x + (float)Math.pow(2,-25)){ 
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
		System.out.println(ANSI_GREEN + "fin du test pour ulp. Erreur rencontré : " +ANSI_RED+ nbErreur + ANSI_GREEN +"sur " +nb+" tests.\n "+ ANSI_RESET);
		
		System.out.println(ANSI_GREEN+ "début du test de tan \n"+ ANSI_RESET);
		nbErreur = 0 ;
		nb = 0 ;
		float diffRelative = (float) 0.0 ;
		float diff = (float) 0.0 ;
		long temps = System.nanoTime();
		for (float x = (float)0.0 ; x < (float)Math.PI/2; x = x + (float)Math.pow(2,-13)){ 
			nb ++ ;
			diffRelative = Math.abs(((float)Math_deca.tan(x) - (float)Math.tan(x))/(float)Math.tan(x));
			diff = Math.abs(((float)Math_deca.tan(x) - (float)Math.tan(x))) ;
			if ( diff >= 10*Math_deca.ulp((float)Math.tan(x))){ 
			//if ( diffRelative >= Math.pow(10,-3)) {
				// System.out.println("Précision (10.ulp(tan(x))) : " + 10*Math_deca.ulp((float)Math.tan(x)));
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.tan : "+Math_deca.tan(x));
				// System.out.println("Math.tan : "+(float)Math.tan(x));
				// System.out.println("différence : " + diff);
				// System.out.println("différence relative: " + diffRelative);
				// System.out.println();
				nbErreur ++;
			}
		}	
		System.out.println(ANSI_GREEN+"fin du test de tan. Erreur rencontré : " +ANSI_RED+ nbErreur +  ANSI_GREEN+ "sur " +nb+
			" tests. \n" +ANSI_RESET);
		
		nbErreur = 0 ;
		nb = 0 ;
		System.out.println(ANSI_GREEN + "début du test de sqrt \n"+ANSI_RESET);
		for(float x = (float) 0 ; x < 1; x = x + (float) Math.pow(2,-20)){
			diffRelative = Math.abs(((float)Math.sqrt(x) - Math_deca.sqrt(x))/(float)Math.sqrt(x));
			diff = Math.abs(((float)Math.sqrt(x) - Math_deca.sqrt(x)));
			nb ++ ;
			//if ( diffRelative >= Math.pow(10,-6)) {
			if ( diff >= 2*Math_deca.ulp((float)Math.sqrt(x))){
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.sqrt : "+Math_deca.sqrt(x));
				// System.out.println("Math.sqrt : "+(float)Math.sqrt(x));
				// System.out.println("différence relative: " + diffRelative);
				// System.out.println();
				nbErreur ++;
			}
		}
		System.out.println(ANSI_GREEN+"fin du test de sqrt. Erreur rencontré : " +ANSI_RED+ nbErreur +ANSI_GREEN+ " sur " +nb+ "tests.\n"+ANSI_RESET);

		nbErreur = 0 ;
		nb = 0 ;
		System.out.println(ANSI_GREEN+"début du test de cos\n"+ANSI_RESET);
		for(float x = (float)0.0; x < (float)Math.PI/2 ; x = x + (float) Math.pow(2,-13)){
			diffRelative = Math.abs(((float)Math.cos(x) - Math_deca.cos(x))/(float)Math.cos(x));
			diff = diffRelative*(float)Math.cos(x);
			nb ++ ;
			//if ( diffRelative >= Math.pow(10,-3)) {
			if ( diff > 5*Math_deca.ulp((float)Math.cos(x))){
				// System.out.println("\n précision (5.ulp(sin(x)) : " + 5*Math_deca.ulp((float)Math.sin(x)));
				// 	System.out.println("Erreur pour x="+x);
				// 	System.out.println("Math_deca.cos: "+Math_deca.cos(x));
				// 	System.out.println("Math.cos : "+ (float)Math.cos(x));
				// System.out.println("différence relative: " + diffRelative);
				// System.out.println();
				nbErreur ++;
			}
		}
		System.out.println(ANSI_GREEN+"fin du test de cos. Erreur rencontré : " + ANSI_RED+nbErreur + ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_RESET);

		nbErreur = 0 ;
		nb = 0 ;
		System.out.println(ANSI_GREEN+"début du test de sin\n"+ANSI_RESET);
		for(float x = (float)-Math.pow(10,2) ; x < (float)Math.pow(10,2) ; x = x + (float) Math.pow(2,-10)){

			diffRelative = Math.abs(((float)Math.sin(x) - Math_deca.sin(x))/(float)Math.sin(x));
			diff = Math.abs(((float)Math.sin(x) - Math_deca.sin(x)));
			nb ++ ;
			//if ( diffRelative >= Math.pow(10,-3)) {
			if ( diff > 10*Math_deca.ulp((float)Math.sin(x))){
				// System.out.println("\nprécision (1.ulp(sin(x)) : " + Math_deca.ulp((float)Math.sin(x)));
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.sin : "+Math_deca.sin(x));
				// System.out.println("Math.sin : "+(float)Math.sin(x));
				// System.out.println("différence relative: " + diffRelative);
				// System.out.println("différence : " + diff);
				// System.out.println();
				nbErreur ++;
			}
		}
		System.out.println(ANSI_GREEN+"fin du test de sin. Erreur rencontré : " +ANSI_RED+ nbErreur + ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_RESET);



            System.out.println("test du DSE: \n");
            nbErreur=0;
            nb=0;
            for(float x = 0.F ; x < (float)Math.PI/4 ; x = x + (float) Math.pow(2,-5)){
			diffRelative = Math.abs(((float)Math.sin(x) - Math_deca.sintaylor(x))/(float)Math.sin(x));
			diff = diffRelative*(float)Math.sin(x);
			nb ++ ;
			//if ( diffRelative >= Math.pow(10,-3)) {
			if ( diff > Math_deca.ulp((float)Math.sin(x))){
				 System.out.println("Erreur pour x="+x);
				 System.out.println("Math_deca.sintaylor : "+Math_deca.sintaylor(x));
				 System.out.println("Math.sin : "+Math.sin(x));
				 System.out.println("différence : " + diff/Math_deca.ulp((float)Math.sin(x)));
				 System.out.println();
				nbErreur ++;
			}
                        
		}
            System.out.println("fin du test de sin. Erreur rencontré : " + nbErreur + " sur " +nb+ "tests.\n");
	}
   
        
        
        

}