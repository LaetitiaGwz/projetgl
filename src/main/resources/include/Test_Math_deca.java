import java.lang.Math ;

public class Test_Math_deca{

	 //couleur pour print.
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE = "\u001B[34m"; 
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";

	public static void main(String args[]){
		
		int nbErreur = 0 ;
		int nb = 0 ;	
		float diff = 0 ;	
		double diffMean = 0 ;
		float diffMax = 0 ;
		System.out.println(ANSI_GREEN+ "debut du test pour ulp\n" + ANSI_RESET);
		for (float x = 0 ; x<Math.pow(2,-5); x = x + (float)Math.pow(2,-25)){ 
		// avec un pas de pow(2,-14), il y a un problème pour 1024 + pas = 1024 -> boucle infini
			nb ++ ;
			diff = Math.abs((float)Math.ulp(x) - Math_deca.ulp(x));
			if (diff> Math_deca.ulp(x)) {
				System.out.println("Erreur pour x="+x);
				System.out.println("Math.ulp donne : " + Math.ulp(x));
				System.out.println("Math_deca donne : " + Math_deca.ulp(x));
				System.out.println();
				nbErreur ++;
			}
		}
		System.out.println(ANSI_GREEN + "fin du test pour ulp. Erreur rencontré : " 
			+ANSI_RED+ nbErreur + ANSI_GREEN +"sur " +nb+" tests.\n "+ ANSI_RESET);

		System.out.println(ANSI_GREEN+ "début du test de tan \n"+ ANSI_RESET);
		nbErreur = 0 ;
		nb = 0 ;
		float diffRelative = (float) 0.0 ;
		diff = (float) 0.0 ;
		// long temps = System.nanoTime();
		// for (float x = (float)0.0 ; x < (float)Math.PI/2; x = x + (float)Math.pow(2,-13)){ 
		// 	nb ++ ;
		// 	diffRelative = Math.abs(((float)Math_deca.tanCordic(x) - (float)Math.tan(x))/(float)Math.tan(x));
		// 	diff = Math.abs(((float)Math_deca.tanCordic(x) - (float)Math.tan(x))) ;
		// 	if ( diff >= 10*Math_deca.ulp((float)Math.tan(x))){ 
		// 	//if ( diffRelative >= Math.pow(10,-3)) {
		// 		// System.out.println("Précision (10.ulp(tan(x))) : " + 10*Math_deca.ulp((float)Math.tan(x)));
		// 		// System.out.println("Erreur pour x="+x);
		// 		// System.out.println("Math_deca.tanCordic : "+Math_deca.tanCordic(x));
		// 		// System.out.println("Math.tan : "+(float)Math.tan(x));
		// 		// System.out.println("différence : " + diff);
		// 		// System.out.println("différence relative: " + diffRelative);
		// 		// System.out.println();
		// 		nbErreur ++;
		// 	}
		// }	
		// System.out.println(ANSI_GREEN+"fin du test de tan. Erreur rencontré : " 
		// 	+ANSI_RED+ nbErreur +  ANSI_GREEN+ "sur " +nb+
		// 	" tests. \n" +ANSI_RESET);
		
		nbErreur = 0 ;
		nb = 0 ;
		// System.out.println(ANSI_GREEN + "début du test de sqrt \n"+ANSI_RESET);
		// for(float x = (float) 0 ; x < 1; x = x + (float) Math.pow(2,-23)){
		// 	diffRelative = Math.abs(((float)Math.sqrt(x) - Math_deca.sqrt(x))/(float)Math.sqrt(x));
		// 	diff = Math.abs(((float)Math.sqrt(x) - Math_deca.sqrt(x)));
		// 	nb ++ ;
		// 	//if ( diffRelative >= Math.pow(10,-6)) {
		// 	if ( diff >= 2*Math_deca.ulp((float)Math.sqrt(x))){
		// 		// System.out.println("Erreur pour x="+x);
		// 		// System.out.println("Math_deca.sqrt : "+Math_deca.sqrt(x));
		// 		// System.out.println("Math.sqrt : "+(float)Math.sqrt(x));
		// 		// System.out.println("différence relative: " + diffRelative);
		// 		// System.out.println();
		// 		nbErreur ++;
		// 	}
		// }
		// System.out.println(ANSI_GREEN+"fin du test de sqrt. Erreur rencontré : " 
		// 	+ANSI_RED+ nbErreur +ANSI_GREEN+ " sur " +nb+ "tests.\n"+ANSI_RESET);

		// System.out.println(ANSI_GREEN+"début du test de cosCordic\n"+ANSI_RESET);
		// nbErreur=0;
		// nb=0;
		// diffMean = 0 ;
		// diffMax = 0 ;
		// diff = 0 ;
		// for(float x = 0; x < (float)Math.PI/4; x = x + (float) Math.pow(2,-17)){
		// 	diff = Math.abs( Math_deca.cosCordic(x) - (float)Math.cos(x) );
		// 	diffMax = (diff/Math_deca.ulp((float)Math.cos(x)) >diffMax)?diff/Math_deca.ulp((float)Math.cos(x)):diffMax;
		// 	nb ++ ;
		// 	diffMean +=	diff/Math_deca.ulp((float)Math.cos(x));	
		// 	if ( diff > Math_deca.ulp((float)Math.cos(x)) ){
		// 		nbErreur ++;
		// 		// System.out.println("Erreur pour x="+x);
		// 		// System.out.println("Math_deca.cosCordic : "+Math_deca.cosTaylor(x));
		// 		// System.out.println("Math.cos : "+(float)Math.cos(x));
		// 		// System.out.println("différence en ulp : " + diff/Math.ulp((float)Math.cos(x)));
		// 		//System.out.println();
		// 	}
		// }
		// diffMean /=nbErreur ;
		// System.out.println(ANSI_GREEN+"fin du test de cosCordic. Erreur rencontré : "
		// 	+ANSI_RED+ nbErreur + ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_GREEN+"Erreur moyenne en ulp : " 
		// 	+ANSI_RED+ diffMean+ANSI_GREEN+", Erreur max en ulp : " 
		// 	+ANSI_RED+ diffMax+ANSI_RESET);

		nbErreur = 0 ;
		nb = 0 ;
		// System.out.println(ANSI_GREEN+"début du test de sinCordic\n"+ANSI_RESET);
		// for(float x = (float)-0.2 ; x < (float)Math.PI/4; x = x + (float) Math.pow(2,-10)){
		// 	diffRelative = Math.abs(((float)Math.sin(x) - Math_deca.sinCordic(x))/(float)Math.sin(x));
		// 	diff = Math.abs(((float)Math.sin(x) - Math_deca.sinCordic(x)));
		// 	nb ++ ;
		// 	//if ( diffRelative >= Math.pow(10,-3)) {
		// 	if ( diff > Math_deca.ulp((float)Math.sin(x))){
		// 		// System.out.println("\nprécision (1.ulp(sin(x)) : " + Math_deca.ulp((float)Math.sin(x)));
		// 		// System.out.println("Erreur pour x="+x);
		// 		// System.out.println("Math_deca.sinCordic : "+Math_deca.sinCordic(x));
		// 		// System.out.println("Math.sin : "+(float)Math.sin(x));
		// 		// System.out.println("différence relative: " + diffRelative);
		// 		// System.out.println("différence : " + diff);
		// 		// System.out.println();
		// 		nbErreur ++;
		// 	}
		// }
		// System.out.println(ANSI_GREEN+"fin du test de sinCordic. Erreur rencontré : " 
		// 	+ANSI_RED+ nbErreur + ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_RESET);

		System.out.println(ANSI_GREEN+"début du test de sinTaylor\n"+ANSI_RESET);
		nbErreur=0;
		nb=0;
		diffMean = 0 ;
		diffMax = 0 ;
		for(float x = 0; x <(float)Math.PI*2;x += (float)Math.pow(2,-17)){
		//for(float x = (float)Math.PI/2; x <(float)Math.PI*3/2 ;x += (float)Math.pow(2,-17)){
			diff =Math.abs(((float)Math.sin(x) - Math_deca.sinTaylor(x)));
			nb ++ ;
			diffMax = (diff/Math_deca.ulp((float)Math.sin(x)) >diffMax)?diff/Math_deca.ulp((float)Math.sin(x)):diffMax;
			if ( diff >(float)Math.ulp((float)Math.sin(x))){
				nbErreur ++;
				diffMean += diff/Math_deca.ulp((float)Math.sin(x));
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.sinTaylor : "+Math_deca.sinTaylor(x));
				// System.out.println("Math.sin : "+(float)Math.sin(x));
				// System.out.println("différence en ulp : " + diff/Math_deca.ulp((float)Math.sin(x)));
				// System.out.println();


			}
		}
		diffMean/=(nbErreur);
		System.out.println(ANSI_GREEN+"fin du test de sinTaylor. Erreur rencontré : " 
			+ANSI_RED+ nbErreur + ANSI_GREEN+" sur " +nb+ "tests\n"+ANSI_GREEN+"Erreur moyenne en ulp : " 
			+ANSI_RED+ diffMean+ANSI_GREEN+", Erreur max en ulp : " 
			+ANSI_RED+ diffMax+ANSI_RESET);
		

		System.out.println(ANSI_GREEN+"début du test de cosTaylor\n"+ANSI_RESET);
		nbErreur=0;
		nb=0;
		diffMax = 0 ;
		diffMean = 0 ;
		for(float x = 0f; x <(float)Math.PI*2;x += (float)Math.pow(2,-17)){
		//for(float x = (float)Math.PI*3/2; x <(float)Math.PI*3*1.005/2  ;x += (float)Math.pow(2,-5)){			
			diff =Math.abs(((float)Math.cos(x) - Math_deca.cosTaylor(x)));
			diffMax = (diff/Math.ulp((float)Math.cos(x)) >diffMax)?diff/Math.ulp((float)Math.cos(x)):diffMax;
			nb ++ ;
			//System.out.println(diff);
			if ( diff >Math.ulp((float)Math.cos(x))){
				diffMean += diff/Math_deca.ulp((float)Math.cos(x));	
				nbErreur ++;
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.cosTaylor : "+Math_deca.cosTaylor(x));
				// System.out.println("Math.cos : "+(float)Math.cos(x));
				// System.out.println("différence en ulp : " + diff/Math_deca.ulp((float)Math.cos(x)));
				// System.out.println();
			}			
		}
		diffMean/=(nbErreur);
		System.out.println(ANSI_GREEN+"fin du test de cosTaylor. Erreur rencontré : "
			+ANSI_RED+ nbErreur + ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_GREEN+"Erreur moyenne en ulp : " 
			+ANSI_RED+ diffMean+ANSI_GREEN+", Erreur max en ulp : " 
			+ANSI_RED+ diffMax+ANSI_RESET);

		
		System.out.println(ANSI_GREEN+"début du test de arcsin\n"+ANSI_RESET);
		nbErreur=0;
		nb=0;
		diffMax = 0 ;
		diffMean = 0 ;
		for(float x = -1f; x < 1f ; x = x + (float) Math.pow(2,-15)){
			diff =Math.abs(((float)Math.asin(x) - Math_deca.asin(x)));
			diffMax = (diff/Math_deca.ulp((float)Math.asin(x)) > diffMax)?diff/Math_deca.ulp((float)Math.asin(x)):diffMax;
			nb ++ ;
			if ( diff > Math_deca.ulp((float)Math.asin(x))){
				diffMean += diff/Math_deca.ulp((float)Math.asin(x));	
				// System.out.println("Erreur pour x="+x);
				// System.out.println("Math_deca.arcsin : "+Math_deca.asin(x));
				// System.out.println("Math.arcsin : "+Math.asin(x));
				// System.out.println("différence : " + diff/Math_deca.ulp((float)Math.asin(x)));
				// System.out.println();
				nbErreur ++;
			}
		}
		diffMean/=(nbErreur);
		System.out.println(ANSI_GREEN+"fin du test de arcsin. Erreur rencontré : "+ANSI_RED+nbErreur 
			+ ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_GREEN+"Erreur moyenne en ulp : " 
			+ANSI_RED+ diffMean+ANSI_GREEN+", Erreur max en ulp : " 
			+ANSI_RED+ diffMax+ANSI_RESET);




		// System.out.println(ANSI_GREEN+"Début du test de arctan :\n"+ANSI_RESET);
		// nbErreur=0;
		// nb=0;
		// diffMax = 0 ;
		// diffMean = 0 ;
		// for(float x = 0; x < 100 ; x = x + (float) Math.pow(2,-16)){
		// 	diffRelative = Math.abs(((float)Math.atan(x) - Math_deca.atan(x))/(float)Math.atan(x));
		// 	diff =Math.abs(((float)Math.atan(x) - Math_deca.atan(x)));
		// 	diffMax = (diff/Math_deca.ulp((float)Math.atan(x)) >diffMax)?diff/Math_deca.ulp((float)Math.atan(x)):diffMax;
		// 	diff =Math.abs(((float)Math.atan(x) - Math_deca.atan(x)));
		// 	nb ++ ;
		// 	//if ( diffRelative >= Math.pow(10,-3)) {
		// 	if ( diff >= Math_deca.ulp((float)Math.atan(x))){
		// 		diffMean += diff/Math.ulp((float)Math.atan(x));	
		// 		// System.out.println("Erreur pour x="+x);
		// 		// System.out.println("Math_deca.arctan : "+Math_deca.atan(x));
		// 		// System.out.println("Math.arctan : "+(float)Math.atan(x));
		// 		// System.out.println("différence : " + diff/Math_deca.ulp((float)Math.atan(x)));
		// 		// System.out.println();
		// 		nbErreur ++;
		// 	}

		// }
		// diffMean /=nbErreur;
		// System.out.println(ANSI_GREEN+"fin du test de arctan. Erreur rencontré : "+ANSI_RED+nbErreur 
		// 	+ ANSI_GREEN+" sur " +nb+ "tests.\n"+ANSI_GREEN+"Erreur moyenne en ulp : " 
		// 	+ANSI_RED+ diffMean+ANSI_GREEN+", Erreur max en ulp : " 
		// 	+ANSI_RED+ diffMax+ANSI_RESET);


	}

}