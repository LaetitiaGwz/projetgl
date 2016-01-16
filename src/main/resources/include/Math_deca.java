import java.lang.Math ;

public class Math_deca{
	public static final float MIN_VALUE = (float)1.4E-45;
	public static final float MAX_VALUE = (float)1.633123935319537E16;
	public static final float PI = (float)3.141592653589793;
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
	// changement : on ne prend plus les theta_k tel que atan(theta_k) = 10^-k mais atan(theta_k)=2^-k ;
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

	public static float cos(float x){
		float t = tan(x/2) ;
		return (float) 1/sqrt(1 + power(tan(x),2));
		//return (float)(1-power(t,2))/(1 + power(t,2)) ;

	}

	public static float sin(float x){
		return (float) tan(x)/sqrt(1 + power(tan(x),2));
		//return tan(x)*cos(x) ; 
	}
        
        
        public static float sintaylor(float x){
            float res=x;
            float temp=x;
            int i=1;
            System.out.println("test syn taylor"+x);
            do{
                temp=-temp*x*x/((1+2*i)*2*i);
                res=res+temp;
                i=i+1;
               // System.out.println(i+"\n");
                 //System.out.println(abs(res)/ulp((float)Math.sin(x)));
            }
            //while(abs(res) > ulp((float)Math.sin(x)));
           
            while(i < 7);
            return res;
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