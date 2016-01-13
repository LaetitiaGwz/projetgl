import java.lang.Math ;

public class Test_Math_deca{

	public static void main(String args[]){

		float x = new Float(12.0) ;
		System.out.println("ulp : "+Math_deca.ulp(x));
		System.out.println("math ulp " + Math.ulp(x));
	}
}