import java.lang.Math ;
/**
 * Classe CodyAndWaite : implémente les valeurs de retour de l'algorithme de Cody and Waite. 
 * Liens utiles : 
 * @author Adrien Fischman, Germain Geoffroy.
 */
class CodyAndWaite{
	/**
	* La valeur x est la valeur qui va être réduite.
	*/
	public static float x ;

	/**
	*Le coefficient de réduction de x.
	*/
	public static int k ;

	public CodyAndWaite(){
		this.x = 0f ;
		this.k = 0;
	}
	public CodyAndWaite(float x, int k){
		this.x = x ;
		this.k = k ;
	}
	/**
	*Met à jour la valeur réduite.
	*@param Valeur de mise à jour.
	*/
	public void setX(float x){
		this.x = x ;
	}
	/**
	*Met à jour le coefficient de réduction.
	*@param Valeur de mise à jour.
	*/
	public void setK(int k){
		this.k = k ;
	}
	/**
	*Retourne la valeur réduite.
	*@return Valeur réduite.
	*/
	public float getX(){
		return this.x ;
	}
	/**
	*Retourne le coefficient de réduction.
	*@return Coefficient de réduction.
	*/
	public int getK(){
		return this.k;
	}
}
/**
 * Classe Math. 
 * Liens utiles : 
 * @author Adrien Fischman, Germain Geoffroy.
 */
public class Math_deca{
	/**
	* Valeur minimale prise par les nombres flottants.
	*/
	public static final float MIN_VALUE = Float.MIN_VALUE;
	/**
	* Valeur maximale prise par les nombres flottants.
	*/
	public static final float MAX_VALUE = Float.MAX_VALUE;
	/**
	* Valeur de Pi en flottant ;
	*/
	public static final float PI = 3.14159273653589793f;
	//public static final float PI_2 = (float)1.5707964 ;
	// public static final float PI_4 = (float)0.7853982 ;
	// public static final float PIDIV=(float)Math.PI/8;
	// public static final float PIDIV2=(float)Math.PI/2;

	/**
	* Calcule x^y et renvoie le résultat.
	*@param flottant qui va être élevé à la puissance y.
	*@param entier correspond à la puissance.
	*@return flottant représentant le résultat de x^y.
	*/
	public static float power(float x, int y){

		float originalX = x;

		if ( y==0 ) {
			return 1.0f;
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

	/**
	* Calcule ulp(x) et renvoie le résultat.
	*@param flottant dont on calcule son ulp.
	*@return flottant correspond au nombre d'ulp du flottant x.
	*/
	public static float ulp(float x){
		/*Les NaN et les infinis sont gérés par le compilateur qui lève les exceptions adéquats.
		On ne prend donc pas en compte ces cas ici.*/

		//valeur de l'exposant de x.
		int exp = 0;
		
		if ( x == -MAX_VALUE || x == MAX_VALUE) {
			//valeur de retour de la méthode ulp de la classe Math de Java.
			return power(2,104);
		}
		//On utilise la symétrie : ulp(-x)=ulp(x).
		if(x < 0) {
			x = -x ;
		}	

		if ( x >= 1 ) {
			//L'exposant est décrémenté de 1 pour satisfaire la condition 2^0=1.
			exp-- ;
			while (x >= 1 ) {
				x =  x/(float)2.0;
				exp ++ ;
			}			
		}
		else if ( x < 1 && x != 0){
			while ( x < 1) {
				x = x*(float)2.0;
				exp -- ;
			}
		}
		// cas x = 0
		else{
			//valeur de retour de la méthode ulp de la classe Math de Java.
			return MIN_VALUE ; 
		}
		return power(2,exp - 23) ;	
	}

	// changement : on ne prend plus les theta_k tel que atan(theta_k) = 10^-k mais atan(theta_k)=2^-k ;
	public static float tanCordic(float theta){

		// On se ramène à des angles entre -PI/2 et PI/2
		while ( theta > PI/2 || theta < -PI/2){
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

	/**
	* Calcule la valeur absolue de x et renvoie le résultat.
	*@param flottant dont on calcule sa valeur absolue.
	*@return flottant correspond à la valeur aboslue de x.
	*/
	public static float abs(float x){
		float res = (x>=0)?x:-x;
		return res;
	}

	/**
	* Calcule la racine carrée de x et renvoie le résultat.
	*@param flottant dont on calcule sa racine carré.
	*@return flottant correspond à la racine carré de x.
	*@throws erreur soulevé si le flottant x est négatif.
	*/
	public static float sqrt(float x){
		//Déclaration variables :
		//Exposant servant dans le cas x<1.
		int exp = 0 ;
		//Variables pour la recherche dichotomique.
		float low = (float) 0.0; 
		float high = x;
		float mid = x;
		float oldMid = (float) -5.0 ;
		float epsilon = power(2,-45); 

		if (x<0){
			String message = x+" est un nombre négatif, on ne considère pas les racines carrés des nombres négatifs.";
			//throw new arithmeticException(message);
			System.out.println(message);
			return (float)0.0;
		}

		if (x == (float)1.0 || x == (float) 0.0){
			return x ;
		}

		// On multiplie les nombres inférieurs à 1 par des puissances de 10 pour améliorer la précision.
		while ( x<1 ) {
			x = x*power(10,4) ;
			// 4 car c'est une puissance de 2 et donc on peut retrouver la racine carré facilement.
			exp += 4 ;
		}
		high = x;
		mid = x;
		//On fait une recherche dichotomique de la racine carrée.
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
		//On prend en compte le cas x<1 en divisant par 10^{exp/2}.
		return mid/power(10,exp/2) ;
	}

	public static float cosCordic(float x){
		float t = tanCordic(x/2) ;
		//return (float) 1/sqrt(1 + power(tanCordic(x),2));
		return (float)(1-power(t,2))/(1 + power(t,2)) ;

	}

	public static float sinCordic(float x){
		return (float) tanCordic(x)/sqrt(1 + power(tanCordic(x),2));
		//return tanCordic(x)*cosCordic(x) ; 
	}


	/**
	* Calcule la racine carrée de x et renvoie le résultat.
	*@param flottant dont on calcule sa racine carré.
	*@return flottant correspond à la racine carré de x.
	*@throws erreur soulevé si le flottant x est négatif.
	*/
	public static float sinTaylor(float x){
		//réduction sur ]-PI;PI]
		// int k = 0 ;
		// x = reductionCodyAndWaite(x,k) ;
		// // if ( x == PI || x == -PI){
		// // 	return (float) 0.0 ;
		// // }
		// // if ( x == PI_2 || x == -PI_2){
		// // 	if ( x>0){
		// // 		return (float) 1.0 ;
		// // 	}
		// // 	else {
		// // 		return (float) -1.0;
		// // 	}
		// // }

		// //réduction sur ]-PI_2; PI/2]
		// boolean minus = false;
		// // float c1 = (float)3.1415863; // meilleur valeur : 3.1415863
		// // float c2 = (float)0.00000635358;//0.00000635358
		// // float c1 = (float)3.125; // meilleur valeur : 3.125
		// // float c2 = (float)0.016571045;// 0.016571045;
		// // float c3 = (float)0.00002160858;//0.00002160858
		// float c1 = (float)13176796*power(2,-22);
		// float c2 = (float)-11464520*power(2,-45);
		// float c3 = (float)-15186280*power(2,-67);
		// if ( x > PI_2 || x < -PI_2){
		// 	minus = !minus;
		// 	if(x>0){
		// 		x = x-c1;
		// 		x = x-c2;
		// 		x = x-c3;
		// 	}
		// 	else{
		// 		x = x+c1;
		// 		x = x+c2;
		// 		x = x+c3;
		// 	}
		// }
		// //réduction sur [-PI/4;PI/4]
		// // c1 = (float)1.5707855 ; // meilleur valeur :1.5707855
		// // c2 = (float)0.00001082679; // 0.00001082679
		// // test : 618 erreurs
		// c1 = 13176796*power(2,-23);
		// c2 = -11464520*power(2,-46);
		// c3 = -15186280*power(2,-68);
		// //if ( x>PI_4 ) {
		// if ( x>c1/2 ) {	
		// 	x = x -c1;
		// 	x = x -c2;
		// 	x = x -c3;
		// 	if(minus){
		// 		return -cosTaylor(x);
		// 	}
		// 	else{
		// 		return cosTaylor(x);
		// 	}		
		// }
		// //else if ( x<-PI_4){
		// else if ( x<-c1/2){	
		// 	x = x + c1;
		// 	x = x + c2;
		// 	x = x + c3;

		// 	if(minus){
		// 		return cosTaylor(x);
		// 	}
		// 	else{
		// 		return -cosTaylor(x);
		// 	}
		// }
		// //réduction sur [0;PI/4]
		// if (x<0){
		// 	minus = !minus;
		// 	x=-x;
		// }
		float coeff = (float)sqrt(2);
		int k = 0 ;
		boolean minus = false ;
		CodyAndWaite reduction = reductionCodyAndWaite(x,k);
		float res = x ;
		float temp = x ;
		int i = 1 ;
		x = reduction.getX();
		k = reduction.getK();
		k=k%8;
		coeff = 1/coeff ;
		if(k==1){
			return (float)(coeff*(sinTaylor(x)+cosTaylor(x)));
		}
		else if (k==2){
			return cosTaylor(x);
		}
		else if (k==3){
			return (float)( coeff*( -sinTaylor(x) + cosTaylor(x) ) );
		}
		else if (k==4){
			minus = !minus;
		}
		else if (k==5){
			return (float)(-coeff*(sinTaylor(x)+cosTaylor(x)));
		}
		else if (k==6){
			return -cosTaylor(x);
		}
		else if (k==7){
			return (float)(coeff*(sinTaylor(x)-cosTaylor(x)));
		}
		
		//Développement en série entière.
		res = x;
		temp = x;
		while ( abs(temp) > ulp(res)){
		//while ( i < 15){
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
		// zone de test 
		// supposons x >0
		// float xt =x;
		// int k = (int) (x/(Math.PI/4));
		// if(x-k*Math.PI/4>Math.PI/8.0)
		// 	k++;
		// // 110010 010000 111111 011010 101000 100010 000101 101000 110000 100011 0100
		// // 1100010011000110011000101000101110000000110111000001110011010001
		// float c11 = (0b110010)*power(2,-6);
		// float c21 = (0b010000)*power(2,-12);
		// float c31 = (0b111111)*power(2,-18);
		// float c41 = (0b011010)*power(2,-24);
		// float c51 = (0b101000)*power(2,-30);
		// float c61 = (0b100010)*power(2,-36);
		// float c71 = (0b000101)*power(2,-42);
		// float c81 = (0b101000)*power(2,-48);
		// float c91 = (0b110000)*power(2,-54);
		// float c101 = (0b100011)*power(2,-60);
		// float c111 = (0b010011)*power(2,-66);
		// // float c11 = (0b110)*power(2,-3);
		// // float c11 = (0b110)*power(2,-3);
		// // float c11 = (0b110)*power(2,-3);
		// // float c11 = (0b110)*power(2,-3);
		// // float c11 = (0b110)*power(2,-3);
		// x= x-k*c11;
		// x= x-k*c21;
		// x= x-k*c31;
		// x= x-k*c41;
		// x= x-k*c51;
		// x= x-k*c61;
		// x= x-k*c71;
		// x= x-k*c81;
		// x= x-k*c91;
		// x= x-k*c101;
		// x= x-k*c111;
		// System.out.println("my tests : " +k);
		// System.out.println("java cos " + Math.cos(xt));
		// System.out.println("my cos"+ sinTaylor(x));
		// float diff =Math.abs(Math_deca.sinTaylor(x) - (float)Math.cos(xt));
		// diff/=Math.ulp(Math.cos(xt));
		// System.out.println("diff : "+diff);
		// System.out.println("fin de mes tests:");

		//	fin zone 

		// //réduction sur ]-PI;PI]
		// x = reductionCodyAndWaite(x) ;
		// // if ( x == PI || x == -PI){
		// // 	return (float) -1.0 ;
		// // }
		// // if ( x == PI_2 || x == -PI_2){
		// // 	return (float) 0.0 ;
		// // }
		// //réduction sur ]-PI/2; PI/2]
		// boolean minus = false;
		// // float c1 = (float)3.125; // meilleur valeur : 3.125
		// // float c2 = (float)0.016571045;// 0.016571045;
		// // float c3 = (float)0.00002160858;//0.00002160858
		// float c1 = 13176796*power(2,-22);
		// float c2 = -11464520*power(2,-45);
		// float c3 = -15186280*power(2,-67);

		// if ( x > PI_2 || x < -PI_2 ){
		// 	minus = !minus;
		// 	if (x>0){
		// 		x = x - c1;
		// 		x = x - c2;
		// 		x = x - c3;
		// 	}
		// 	else{
		// 		x = x + c1;
		// 		x = x + c2;
		// 		x = x + c3;
		// 	}
		// }
		// System.out.println("valeur x : " + x);
		// //réduction sur [-PI/4;PI/4]
		// //meilleurs valeurs :
		// // c1 = (float)3.140625/2;
		// // c2 = (float)9.67653589793E-4/2;
		// // c3 = 0f;
		// c1 = 13176796*power(2,-23);
		// c2 = -11464520*power(2,-46);
		// c3 = -15186280*power(2,-68);
		
		// if ( x > PI_4 ) {
		// 	x = x - c1;
		// 	x = x - c2;
		// 	x = x - c3;
		// 	if(minus){
		// 		return sinTaylor(x);
		// 	}
		// 	else{

		// 		return -sinTaylor(x);
		// 	}
		// }
		// else if ( x<-PI_4 ){
		// 	x = x + c1;
		// 	x = x + c2;
		// 	x = x + c3;
		// 	if(minus){
		// 		return -sinTaylor(x);
		// 	}
		// 	else{
		// 		return sinTaylor(x);
		// 	}
		// }
		// if (x<0){
		// 	x = -x;
		// }

		int k = 0 ;
		boolean minus = false ;
		CodyAndWaite reduction = reductionCodyAndWaite(x,k);
		float res = 1;
		float temp = 1;
		int i = 1;
		float coeff = (float)sqrt(2);
		x = reduction.getX();
		k = reduction.getK();
		k = k%8 ; 
		coeff = 1/coeff ;
		if(k==1){
			return (float)(coeff*(cosTaylor(x) - sinTaylor(x)));
		}
		else if (k==2){
			return -sinTaylor(x);
		}
		else if (k==3){
			return (float)(-coeff*(cosTaylor(x)+sinTaylor(x)));
		}
		else if (k==4){
			minus = !minus ;
		}
		else if (k==5){
			return (float)(coeff*(-cosTaylor(x)+sinTaylor(x)));
		}
		else if (k==6){
			return sinTaylor(x);
		}
		else if (k==7){
			return (float)(coeff*(cosTaylor(x) + sinTaylor(x)));

		}
		
		//Développement en série entière.
		 while ( abs(temp)> ulp(res) ) {
		//while ( i < 15){
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
	public static CodyAndWaite reductionCodyAndWaite(float x, int k){
		CodyAndWaite res = new CodyAndWaite();
		boolean minus = false ;
		// float c1 = (0b110010)*power(2,-6);
		// float c2 = (0b010000)*power(2,-12);
		// float c3 = (0b111111)*power(2,-18);
		// float c4 = (0b011010)*power(2,-24);
		// float c5 = (0b101000)*power(2,-30);
		// float c6 = (0b100010)*power(2,-36);
		// float c7 = (0b000101)*power(2,-42);
		// float c8 = (0b101000)*power(2,-48);
		// float c9 = (0b110000)*power(2,-54);
		// float c10 = (0b100011)*power(2,-60);
		// float c11 = (0b010011)*power(2,-66);
		// float c12 = (0b000100)*power(2,-72);
		// float c13 = (0b110001)*power(2,-78);
		// float c14 = (0b100110)*power(2,-84);
		// float c15 = (0b001010)*power(2,-90);
		// float c16 = (0b001011)*power(2,-96);
		float c1 = 50*power(2,-6);
		float c2 = 16*power(2,-12);
		float c3 = 63*power(2,-18);
		float c4 = 26*power(2,-24);
		float c5 = 40*power(2,-30);
		float c6 = 34*power(2,-36);
		float c7 = 5*power(2,-42);
		float c8 = 40*power(2,-48);
		float c9 = 48*power(2,-54);
		float c10 = 35*power(2,-60);
		float c11 = 19*power(2,-66);
		float c12 = 4*power(2,-72);
		float c13 = 49*power(2,-78);
		float c14 = 38*power(2,-84);
		float c15 = 10*power(2,-90);
		float c16 = 11*power(2,-96);
		if(x<PI/8.0){
			res.setX(x); 
			return res ;
		}

		if(x<0){
			x = -x;
			minus = !minus ;
		}
		//réduction sur [-PI/8;PI/8]
		k = (int) (x/(Math.PI/4));
		//on préfère être proche de 0 plutot que PI/4
		if(x-k*Math.PI/4>Math.PI/8.0){
			k++;
		}
		
		x = x - k*c1;
		x = x - k*c2;
		x = x - k*c3;
		x = x - k*c4;
		x = x - k*c5;
		x = x - k*c6;
		x = x - k*c7;
		x = x - k*c8;
		x = x - k*c9;
		x = x - k*c10;
		x = x - k*c11;
		x = x  - k*c12;
		x = x  - k*c13;
		x = x  - k*c14;
		x = x  - k*c15;
		x = x  - k*c16;

		res.setK(k);
		if(minus){
			res.setX(-x);
		}
		else{
			res.setX(x);
		}
		return res ;
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

		//terme n impaire seulement  
		int n=33;

		float res=fact(n-1)/(power(2,n-1)*fact((n-1)/2)*fact((n-1)/2)*(n));
		int k=n-1;
		boolean b= true;
		while(k>=0){

			if (b==true){
				res=res*x;  
				b=false;
			}
			else{
				res=res*x+fact(k-1)/(power(2,k-1)*fact((k-1)/2)*fact((k-1)/2)*(k));
				b=true;
			}
			k=k-1;

		}
		return res;
	}
	
	public static float asin(float x){

		if(abs(x)<=0.72){
			return asindse(x);
		}
		else if(x>0.72){ 
			return PI/2-asindse(sqrt(1-power(x,2)));
		}
		else{
			return asindse(sqrt(1-power(x,2)))-PI/2;
		}

	}
	public static float atan(float x){
		int minus = 1 ;
		if (x<0){
			minus = -1 ;
		}
		if(x>1.1875){
			return minus*(PI/2-atanHermite(1/x));
		}
		else if (x>0.6875f){			
			return minus*(atanHermite((x -1f)/(1f+x)) + PI/4.0f);
		}
		else{
			return minus*atanHermite(x);
		}
		// if (x<0){
		// 	minus = -1 ;
		// }
		// if(x>1.1875){
		// 	return minus*(PI/2-atandse(1/x));
		// }
		// else if (x>0.6875f){			
		// 	return minus*(atandse((x -1f)/(1f+x)) + PI/4.0f);
		// }
		// else{
		// 	return minus*atandse(x);
		// }
	}
	public static float atanHermite(float x){
		float res=
        (power(x,55)/901120f)
        -(7f*power(x,54)/221184f)
        +(377f*power(x,53)/868352f)
        -(203f*power(x,52)/53248f)
        +(10049f*power(x,51)/417792f)
        -(11879f*power(x,50)/102400f)
        +(178321f*power(x,49)/401408f)
        -(68063f*power(x,48)/49152f)
        +(2751463f*power(x,47)/770048f)
        -(1454473f*power(x,46)/188416f )
        +(10371647f*power(x,45)/737280f)
        -(489259f*power(x,44)/22528f)
        +(5012527f*power(x,43)/176128f)
        -(1361617f*power(x,42)/43008f)
        +(5016623f*power(x,41)/167936f)
        -(489259f*power(x,40)/20480f)
        +(10355263f*power(x,39)/638976f)
        -(1454473f*power(x,38)/155648f)
        +(2767847f*power(x,37)/606208f)
        -(68063f*power(x,36)/36864f)
        +(170129f*power(x,35)/286720f)
        -(11879f*power(x,34)/69632f)
        +(18241f*power(x,33)/270336f)
        -(203f*power(x,32)/32768f)
        -(16007f*power(x,31)/507904f)
        -(7f*power(x,30)/122880f)
        +(565f*power(x,29)/16384f)
        -(power(x,27)/27f)
        +(power(x,25)/25f)
        -(power(x,23)/23f)
        +(power(x,21)/21f)
        -(power(x,19)/19f)
        +(power(x,17)/17f)
        -(power(x,15)/15f)
		+(power(x,13)/13f)
		-(power(x,11)/11f)
		+(power(x,9)/9f)
		-(power(x,7)/7f)
		+(power(x,5)/5f)
		-(power(x,3)/3f)
		+x;
        return res;

	}
	
	public static float atandse(float x){
		int n=326;
		float res=-1/327.F;
            //System.out.println("aa");
            //System.out.println(res);
		boolean b= true;
		float signe=-1;
		while(n>=0){
               // System.out.println(b);
              //  System.out.println(n);

			if (b==true){
				res=res*x;  
				b=false;
			}
			else{
				signe=signe*(-1);
				res=res*x+signe/(n);
				b=true;


			}
			n=n-1;

               //System.out.println(res);


		}   
		return res;
	}



}