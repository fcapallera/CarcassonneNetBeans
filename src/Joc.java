
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** @file Joc.java
    @brief Classe Joc, conté la informació dels Jugador, el tauler de joc. En aquesta classe hi ha el bucle principal del joc i la comunicació amb l'interfície gràfica.
*/
/** @class Joc
    @brief Classe Joc, conté la informació dels Jugador, el tauler de joc. En aquesta classe hi ha el bucle principal del joc i la comunicació amb l'interfície gràfica.
    @author Adrià Orellana
*/
public class Joc {

    private Stack<Peça> _peces; ///< Stack barrejat de les peces
    private List<Jugador> _jugadors; ///< Llista de Jugador
    private final Tauler _tauler; ///< Objecte Tauler
    private int _torn; ///< Índex del torn
    private final CarcassonneGUI _gui; ///< Interfície Gràfica
    
    /** @invariant _torn >= 0 /\ _torn < _jugadors.size() /\ _gui != null /\ _tauler != null */
    
    /** @brief Constructor de Joc a partir de la GUI.
	@pre --
	@post inicialitzem estructures buides i un tauler buit. */
    public Joc(CarcassonneGUI gui){
        _peces = new Stack<Peça>();
        _jugadors = new ArrayList<Jugador>();
        _tauler = new Tauler(false);
        _torn = 0;
        _gui = gui;
    }
    
    /** @brief Obté una llista de jugadors ordenats per puntuació.
	@pre --
	@post Retorna la llista de jugadors reordenada per puntuació. */
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
    
    /** @brief Torna l'element de sobre la pila de peces.
	@pre --
	@post Torna l'element de sobre la pila de peces. */
    public Peça peekPila(){
        return _peces.peek();
    }
    
    /** @brief Torna l'element de sobre la pila de peces i el treu.
	@pre --
	@post _peces.size() = peces.size()-1. */
    public Peça popPila(){
        return _peces.pop();
    }
    
    /** @brief Torna la pila de peces.
	@pre --
	@post Torna la pila de peces. */
    public Stack<Peça> getPila(){
        return _peces;
    }
    
    /** @brief Retorna l'objecte tauler.
	@pre --
	@post Retorna l'objecte tauler. */
    public Tauler getTaulaJoc(){
        return _tauler;
    }
    
    /** @brief Retorna el torn.
	@pre --
	@post Retorna el torn. */
    public int getTorn(){
        return _torn % _jugadors.size();
    }
    
    /** @brief Juga un torn del joc.
	@pre --
	@post Si juga un jugador humà es fa per GUI, altrament el cpu fa una tirada. */
    public void jugar(){
        //Avança un torn i deixa jugar al seguent jugador
        
        if(_jugadors.get(_torn).get_cpu()){
            _jugadors.get(_torn).tirarCpu(_tauler,_peces.pop(),this);
            passarTorn();
            acabarTorn();
        }
        else{
            //Jugar amb jugador humà, ergo, no fer res.
        }
        _tauler.actualitzarPuntuacions();
    }
    
    /** @brief Acaba la partida i fa el recompte final.
	@pre l'stack està buit
	@post S'ha acabat la partida. Cada jugador rep la puntuació de les construccions incompletes. */
    public void acabarPartida(){
        _tauler.recompteFinal();
    }
    
    /** @brief S'acaba el torn, juga el següent jugador.
	@pre el jugador ha tirat o ha passat torn.
	@post S'actualitza l'interfície gràfica, es crida el torn del següent jugador. */
    public void acabarTorn(){
        _gui.acabarTorn();
    }
    
    /** @brief S'actualitza el tauler de joc a l'interfície gràfica.
	@pre cert
	@post S'actualitza el tauler del joc amb les noves peces, afegint o traient seguidors, etc. */
    public void actualitzarTaulerGUI(int x, int y){
        System.out.println("Tauler ("+_gui.getXTauler(x)+","+_gui.getYTauler(y)+")");
         _gui.refreshTauler(_gui.getXTauler(x), _gui.getYTauler(y));
    }
    
    /** @brief Retorna el nombre de jugadors.
	@pre cert
	@post Retorna el nombre de jugadors. */
    public int getnJugadors(){
        return _jugadors.size();
    }
    
    /** @brief Retorna el Jugador n-èssim.
	@pre n >= 0 /\ n <= nJugadors.
	@post Retorna el Jugador n-èssim. */
    public Jugador jugadorN(int n){
        return _jugadors.get(n);
    }
    
    /** @brief S'augmenta un torn.
	@pre s'ha jugat.
	@post Augmenta el torn (si ha jugat el jugador n-1 comença el jugador 0) */
    public void passarTorn(){
        _torn = (_torn+1)%_jugadors.size();
    }
    
    /** @brief Retorna la GUI
	@pre cert
	@post Retorna la GUI */
    public CarcassonneGUI get_gui(){
        return _gui;
    }
    
    /** @brief Assigna un Stack de peces \p peces .
	@pre cert
	@post Assigna un Stack de peces \p peces . */
    public void set_peces(Stack<Peça> peces){
        _peces = peces;
    }
    
    /** @brief Assigna una llista de jugadors \p jugadors .
	@pre cert
	@post _jugadors = \p jugadors . */
    public void set_jugadors(List<Jugador> jugadors){
        _jugadors = jugadors;
    }
    
}
