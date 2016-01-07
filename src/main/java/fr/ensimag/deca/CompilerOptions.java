package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl41
 * @date 01/01/2016
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
	private boolean parse = false;
	private boolean verification =false;
	private boolean nocheck= false;
	private int registre =16;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement
		if (args.length==1){
			throw new CLIException("Aucun parametre rentree");
		}
		if(args.length==2 && args[1].equals("-b")){ //on check pour la bannière
			this.printBanner=true;
		}





        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        throw new CLIException("commande incorrect");
    }

    protected void displayUsage() {
        System.out.println("Voici les différents fonctions du compilateur:");
		System.out.println("-b affiche le nom de l'equipe, doit etre utiliser seul");
		System.out.print("-p apllique le parser et décompile celui-ci et l'affiche, doit etre suivi au moins d'un nom de fichier source ");
		System.out.println(" ne peux etre utilisé avec -v");
    	System.out.print("-v arrete decac après la vérification, ne produit pas de sortie, doit etre suivi au moins d'un nom de fichier source");
		System.out.print("ne peut estre suivit de -p");
		System.out.println("-n enleve les tests de débordements (arithméthique,mémoire,null");
		System.out.println("-r X limite l'utilisation des registres à R(X-1), nb: 4 <= X <= 16");
		System.out.println("-d active les traces de debug, activer plusieurs fois pour avoir plus de traces");
		System.out.println("-P si plusieurs fichiers sources lance la compilation en parallèle");
		System.out.println("-w affiche les warnings");
	}
}
