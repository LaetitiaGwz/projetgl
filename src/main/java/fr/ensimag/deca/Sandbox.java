package fr.ensimag.deca;

/**
 * Created by matthieu on 20/01/16.
 */

class Socrate{
    void quiSuisJe(){
        System.out.print("socrate");
    }
}

class Platon extends Socrate{
    void quiSuisJe(){
        System.out.print("platon");
    }
}

class Aristote extends Socrate{

}


	class Sandbox {

		public static void main(String[] args) {
            Socrate s = new Platon();
            Aristote a = (Aristote) s;
		}
	}
