import java.util.*;

/** @file Tauler.java
    @brief Un Tauler de joc dinàmic com si fos un joc de taula. Conté la lògica per afegir peces i controlar tot el que hi ha sobre el tauler.
*/
/** @class Tauler
    @brief Un Tauler de joc dinàmic com si fos un joc de taula. Conté la lògica per afegir peces i controlar tot el que hi ha sobre el tauler.
    @author Ferran Capallera
*/
public class Tauler {
    private Map<Integer,Peça> _tauler = new HashMap<>(); ///< Mapa de peces <hashCode,Peça> accés O(1)
    private Map<String, List<Construccio>> _connexions = new HashMap<>(); ///< Mapa de connexions. Cada clau és un tipus de Construcció (Cami, Vila, Monestir), el valor la llista.
    private Set<Integer> _disponibles = new HashSet<>(); ///< Set de hashCodes de les peces disponibles (on és vàlid jugar una peça).
    private int _maxX = 0; ///< x de la Peça amb y més gran.
    private int _minX = 0; ///< x de la Peça amb x més petita.
    private int _maxY = 0; ///< y de la Peça amb y més gran.
    private int _minY = 0; ///< y de la Peça amb y més petita.
    
    /** @invariant _disponibles.size() >= 0 /\  No hi ha construccions repetides */
    
    /** @brief Constructor de Tauler.
	@pre cert
	@post Inicialitza les estructures buides. */
    public Tauler(boolean camperols){
        _connexions.put("V",new ArrayList<>());
        _connexions.put("C",new ArrayList<>());
        _connexions.put("M", new ArrayList<>());
        if(camperols) _connexions.put("F", new ArrayList<>());
    }
    
    /** @brief Retorna l'estructura del Tauler de joc.
	@pre cert
	@post Retorna l'estructura del Tauler de joc. */
    public Map<Integer,Peça> getTauler(){
        return _tauler;
    }
    
    /** @brief Retorna la cota mínima de x.
	@pre cert
	@post Retorna la cota mínima de x. */
    public int getMinX(){
        return _minX;
    }
    
    /** @brief Retorna la cota màxima de y.
	@pre cert
	@post Retorna la cota màxima de y. */
    public int getMaxY(){
        return _maxY;
    }
    
    /** @brief Ens diu si el tauler conté una Peça.
	@pre cert
	@post Retorna cert si la Peça està al tauler, fals altrament. */
    public boolean contains(Peça peça){
        return _tauler.containsKey(peça.hashCode());
    }
    
    /** @brief Ens diu si el tauler conté un hashCode
	@pre cert
	@post Retorna cert si el hashCode està al tauler, fals altrament. */
    public boolean contains(Integer hash){
        return _tauler.containsKey(hash);
    }
    
    /** @brief Afegeix una Peça al tauler i vincula totes les estructures necessàries.
	@pre cert
	@post Afegeix una peça emplenant tots els seus atributs, actualitza les cotes, recalcula les peces disponibles, actualitza les construccions mirant
              si hi ha hagut una expansió, una fusió amb una altra construcció, o simplement s'ha creat una construcció nova.*/
    public void afegirPeça(Peça peça, int x, int y){
        actualitzarCotes(x,y);
        peça.set_x(x);
        peça.set_y(y);
        _disponibles.remove(peça.hashCode());
        _tauler.put(peça.hashCode(),peça);
        
        List<Peça> adjacents = new ArrayList<>();
        int[] hashKeyAdj = {1,100,-1,-100};
        
        //Calculem les peces adjacents
        for(int i=0;i<4;i++){
            if(_tauler.containsKey(peça.hashCode()+hashKeyAdj[i])){
                Peça adj = _tauler.get(peça.hashCode()+hashKeyAdj[i]);
                adjacents.add(adj);
                adj.setPeçaAdjacent(peça,(i+2)%4);
            } else adjacents.add(null);
        }
        //Assignem les peces adjacents
        peça.set_adjacents(adjacents);
        
        //Mirem si al centre de la peça hi ha un monestir i el creem
        if(peça.centre()=='M'){
            Regio m = peça.getRegio(-1);
            Construccio c = new Monestir(m);
            ((Monestir)c).setPendents(this, peça);
            m.set_pertany(c);
            _connexions.get("M").add(c);
        }
        
        //Actualitzem les peces pendents dels possibles monestirs adjacents
        actualitzarMonestirs(peça);
        
        //Per cada peça adjacent:
        for(int i=0;i<4;i++){
            Regio r = peça.getRegio(i);
            //Si no hi ha peça adjacent afegim aquesta posició a _pendents.
            if(adjacents.get(i)==null) _disponibles.add(peça.hashCode()+hashKeyAdj[i]);
            //Si és un camp no actuem (CAMPS NO IMPLEMENTAT)
            if(r.get_codi()!='F'){
                //Cas no hi ha peça adjacent
                if(adjacents.get(i)==null){
                    //Si la regió no pertany a cap Construcció (nova)
                    if(r.get_pertany()==null){
                        //Creem la nova construcció
                        Construccio c;
                        //Nova Vila
                        if(r.get_codi()=='V'){
                            c = new Vila(r);
                            _connexions.get(""+r.get_codi()).add(c);
                            r.set_pertany(c);
                        }
                        //Nou camí
                        else if(r.get_codi()=='C'){
                            c = new Cami(r);
                            _connexions.get(""+r.get_codi()).add(c);
                            r.set_pertany(c);
                        }
                    }
                }
                //Hi ha una peça a la posició adjacent i
                else{
                    //La regió no pertany a cap Construcció (nova)
                    if(r.get_pertany()==null){
                        //Afegim la regió a la construcció adjacent (expansió)
                        Construccio c = adjacents.get(i).getRegio((i+2)%4).get_pertany();
                        c.addRegio(r);
                        r.set_pertany(c);
                    }
                    //La regió ja pertany a una construcció
                    else{
                        //Fusionem la regió nova amb la ja existent, eliminem la ja existent i ens quedem amb la fusionada.
                        Construccio a = r.get_pertany();
                        Construccio b = adjacents.get(i).getRegio((i+2)%4).get_pertany();
                        a.fusionar(b); //a és la regió fusionada
                        _connexions.get(""+r.get_codi()).remove(b); //no necessitem b
                    }
                }
            }
        }
    }
    
    /** @brief El jugador \p actual afageix un seguidor a la peça de (\p x,\p y) a la posició \p pos = {C,N,E,S,W}
	@pre la peça s'acaba d'afegir al taulell.
	@post s'afageix el seguidor del jugador \p actual a la Regió _regions[pos] /\ actual._seguidors-- */
    public void afegirSeguidor(int x, int y, int pos, Jugador actual){
        actual.utilitzarSeguidor();
        Peça peça = getPeça(x,y);
        Regio regio = peça.getRegio(pos-1);
        regio.setSeguidor(actual);
    }
    
    /** @brief Retorna una llista de les posicions vàlides on afegir un seguidor.
	@pre x,y formen una posició vàlida del tauler /\ actual != null.
	@post Retorna una llista d'Integers (booleans) on vàlida=1 i no vàlida=0 en l'ordre {C,N,E,S,W} */
    public List<Integer> seguidorsValids(int x, int y, Jugador actual){
        List<Integer> valids = new ArrayList<>();
        Peça peça = getPeça(x,y);
        for(int i=0;i<5;i++){
            Regio regio = peça.getRegio(i-1);
            //CAMPS NO IMPLEMENTAT
            if(regio==null || regio.get_codi()=='F') valids.add(0);
            else{
                if(!regio.get_pertany().ocupada()) valids.add(1);
                else valids.add(0);
            }
        }
        return valids;
    }

    /** @brief Retorna la Peça en la posició x,y.
	@pre cert
	@post Si x,y formen una posició vàlida retorna la Peça corresponent. Altrament retorna null. */
    public Peça getPeça(int x, int y){
        return _tauler.get(100*x+y);
    }
    
    /** @brief S'actualitzen les cotes límit del tauler.
	@pre cert
	@post maxX = max(x,maxX) /\ minX = min(x,minX) /\ maxY = max(y,maxY) /\ minY = min(y,minY) */
    private void actualitzarCotes(int x, int y){
        if(x < _minX) _minX = x;
        else if(x > _maxX) _maxX = x;
        if(y < _minY) _minY = y;
        else if(y > _maxY) _maxY = y;
    }
    
    /** @brief Retorna cert si colocar \p peça a la posició \p x , \p y és vàlid.
	@pre peça != null
	@post (x,y) ha de tenir almenys una peça adjacent colocada /\ cada peça adjacent coincidex amb el tipus de regió (Camp amb Camp, Vila amb Vila, Camí amb Camí). */
    public boolean jugadaValida(Peça peça, int x, int y){
        if(!_disponibles.contains(x*100+y)) return false;
        else{
            boolean retorn = true;
            int[] hashKeyAdj = {1,100,-1,-100};
            for(int i=0;i<4;i++){
                if(_tauler.containsKey(100*x+y+hashKeyAdj[i])){
                    if(!peça.esCompatible(_tauler.get(100*x+y+hashKeyAdj[i]), i)) retorn = false;
                }
            }
            return retorn;
        }
    }
    
    /** @brief S'actualitzen les puntuacions de tots els jugadors.
	@pre S'acaba de jugar una peça.
	@post Per cada construcció finalitzada, cada jugador que puntua rep els seus punts, rep els seguidors dins la construcció, s'elimina la construcció. */
    public void actualitzarPuntuacions(){
        Iterator it = _connexions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            List<Construccio> llista = (List<Construccio>)pair.getValue();
            Iterator<Construccio> iter = llista.iterator();
            while(iter.hasNext()){
                Construccio c = iter.next();
                //Si ja està completa (s'ha de puntuar)
                if(c.completada()){
                    List<Jugador> puntuadors = c.quiPuntua();
                    for(Jugador j : puntuadors) j.sumarPunts(c.puntuar());
                    c.tornarSeguidors();
                    iter.remove();
                }
            }
        }
    }
    
    /** @brief Es procedeix al recompte final tal com estipula a les regles del joc.
	@pre S'acaba de jugar l'última peça.
	@post Per cada construcció els jugadors reben els seus respectius punts (les viles compten la meitat) */
    public void recompteFinal(){
        Iterator it = _connexions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            List<Construccio> llista = (List<Construccio>)pair.getValue();
            for(Construccio c : llista){
                List<Jugador> puntuadors = c.quiPuntua();
                //Si és un Vila només puntua la meitat.
                int punts = ((c instanceof Vila) ? c.puntuar()/2 : c.puntuar());
                for(Jugador j : puntuadors) j.sumarPunts(punts);
                c.tornarSeguidors();
            }
        }
    }
    
    
    /** @brief Comprova si donada una \p peça, una posició (x,y) i un jugador \p actual, és vàlid afegir un seguidor {-1,C,N,E,S,W}.
	@pre La peça encara no s'ha colocat, càlcul de la cpu.
	@post Retorna cert si és vàlid colocar el seguidor, fals altrament. */
    public boolean seguidorValid(Peça peça, int posSeguidor, int x, int y, Jugador jugador){
        //Si no es vol afegir seguidor (cas -1) surt directament.
        if(posSeguidor==-1) return false;
        //No es pot afegir un seguidor a un encreuament
        else if(posSeguidor==0 && peça.centre()=='X') return false;
        //Si és un camp retorna fals (CAMP NO IMPLEMENTAT)
        else if(peça.getRegio(posSeguidor-1).get_codi()=='F') return false;
        //Sempre es pot afegir un seguidor a un Monestir
        else if(peça.getRegio(posSeguidor-1).get_codi()=='M') return true;
        //Altrament calcular
        else{
            int idRegio = peça.getRegio(posSeguidor-1).get_id();
            //Per cada peça adjacent de la regió (Ex: un camí que connecta el Nord amb l'Est).
            for(int i=0;i<4;i++){
                if(idRegio == peça.getRegio(i).get_id()){
                    int[] hashKeyAdj = {1,100,-1,-100};
                    Peça adjacent = _tauler.get(x*100+y+hashKeyAdj[i]);
                    //Si hi ha una peça adjacent la construcció no ha d'estar ocupada.
                    if(adjacent!=null){
                        Regio adj = adjacent.getRegio((i+2)%4);
                        Construccio c = adj.get_pertany();
                        if(c._ocupada) return false;
                    }
                }
            }
        }
        //Si no ha retornat res fins al moment tot OK.
        return true;
    }
    
    /** @brief Actualitza els monestirs si tenen la \p peça al seu {N,NE,E,SE,S,SW,W,NW}.
	@pre cert
	@post Treu la \p peça de la llista de pendents dels monestirs (si hi és). */
    private void actualitzarMonestirs(Peça peça){
        List<Construccio> monestirs = _connexions.get("M");
        for(Construccio m : monestirs) m.removePendent(peça.hashCode());
    }
    
    /** @brief Retorna el set de posicions disponibles.
	@pre cert
	@post Retorna el set de posicions disponibles. */
    public Set<Integer> get_disponibles(){
        return _disponibles;
    }

}
