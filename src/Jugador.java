
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** @file Jugador.java
    @brief Classe Jugador
*/
/** @class Jugador
    @brief Informació d'un Jugador i lògica per la tirada CPU.
    @author Ferran Capallera
*/
public class Jugador {
    private int _id;///< Id del Jugador
    private String _nom;///< Nom del Jugador
    private boolean _cpu = false;///< Cert si el jugador no és humà
    private int _punts = 0;///< Punts del Jugador
    private int _seguidors = 7;///< Nombre de seguidors restants d'un Jugador
    private Map<String, List<Construccio>> _construccions = new HashMap<>();
    
    /** @brief Constructor de Jugador
	@pre --
	@post Set _id = _id */
    public Jugador(int _id) {
        this._id = _id;
        _construccions.put("V",new ArrayList<>());
        _construccions.put("C",new ArrayList<>());
        _construccions.put("M", new ArrayList<>());
    }
    
    
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        
        if(!(other instanceof Jugador)) return false;
        
        Jugador j = (Jugador)other;
        
        return _id == j.getId();
    }
    
    /** @brief Posa l'atribut CPU a cert
	@pre --
	@post Set _cpu = true */
    public void setCpu() {
        this._cpu = true;
    }
    
    /** @brief Retorna cert si el jugador és CPU
	@pre --
	@post Retorna _cpu */
    public boolean get_cpu(){
        return _cpu;
    }
    
    /** @brief Retorna la Id del jugador
	@pre --
	@post Retorna _id */
    public int getId(){
        return _id;
    }
    
    /** @brief Representa la utilització d'un seguidor
	@pre 
	@post  */
    public void utilitzarSeguidor(){
        _seguidors -= 1;
    }
    
    /** @brief retorna \p s seguidors al jugador.
	@pre s'ha completat una regió.
	@post seguidors = seguidors + \p s. */
    public void tornarSeguidor(int s){
        _seguidors += s;
    }
    
    /** @brief suma \p punts al jugador.
	@pre cert
	@post _punts += \p punts. */
    public void sumarPunts(int punts){
        _punts += punts;
    }
    
    /** @brief Retorna els punts del Jugador
	@pre --
	@post Retorna _punts */
    public int getPunts(){
        return _punts;
    }
    
    /** @brief Retorna els seguidors del Jugador
	@pre --
	@post Retorna _seguidors */
    public int getSeguidors(){
        return _seguidors;
    }
    
    /** @brief Algoritme del càlcul de la millor jugada.
	@pre és el torn del jugador /\ el jugador és cpu
	@post Si troba la millor jugada fa una tirada, si no troba cap jugada bona o no pot jugar passa torn. */
    public void tirarCpu(Tauler tauler, Peça peça, Joc joc){
        Set<Integer> disponibles = tauler.get_disponibles();
        Tirada millor = new Tirada((Posicio)null);
        int heurMillor = 0;
        
        for(Integer disponible : disponibles){
            Posicio actual = new Posicio(disponible);
            Tirada tirada;
            for(int i=0;i<4;i++){
                if(tauler.jugadaValida(peça, actual._x, actual._y)){
                    for(int j=-1;j<5;j++){
                        tirada = new Tirada(actual,i,j);
                        if(j==-1 || tauler.seguidorValid(peça, j, actual._x, actual._y, this)){
                            int heurActual = heuristica(tauler,tirada,peça);
                            if(heurActual >= heurMillor){
                                heurMillor = heurActual;
                                millor = new Tirada(tirada);
                            }
                        }
                    }
                }
                peça.rotarClockWise();
            }
        }
        peça.rotarFins(millor.rotacio);
        if(millor.posicio!=null){
            System.out.println("La gui ha tirat: ("+millor.posicio._x+","+millor.posicio._y+") amb heuristica: "+heurMillor);
            joc.actualitzarTaulerGUI(millor.posicio._x, millor.posicio._y);
            tauler.afegirPeça(peça, millor.posicio._x, millor.posicio._y);
            if(millor.seguidor>-1) tauler.afegirSeguidor(millor.posicio._x, millor.posicio._y, millor.seguidor, this);
        }
        joc.acabarTorn();
    }
    
    /** @brief Calcula l'heurística d'una tirada en concret.
	@pre la tirada és vàlida /\ 
	@post Retorna l'heurística d'una jugada en concret. */
    private int heuristica(Tauler tauler, Tirada tirada, Peça peça){
        int heuristica = 0;
        int heuristicaSeguidors = 0;
        int x = tirada.posicio._x;
        int y = tirada.posicio._y;
        int seguidor = tirada.seguidor;
        int[] hashKeyAdj = {1,100,-1,-100};
        int tornats = 0;
        
        //1. Calculem l'heurística sense tenir en compte si afegim seguidor
        
        //Completa un monestir
        for(Construccio c : _construccions.get("M")){
            Set<Integer> pendents = c.get_pendents();
            if(pendents.contains(100*x+y)){
                heuristica += 10 - pendents.size();
                if(pendents.size()==1) tornats++;
            }
        }
        
        //Expandeix construccions que són nostres
        Set<Construccio> explorades = new HashSet<>();
        for(int i=0;i<4;i++){
            if(peça.getRegio(i).get_codi()!='F'){
                Peça adjacent = tauler.getTauler().get(x*100+y+hashKeyAdj[i]);
                if(adjacent!=null){
                    Construccio c = adjacent.getRegio((i+2)%4).get_pertany();
                    if(!explorades.contains(c)){
                        if(c.quiPuntua().contains(this)){
                            heuristica += c.puntuar();
                            if(c.get_pendents().contains(x*100+y) && c.get_pendents().size()==1) tornats += c.get_seguidors().get(this);
                            if(peça.getRegio(i).get_codi()=='V') heuristica += (2 + 2*(adjacent.getRegio((i+2)%4).hiHaEscut() ? 1 : 0));
                            else if(peça.getRegio(i).get_codi()=='C') heuristica += 1;
                        }
                        else if(c.ocupada())
                            heuristica -= c.puntuar()/2;
                        explorades.add(c);
                    }
                }
            }
        }
        //Ara comprovarem si afegint un seguidor tenim benefici extra
        if(seguidor!=-1){
            if(_seguidors == 1 && tornats == 0) heuristicaSeguidors -= 100;
            else{
                //Vol colocar un monestir
                if(seguidor==0 && peça.getRegio(-1).get_codi()=='M'){
                    heuristicaSeguidors += 3;
                    int[] hashKeyMonestir = {100,101,1,-99,-100,-101,-1,99};
                    for(int p : hashKeyMonestir){
                        if(tauler.getTauler().containsKey(x*100+y+p)) heuristicaSeguidors++;
                    }
                }
                
                Regio triada = peça.getRegio(seguidor-1);
                explorades = new HashSet<>();
                boolean comprovada = false;
                for(int i=0;i<4;i++){
                    if(triada.equals(peça.getRegio(i))){
                        Peça adjacent = tauler.getTauler().get(x*100+y+hashKeyAdj[i]);
                        if(adjacent!=null){
                            Construccio c = adjacent.getRegio((i+2)%4).get_pertany();
                            if(!explorades.contains(c)){
                                if(!c._ocupada){
                                    heuristicaSeguidors += c.puntuar();
                                    if(c.get_pendents().size()==1) heuristicaSeguidors += 3;
                                }
                            }
                        }
                        else{
                            if(!(_seguidors < 3 && tornats == 0) && !comprovada){
                                comprovada = true;
                                if(peça.getRegio(i).get_codi()=='V') heuristicaSeguidors += (2 + 2*(triada.hiHaEscut() ? 1 : 0));
                                else if(peça.getRegio(i).get_codi()=='C') heuristicaSeguidors += 1;
                            }
                        }

                    }
                }
            }
            //Si afegir un seguidor no ens dona cap bonus val més que no l'afegim (penalització)
            if(heuristicaSeguidors == 0) heuristica -= 10;
        }
        
        //El fet d'afegir un seguidor ens penalitza una mica. Quantes més peces ens tornin en una tirada més bonus. (Ens dona agilitat de joc)
        return heuristica + (heuristicaSeguidors-1) + tornats * 2;
    }
    
    
    /** @brief Ens diu si aquest jugador té algun seguidor en la construcció \p c.
	@pre c != null 
	@post Retorna cert si aquest jugador té un seguidor a la construcció, fals altrament. */
    public boolean teConstruccio(Construccio c){
        if(c instanceof Cami) return _construccions.get("C").contains(c);
        else if(c instanceof Vila) return _construccions.get("V").contains(c);
        else if(c instanceof Monestir) return _construccions.get("M").contains(c);
        else return false;
    }
    
    /** @brief Ens diu si aquest jugador té algun seguidor en la construcció \p c.
	@pre c != null 
	@post Retorna cert si aquest jugador té un seguidor a la construcció, fals altrament. */
    public void addConstruccio(Construccio c){
        if(c instanceof Cami) _construccions.get("C").add(c);
        else if(c instanceof Vila) _construccions.get("V").add(c);
        else if(c instanceof Monestir) _construccions.get("M").add(c);
    }
    
    
    /** @brief Elimina la construcció \p c de les construccions del jugador.
	@pre cert
	@post nConstruccions = nConstruccions -1, si la construcció existeix. */
    public void removeConstruccio(Construccio c){
        if(c instanceof Cami) _construccions.get("C").remove(c);
        else if(c instanceof Vila) _construccions.get("V").remove(c);
        else if(c instanceof Monestir) _construccions.get("M").remove(c);
    }
    
    /** @class Tirada
        @brief Classe auxiliar per computar la CPU. Conté la informació bàsica d'una tirada. (decisió)
        @author Ferran Capallera
    */
    private class Tirada {
        public Posicio posicio; ///< Posicio on es jugarà la peça.
        public int rotacio; ///< Número de vegades que s'ha rotat a partir de la rotació inicial.
        public int seguidor; ///< Posició on afegirem el seguidor {C,N,E,S,W}
        
        /** @brief Crea una tirada a partir d'una Posició.
            @pre cert
            @post Assigna la Posició, la resta d'atributs s'inicialitzen a null. */
        public Tirada(Posicio posicio){
            this.posicio = posicio;
            this.rotacio = 0;
            this.seguidor = 0;
        }
        
        /** @brief Constructor de Tirada amb tots els paràmetres.
            @pre cert
            @post Crea una Tirada amb tots els atributs inicialitzats. */
        public Tirada(Posicio posicio, int rotacio, int seguidor){
            this.posicio = posicio;
            this.rotacio = rotacio;
            this.seguidor = seguidor;
        }
        
        
        /** @brief Constructor de còpia a partir d'una altra Tirada.
            @pre cert
            @post Genera una nova tirada a partir d'una altra. (clone) */
        public Tirada(Tirada tirada){
            this.seguidor = tirada.seguidor;
            this.rotacio = tirada.rotacio;
            this.posicio = new Posicio(tirada.posicio._x,tirada.posicio._y);
        }
        
        /** @brief Dona valor a l'atribut rotacio.
            @pre cert
            @post rotacio = \p rot. */
        public void set_rotacio(int rot){
            rotacio = rot;
        }
        
        /** @brief Dona valor a l'atribut rotacio.
            @pre cert
            @post rotacio = \p rot. */
        public void set_seguidor(int seguidor){
            this.seguidor = seguidor;
        }
    }
    
    /** @class Posicio
        @brief Classe auxiliar per computar la CPU. Transforma el hashCode d'una peça en la seva x i y.
        @author Ferran Capallera
    */
    private class Posicio {
        public int _x; ///< Coordenada x
        public int _y; ///< Coordenada y
        
        /** @brief Constructor de còpia (x i y).
            @pre cert
            @post Crea una nova posició amb la x i la y. */
        public Posicio(int x, int y){
            _x = x;
            _y = y;
        }
        
        /** @brief Constructor de Posició a partir d'un hashCode.
            @pre cert
            @post Crea una nova Posició calculant la x i la y del hashCode. */
        public Posicio(Integer hash){
            double xAppr = hash/100.0;
            _x = (int) Math.round(xAppr);
            _y = hash - _x*100;
        }
    }
}
