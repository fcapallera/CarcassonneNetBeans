
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
public class Joc {

    private Stack<Peça> _peces;
    private List<Jugador> _jugadors;
    private Tauler _tauler;
    private int _torn;
    private boolean _jugat;
    private CarcassonneGUI _gui;
    
    public Joc(CarcassonneGUI gui){
        _peces = new Stack<Peça>();
        _jugadors = new ArrayList<Jugador>();
        _tauler = new Tauler(false);
        _torn = 0;
        _jugat = false;
        _gui = gui;
    }
    
    public ArrayList<Jugador> getJugadorsPuntuacio(){
        ArrayList<Jugador> res = new ArrayList<Jugador>();
        for(int i = 0; i < _jugadors.size();i++){
            int j = 0;
            boolean trobat = false;
            while(!trobat && j < res.size()){
                if(res.get(j).getPunts()>= _jugadors.get(i).getPunts()){
                    j++;
                }
                else{
                    trobat = true;
                }
            }
            if(trobat){
                res.add(j,_jugadors.get(i));
            }
            else{
                res.add(res.size(),_jugadors.get(i));
            }
        }
        return res;
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
        
        if(_jugadors.get(_torn).get_cpu()){
            _jugadors.get(_torn).tirarCpu(_tauler,_peces.pop(),this);
            passarTorn();
        }
        else{
            //Jugar amb jugador humà, ergo, no fer res.
        }
        _tauler.actualitzarPuntuacions();
    }
    
    public void acabarPartida(){
        _tauler.recompteFinal();
    }
    
    public void acabarTorn(){
        _gui.acabarTorn();
    }
    
    public void actualitzarTaulerGUI(int x, int y){
        System.out.println("Tauler ("+_gui.getXTauler(x)+","+_gui.getYTauler(y)+")");
         _gui.refreshTauler(_gui.getXTauler(x), _gui.getYTauler(y));
    }
    
    public int getnJugadors(){
        return _jugadors.size();
    }
    
    public Jugador jugadorN(int n){
        return _jugadors.get(n);
    }
    
    public void passarTorn(){
        _torn = (_torn+1)%_jugadors.size();
    }
    
    public CarcassonneGUI get_gui(){
        return _gui;
    }
    
    public void set_peces(Stack<Peça> peces){
        _peces = peces;
    }
    
    public void set_jugadors(List<Jugador> jugadors){
        _jugadors = jugadors;
    }
    
}
