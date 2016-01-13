import java.lang.Math ;

public class Test_Math_deca{

	public static void main(String args[]){

		System.out.println("debut du test pour ulp");
		System.out.println();
		int nbErreur = 0 ;
		for (float x = new Float(-Math.pow(2,10)) ; x<Math.pow(2,10); x = x + (float)Math.pow(2,-13)){ 
		// avec un pas de pow(2,-14), il y a un problème pour 1024 + pas = 1024 -> boucle infini

			if (Math.ulp(x) != Math_deca.ulp(x)) {
				System.out.println("Erreur pour x="+x);
				System.out.println("Math.ulp donne : " + Math.ulp(x));
				System.out.println("Math_deca donne : " + Math_deca.ulp(x));
				System.out.println();
				nbErreur ++;
			}
		}
		System.out.println("fin du test pour ulp. Erreur rencontré : " + nbErreur);

	}
}