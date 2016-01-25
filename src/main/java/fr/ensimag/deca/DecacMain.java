package fr.ensimag.deca;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl41
 * @date 01/01/2016
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {

        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("Le groupe est  ProjetGL41");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            System.err.println("Il n'y a pas de fichier source");
			options.displayUsage();
			System.exit(1);
        }
        if (options.getParallel()) {
            int nbProc = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(nbProc);
            List<Future<?>> results = new LinkedList<Future<?>>();
            for (File source : options.getSourceFiles()) {
                results.add(executor.submit(new DecacCompiler(options, source)));
            }
            for(Future<?> future : results){
                try {
                    if((Boolean) future.get()){
                        error = true;
                    }
                } catch (InterruptedException e) {
                    System.err.println("La compilation a été arrétée");
                } catch (ExecutionException e) {
                    System.err.println("Un problème est intervenu dans l'éxécution");
                }
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
