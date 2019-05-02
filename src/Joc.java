
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
public class Joc {
    static final String PROVES_SRC = "src/proves/";

    private Stack<Peça> _peces;
    private ArrayList<Jugador> _jugadors;
    private Tauler _tauler;
    private int _torn;
    private boolean _jugat;
    
    public Joc(String arxiu){
        _peces = new Stack<Peça>();
        _jugadors = new ArrayList<Jugador>();
        _tauler = new Tauler(false);
        _torn = -1;
        _jugat = false;
        this.init(arxiu);
        
        
    }
    
    

    public void init(String arxiu){
        File f = new File(PROVES_SRC+arxiu+".txt");
        HashMap<String,Integer> peces = new HashMap<>();
        int n = 0;
        boolean error = false;
        try {
            Scanner lector = new Scanner(f);

            //Llegim els jugadors
            if(lector.hasNext() && lector.next().equals("nombre_jugadors")){
                int njugs = lector.nextInt();
                for(int i=0;i<njugs;i++) _jugadors.add(new Jugador(i));
                if(lector.hasNext() && lector.next().equals("jugadors_cpu")){
                    while(lector.hasNextInt()){
                        int j_cpu = lector.nextInt();
                        _jugadors.get(j_cpu-1).setCpu();
                    }
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
                _tauler.afegirPeça(p,0,0);
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
                    _peces.push(p);
                }
            }
            Collections.shuffle(_peces);
        } else System.out.println("Error");
    }
    
    public Peça peekPila(){
        return _peces.peek();
    }
    
    public Peça popPila(){
        return _peces.pop();
    }
    
    public Stack<Peça> getPila(){
        return _peces;
    }
    
    public Tauler getTaulaJoc(){
        return _tauler;
    }
    
    public int getTorn(){
        return _torn % _jugadors.size();
    }
    
    public void jugar(){
        //Avança un torn i deixa jugar al seguent jugador
        _torn ++;
        if(_jugadors.get(_torn % _jugadors.size()).get_cpu()){
            //Jugar la CPU
        }
        else{
            //Jugar amb jugador humà, ergo, no fer res.
        }      
    }
    
    public int getnJugadors(){
        return _jugadors.size();
    }
    
    public Jugador jugadorN(int n){
        return _jugadors.get(n);
    }
}
