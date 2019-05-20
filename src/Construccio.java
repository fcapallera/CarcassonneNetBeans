/** @file Construccio.java
    @brief Classe Construccio
*/
import java.util.*;
import java.util.stream.Stream;

/** @class Construccio
    @brief Representació d'un conjunt de Regions on un Jugador pot posar seguidors. Quan es completa la Construcció, es retornen els seguidors i es puntua per el Jugador que els hi ha posat.
    @author 
*/
public class Construccio {
    protected Set<Regio> _regions = new HashSet<>();///< Set de Regions
    protected Map<Jugador,Integer> _seguidors = new HashMap<>();///< Map de seguidors
    protected Set<Integer> _pendents = new HashSet<>();///< 
    protected boolean _ocupada = false;///< Cert si la Construccio ja té seguidors d'un altre Jugador
    
    /** @brief Constructor de Construccio
	@pre 
	@post  */
    public Construccio(Regio regio){
        addRegio(regio);
        regio.set_pertany(this);    
    }
    
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        
        if(!(other instanceof Construccio)) return false;
        
        Construccio c = (Construccio)other;
        
        return _regions.equals(c.get_regions());
    }
    
    /** @brief Retorna el tamany del Set de Regions
	@pre 
	@post  */
    public int puntuar(){
        return _regions.size();
    }
    
    /** @brief 
	@pre 
	@post  */
    public List<Jugador> quiPuntua(){
        int maxSeg = 0;
        ArrayList<Jugador> puntuadors = new ArrayList<>();
        for(Map.Entry<Jugador,Integer> entry : _seguidors.entrySet()){
            if(entry.getValue()>maxSeg){
                maxSeg = entry.getValue();
                puntuadors.clear();
                puntuadors.add(entry.getKey());
            }
            else if(entry.getValue()==maxSeg) puntuadors.add(entry.getKey());
        }
        return puntuadors;
    }
    
    /** @brief Elimina els seguidors de la Construccio i reinicialitza els contenidors
	@pre 
	@post  */
    public void tornarSeguidors(){
        for(Map.Entry<Jugador,Integer> entry : _seguidors.entrySet()){
            entry.getKey().tornarSeguidor(entry.getValue());
            entry.getKey().removeConstruccio(this);
        }
        for(Regio regio : _regions){
            regio.set_pertany(null);
            regio.esborrarSeguidor();
        }
    }
    
    /** @brief Fusiona dues Construccions
	@pre 
	@post  */
    public void fusionar(Construccio c){
        _regions.addAll(c.get_regions());
        if(c.ocupada()) _ocupada = true;
        for(Regio r : c.get_regions()) r.set_pertany(this);
        
        for (Map.Entry pair : c.get_seguidors().entrySet()) {
            Jugador clau = (Jugador) pair.getKey();
            if(_seguidors.containsKey(clau)){
                _seguidors.put(clau,_seguidors.get(clau)+(Integer)pair.getValue());
            }
            else _seguidors.put(clau,(Integer)pair.getValue());
        }
        _pendents.addAll(c.get_pendents());
        for(Regio r : _regions) _pendents.remove(r.get_peça().hashCode());
        for(Integer i : _pendents) System.out.println(i);
    }
    
    /** @brief Afegeix una Regio a la Construccio
	@pre 
	@post  */
    public void addRegio(Regio regio){
        _regions.add(regio);
        _pendents.remove(regio.get_peça().hashCode());
        Peça actual = regio.get_peça();
        int[] hashKeyAdj = {1,100,-1,-100};
        for(int i = 0;i<4;i++){
            if(actual.getRegio(i)==regio && actual.getPeçaAdjacent(i)==null) _pendents.add(actual.hashCode()+hashKeyAdj[i]);
        }
    }
        
    /** @brief Afegeix un Jugador al Map de Seguidors. Si el jugador ja és al Map, es suma+1 als seguidors que té a la Construccio
	@pre --
	@post  */
    public void addSeguidor(Jugador jugador){
        _seguidors.put(jugador, _seguidors.getOrDefault(jugador, 0)+1);
        _ocupada = true;
        jugador.addConstruccio(this);
    }
    
    /** @brief 
	@pre 
	@post  */
    public void addPendent(Integer pendent){
        _pendents.add(pendent);
    }
    
    /** @brief 
	@pre 
	@post  */
    public void removePendent(Integer pendent){
        _pendents.remove(pendent);
    }
    
    /** @brief Retorna cert si la Construccio conté la Regio
	@pre 
	@post  */
    public boolean conteRegio(Regio regio){
        return _regions.contains(regio);
    }
    
    /** @brief Retorna el Set de Regions
	@pre 
	@post  */
    public Set<Regio> get_regions() {
        return _regions;
    }
    
    /** @brief Retorna el Map que representa els Seguidors de la Construcció
	@pre 
	@post  */
    public Map<Jugador, Integer> get_seguidors() {
        return _seguidors;
    }
    
    /** @brief 
	@pre 
	@post  */
    public boolean completada(){
        return _pendents.size() == 0;
    }
    
    /** @brief 
	@pre 
	@post  */
    public Set<Integer> get_pendents(){
        return _pendents;
    }
    
    /** @brief 
	@pre 
	@post  */
    public void set_pendents(Set<Integer> pendents){
        _pendents = pendents;
    }
    
    /** @brief Retorna el Boolean _ocupada
	@pre _ocupada!=null;
	@post retorna _ocupada */
    public boolean ocupada(){
        return _ocupada;
    }
}
