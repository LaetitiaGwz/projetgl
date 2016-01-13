import java.lang.Math ;

public class Test_Math_deca{

	public static void main(String args[]){

		for (float x = new Float(-Math.pow(2,10)) ; x<=Math.pow(2,10); x = x + (float)Math.pow(2,-5)){
			//System.out.println(x);
			if (Math.ulp(x) != Math_deca.ulp(x)) {
				System.out.println("Erreur pour x="+x);
				System.out.println("Math.ulp donne : " + Math.ulp(x));
				System.out.println("Math_deca donne : " + Math_deca.ulp(x));
				System.out.println();
			}
		}
		System.out.println("fin");
		// float x = (float) 0.0;
		// System.out.println("Math.ulp donne : " + Math.ulp(x));
		// System.out.println("Math_deca donne : " + Math_deca.ulp(x));
				
	}
}