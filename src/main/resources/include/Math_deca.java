import java.lang.Math ;

public class Math_deca{
	public static final float MIN_VALUE = (float)1.4E-45;
	public static final float MAX_VALUE = (float)1.633123935319537E16;
	public static final float PI = (float)3.141592653589793;
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
		//Je ne sais pas s'il faut prendre ne compte les valeur +-infini et NaN 
		int exp = 0;
		if(x < 0) {
			x = -x ;
		}	
		if ( x >= 1 ) {
			exp = -1 ;
			while (x >= 1 ) {
				x =  x/(float)2.0;
				exp ++ ;
			}			
		}
		else if ( x < 1 && x != 0){
			exp = 0 ;
			while ( x < 1) {
				x = x*(float)2.0;
				exp -- ;
			}
		}
		else{ // cas x = 0.0 
			return MIN_VALUE ; // c'est ce que fait java.Math, pas sur pour le deca
		}
		return power(2,exp - 23) ;	
	}
	// Méthode de Cordic cf :http://www.trigofacile.com/maths/trigo/calcul/cordic/cordic.htm
	public static float tan(float theta){

		// On se ramène à des angles entre -PI/2 et PI/2
		while ( theta > PI/2.0 || theta < -PI/2.0){
			theta = (theta < 0) ? theta + PI : theta - PI ;
		}

		// On se ramène à des angles positifs
		int sign = 1;
		if ( theta < 0) {
			theta = - theta;
			sign = -1;
		}

		if (theta == Math_deca.PI/2.0) {
			return sign*MAX_VALUE  ;
		}
		// On initialise le tableau des theta_k
		double tab[] = {0.78539816339744830962,0.099668652491162027378,0.0099996666866652382063,
			0.00099999966666686666652,0.000099999999666666668667,0.0000099999999996666666667,
			0.00000099999999999966666667};

		int k = 0 ; //Définit le plus grand angle courant de rotation theta_k possible 
		float x = (float) 1.0 ; //Abscisse du point courant
		float xTemp = (float) 1.0 ;
		float y = (float) 0.0 ; //Ordonnée du point courant
		float epsilon = power(10,-10) ;
		
		while ( theta >= epsilon) {
			
			if ( k<6) {
				while ( theta < tab[k] ) {
					k++; 
					if (k>=5){
						break ;
					}
				}
			}
			else{
				while (theta < power(10,-k) ) {
					k++ ;
				}
			}
			
			theta = (k<6) ? theta - (float)tab[k] : theta - power(10,-k);
			xTemp = x ;
			x = x - power(10,-k)*y ;
			y = y + power(10,-k)*xTemp;
		}

		return sign*y/x ;
	}

	public static float abs(float x){
		float res = (x>=0)?x:-x;
		return res;
	}

	public static float sqrt(float x){

		if (x<0){
			System.out.println(x+" est un nombre négatif ...");
			return (float)0.0;
		}
		if (x == (float)1.0 || x == (float) 0.0){
			return x ;
		}
		// On multiplie les petits nombres pour en faire des grands nombres.
		int exp = 0 ;
		while ( x<1 ) {
			x = x*power(10,4) ;
			 // 4 car c'est une puissance de 2 et donc on peut retrouver la racine carré facilement.
			exp += 4 ;
		}
		float low = (float) 0.0; 
		float high = x;
		float mid = x;
		float oldMid = (float) -5.0 ;
		float epsilon = power(10,-15); 
		//On fait une recherche dichotomique de la racine ... ce n'est pas niveau coût.
		while ( abs(oldMid - mid)>= epsilon ) {
			oldMid = mid ;

			mid = (high + low)/2 ;
			if (mid*mid > x ){
				high = mid ;
			}
			else{
				low = mid;
			}
		}
		return mid/power(10,exp/2) ;
	}

}