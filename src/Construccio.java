import java.util.*;
import java.util.stream.Stream;

public class Construccio {
    protected Set<Regio> _regions = new HashSet<>();
    protected Map<Jugador,Integer> _seguidors = new HashMap<>();
    protected Set<Integer> _pendents = new HashSet<>();
    protected boolean _ocupada = false;

    public Construccio(Regio regio){
        addRegio(regio);
        regio.set_pertany(this);    
    }

    public int puntuar(){
        return _regions.size();
    }

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
    
    public void tornarSeguidors(){
        for(Map.Entry<Jugador,Integer> entry : _seguidors.entrySet()){
            entry.getKey().tornarSeguidor(entry.getValue());
        }
    }

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

    public void addRegio(Regio regio){
        _regions.add(regio);
        _pendents.remove(regio.get_peça().hashCode());
        Peça actual = regio.get_peça();
        int[] hashKeyAdj = {1,100,-1,-100};
        for(int i = 0;i<4;i++){
            if(actual.getRegio(i)==regio && actual.getPeçaAdjacent(i)==null) _pendents.add(actual.hashCode()+hashKeyAdj[i]);
        }
    }
    
    public void addSeguidor(Jugador jugador){
        _seguidors.put(jugador, _seguidors.getOrDefault(jugador, 0)+1);
        _ocupada = true;
    }
    
    public void addPendent(Integer pendent){
        _pendents.add(pendent);
    }
    
    public void removePendent(Integer pendent){
        _pendents.remove(pendent);
    }

    public boolean conteRegio(Regio regio){
        return _regions.contains(regio);
    }

    public Set<Regio> get_regions() {
        return _regions;
    }

    public Map<Jugador, Integer> get_seguidors() {
        return _seguidors;
    }
    
    public boolean completada(){
        return _pendents.size() == 0;
    }
    
    public Set<Integer> get_pendents(){
        return _pendents;
    }
    
    public void set_pendents(Set<Integer> pendents){
        _pendents = pendents;
    }
    
    public boolean ocupada(){
        return _ocupada;
    }
}
