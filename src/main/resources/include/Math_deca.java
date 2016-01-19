import java.lang.Math ;
/*liens : 
valeur cody and waite : http://arxiv.org/pdf/0708.3722.pdf
Cordic : http://www.trigofacile.com/maths/trigo/calcul/cordic/cordic.htm
*/
public class Math_deca{

	public static final float MIN_VALUE = Float.MIN_VALUE;
	public static final float MAX_VALUE = Float.MAX_VALUE;
	public static final float PI =(float)3.141592653589793;
	public static final float PI_4 = (float)0.7853981633974483 ;
	public static final float PIDIV=(float)Math.PI/8;
	public static final float PIDIV2=(float)Math.PI/2;

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
		//On ne pourra pas prendre en compte les infinis et les NaN en deca
		int exp = 0;
		
		if ( x == -MAX_VALUE || x == MAX_VALUE) {
			return power(2,104);
		}

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
	// changement : on ne prend plus les theta_k tel que atan(theta_k) = 10^-k mais atan(theta_k)=2^-k ;
	public static float tanCordic(float theta){

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

		//if (-theta + Math_deca.PI/(float)2.0 <= Math_deca.ulp(Math_deca.PI/(float)2.0)) {
		if (theta == Math_deca.PI/(float)2.0 ) {	
			return sign*MAX_VALUE  ;
		}
		// On initialise le tableau des theta_k
		float tab[] = {(float)0.7853981633974483,(float)0.4636476090008061,(float)0.24497866312686414,(float)0.12435499454676144,
			(float)0.06241880999595735,(float)0.031239833430268277,(float)0.015623728620476831,(float)0.007812341060101111,(float)0.0039062301319669718,
			(float)0.0019531225164788188,(float)9.765621895593195E-4,(float)4.882812111948983E-4,(float)2.4414062014936177E-4,(float)1.2207031189367021E-4,
			(float)6.103515617420877E-5,(float)3.0517578115526096E-5,(float)1.5258789061315762E-5,(float)7.62939453110197E-6,(float)3.814697265606496E-6,
			(float)1.907348632810187E-6,(float)9.536743164059608E-7,(float)4.7683715820308884E-7,(float)2.3841857910155797E-7,(float)1.1920928955078068E-7,
			(float)5.960464477539055E-8,(float)2.9802322387695303E-8,(float)1.4901161193847655E-8,(float)7.450580596923828E-9,(float)3.725290298461914E-9,
			(float)1.862645149230957E-9,(float)9.313225746154785E-10,(float)4.6566128730773926E-10,(float)2.3283064365386963E-10,(float)1.1641532182693481E-10,
			(float)5.820766091346741E-11};

		int k = 0 ; //Définit le plus grand angle courant de rotation theta_k possible 
		float x = (float) 1.0 ; //Abscisse du point courant
		float xTemp = (float) 1.0 ;
		float y = (float) 0.0 ; //Ordonnée du point courant
		float epsilon = power(2,-45) ;
		
		while ( theta >= epsilon) {
			
			if ( k<34) {
				while ( theta < tab[k] ) {
					k++; 
					if (k>=35){
						break ;
					}
				}
			}
			else{
				while (theta < power(2,-k) ) {
					k++ ;
				}
			}
			
			theta = (k<34) ? theta - (float)tab[k] : theta - power(2,-k); //approximation tan(theta)=theta pour theta petit
			xTemp = x ;
			x = x - power(2,-k)*y ;
			y = y + power(2,-k)*xTemp;
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
		float epsilon = power(2,-23); 
		//On fait une recherche dichotomique de la racine ... ce n'est pas génial niveau coût.
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

	public static float cosCordic(float x){
		float t = tanCordic(x/2) ;
		return (float) 1/sqrt(1 + power(tanCordic(x),2));
		//return (float)(1-power(t,2))/(1 + power(t,2)) ;

	}

	public static float sinCordic(float x){
		return (float) tanCordic(x)/sqrt(1 + power(tanCordic(x),2));
		//return tanCordic(x)*cosCordic(x) ; 
	}

	public static float sinTaylor(float x){
		if ( x == PI || x == -PI){
			return (float) 0.0 ;
		}
		//réduction sur ]-PI;PI]
		x = reductionCodyAndWaite(x) ;

		if ( x == PI/2 || x == -PI/2){
			if ( x>0){
				return (float) 1.0 ;
			}
			else {
				return (float) -1.0;
			}
		}

		//réduction sur ]-PI/2; PI/2]
		boolean minus = false;
		// float c1 = (float)3.1415863; // meilleur valeur : 3.1415863
		// float c2 = (float)0.00000635358;//0.00000635358
		// float c1 = (float)3.125; // meilleur valeur : 3.125
		// float c2 = (float)0.016571045;// 0.016571045;
		// float c3 = (float)0.00002160858;//0.00002160858
		float c1 = (float)13176796*power(2,-22);
		float c2 = (float)-11464520*power(2,-45);
		float c3 = (float)-15186280*power(2,-67);
		if ( x > PI/2 || x < -PI/2){
			minus = !minus;
			if(x>0){
				x = x-c1;
				x = x-c2;
				x = x-c3;
			}
			else{
				x = x+c1;
				x = x+c2;
				x = x+c3;
			}
		}
		//réduction sur [-PI/4;PI/4]
		// c1 = (float)1.5707855 ; // meilleur valeur :1.5707855
		// c2 = (float)0.00001082679; // 0.00001082679
		// test : 618 erreurs
		c1 = (float)13176796*power(2,-23);
		c2 = (float)-11464520*power(2,-46);
		c3 = (float)-15186280*power(2,-68);

		if ( x>PI/4 ) {
			x = x -c1;
			x = x -c2;
			x = x -c3;
			if(minus){
				return -cosTaylor(x);
			}
			else{
				return cosTaylor(x);
			}		
		}
		else if ( x<-PI/4){
			x = x + c1;
			x = x + c2;
			x = x + c3;

			if(minus){
				return cosTaylor(x);
			}
			else{
				return -cosTaylor(x);
			}
		}

		//réduction sur [0;PI/4]
		if (x<0){

			minus = !minus;
			x=-x;
		}

		//réduction sur [O;PI/8]
		// c1 = (float)13176796*power(2,-25);
		// c2 = (float)-11464520*power(2,-48);
		// c3 = (float)-15186280*power(2,-70);
		// if ( x>PI/8 ) {
		// 	x = x -c1;
		// 	x = x -c2;
		// 	x = x -c3;
		// 	if(minus){
		// 		return -sinTaylor(x)*cosTaylor(PI/8) - cosTaylor(x)*sinTaylor(PI/8);
		// 	}
		// 	else{
		// 		return sinTaylor(x)*cosTaylor(PI/8) + cosTaylor(x)*sinTaylor(PI/8);
		// 	}		
		// }
		
		float res=x;
		float temp=x;
		int i=1;
		while ( abs(temp)> ulp(res)) {
		//while ( i < 4){
			temp = - temp*power(x,2)/((2*i + 1)*2*i);
			res = res + temp ;
			i++;
		}
		if(minus){
			return -res ;
		}
		else{
			return res ;
		}
	}

	public static float cosTaylor(float x){

		//réduction sur ]-PI;PI]
		x = reductionCodyAndWaite(x) ;

		//réduction sur ]-PI/2; PI/2]
		boolean minus = false;
		// float c1 = (float)3.125; // meilleur valeur : 3.125
		// float c2 = (float)0.016571045;// 0.016571045;
		// float c3 = (float)0.00002160858;//0.00002160858

		float c1 = (float)13176796*power(2,-22);
		float c2 = (float)-11464520*power(2,-45);
		float c3 = (float)-15186280*power(2,-67);


		if ( x > PI/2 || x < -PI/2 ){
			minus = !minus;
			if (x>0){
				x = x - c1;
				x = x - c2;
				x = x - c3;
			}
			else{
				x = x + c1;
				x = x + c2;
				x = x + c3;
			}
		}

		//réduction sur [-PI/4;PI/4]
		//meilleurs valeurs :
		// c1 = (float)1.5703125;
		// c2 = (float)4.825592E-4;
		// c3 = (float)0.00000126759;

		c1 = (float)13176796*power(2,-23);
		c2 = (float)-11464520*power(2,-46);
		c3 = (float)-15186280*power(2,-68);


		
		if ( x > PI/4 ) {
			x = x - c1;
			x = x - c2;
			x = x - c3;
			if(minus){
				return sinTaylor(x);
			}
			else{

				return -sinTaylor(x);
			}
		}
		else if ( x < -PI/4 ){
			x = x + c1;
			x = x + c2;
			x = x + c3;
			if(minus){
				return -sinTaylor(x);
			}
			else{
				return sinTaylor(x);
			}
		}

		if (x<0){
			x=-x;
		}
		// c1 = (float)13176796*power(2,-25);
		// c2 = (float)-11464520*power(2,-48);
		// c3 = (float)-15186280*power(2,-70);
		// if ( x>PI/8 ) {
		// 	x = x -c1;
		// 	x = x -c2;
		// 	x = x -c3;
		// 	if(minus){
		// 		return -cosTaylor(x)*cosTaylor(PI/8) + sinTaylor(x)*sinTaylor(PI/8);
		// 	}
		// 	else{
		// 		return cosTaylor(x)*cosTaylor(PI/8) - sinTaylor(x)*sinTaylor(PI/8);
		// 	}		
		// }
		
		float res = 1;
		float temp=1;
		int i=1;

		//while ( i<6 || abs(temp)> ulp(res) ) {
		while ( i < 6) {
			temp = -temp*power(x,2)/((2*i - 1)*2*i);
			res=res+temp;
			i ++ ;
		}
		if(minus){
			return -res ;
		}
		else{
			return res ;
		}
	}

	//cf : 
	//http://www.vinc17.net/research/papers/arithflottante.pdf
	// Réduction  l'intervalle ]-PI,PI]
	// retourne un nb positif
	public static float reductionCodyAndWaite(float x){
		if(abs(x)<= 2*PI){
			return x ;
		}
		int k = 0 ;
		float c1 = (float)13176796*power(2,-21);
		float c2 = (float)-11464520*power(2,-44);
		float c3 = (float)-15186280*power(2,-66);
		float xTemp = abs(x) ;
		boolean sign = ( x > 0 )? true : false ;
		while ( xTemp >= 2*PI){
			xTemp = xTemp -(float)2*PI;
			k = (sign)?k+1:k-1 ;
		}
		x = x - k*c1 ;
		x = x - k*c2 ;
		x = x - k*c3 ;
		return x ;
	}

	public static float fact(int n){
		if (n==1 || n==0){
			return 1;
		}
		else{
			return n*fact(n-1);
		}
	}     

	public static float asindse(float x){
        //float a1=power(x,3)/6;
        //float a2=3*power(x,5)/40;
        //float a3=5*power(x,7)/112;
        //res=res+a1+a2+a3;
        //return res;
		float temp=power(x,3)/6;
		float res=x+temp;
		int i=2;
		while(abs(temp)>ulp(res)){
			temp=fact(2*i)*power(x,1+2*i)/(power(2,2*i)*fact(i)*fact(i)*(2*i+1));
			res=res+temp;
			i=i+1;
		}
		return res;
	}

	public static float asin(float x){
		if(abs(x)<0.75){
			return asindse(x);
		}
		else if(x>0.75){ return PIDIV2-asindse(sqrt(1-x*x));}
		else return asindse(sqrt(1-x*x))-PIDIV2;
	}

	public static float atan(float x){
		float temp=-power(x,3)/3;
		float res=x+temp;
		int i=2;
		while(i<6){
			temp=power(-1,i)*power(x,1+2*i)/(1+2*i);
			res=res+temp;
			i=i+1; 
		}
		return res;
	}



}