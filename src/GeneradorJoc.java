
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class GeneradorJoc {
    String _arxiu;
    static final String PROVES_SRC = "src/proves/";
    
    public GeneradorJoc(String arxiu){
        _arxiu = arxiu;
    }
    
    public Joc generar(CarcassonneGUI gui){
        File f = new File(PROVES_SRC+_arxiu+".txt");
        HashMap<String,Integer> peces = new HashMap<>();
        Stack<Peça> pecesJoc = new Stack<>();
        Joc joc = new Joc(gui);
        List<Jugador> jugadors = new ArrayList<>();
        int n = 0;
        boolean error = false;
        try {
            Scanner lector = new Scanner(f);

            //Llegim els jugadors
            if(lector.hasNext() && lector.next().equals("nombre_jugadors")){
                int njugs = lector.nextInt();
                for(int i=0;i<njugs;i++) jugadors.add(new Jugador(i));
                if(lector.hasNext() && lector.next().equals("jugadors_cpu")){
                    while(lector.hasNextInt()){
                        int j_cpu = lector.nextInt();
                        jugadors.get(j_cpu-1).setCpu();
                    }
                    joc.set_jugadors(jugadors);
                } else error = true;
            } else error = true;


            //Llegim les peces
            if(lector.hasNext() && lector.next().equals("rajoles")){
                String codi;
                while(!(codi = lector.next()).equals("#")) {
                    Integer nombre = lector.nextInt();
                    peces.put(codi, nombre);
                }
            } else error = true;

            //Llegim la rajola inicial
            if(lector.hasNext() && lector.next().equals("rajola_inicial")){
                String inicial = lector.next();
                peces.put(inicial,peces.get(inicial)-1);
                Peça p = new Peça(inicial);
                n += p.afegirRegions(n);
                joc.getTaulaJoc().afegirPeça(p,0,0);
            } else error = true;

        } catch (FileNotFoundException e){
            System.err.println(e);
        }

        //Afegim les peces al stack
        if(!error){
            Iterator it = peces.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                for(int i=0;i<(Integer)pair.getValue();i++){
                    Peça p = new Peça((String)pair.getKey());
                    n += p.afegirRegions(n);
                    pecesJoc.push(p);
                }
            }
            Collections.shuffle(pecesJoc);
            joc.set_peces(pecesJoc);
        } else System.out.println("Error");
        
        return joc;
    }
    
}
