import java.lang.Math ;

public class Math_deca{

	public static float power(float x, int y){

		float originalX = x ;

		if ( y==0 ) {
			return new Float(1.0);
		}
		if (y>0) {
			while ( y>1) {
				x = x*originalX ;
				y--;
			}
			return x ;
		}
		else {
			float out = power(x, -y) ;
			return 1/out ;
		}
	}

	public static float ulp(float x){

		int exp = -1 ;

		while (x >= 1 || -x >= 1) {
			x =  x/(float)2.0;
			exp ++ ;
		}

		if(x<= 0) {
			x = -x ;
		}	

		while (x  < 0.5) {
			x = x + (float)0.1 ;
				exp --  ;
		}

		// if(exp<-1){
		// 	exp++;
		// }

		return power(2,exp - 23) ;		
	}


}