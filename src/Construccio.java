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

    public int puntuar(){return _regions.size();}

    public List<Jugador> quiPuntua(){
        Iterator it = _seguidors.entrySet().iterator();
        int maxSeg = 0;
        ArrayList<Jugador> puntuadors = new ArrayList<>();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if((Integer)pair.getValue()>maxSeg){
                maxSeg = (Integer)pair.getValue();
                puntuadors = new ArrayList<>();
                puntuadors.add((Jugador)pair.getKey());
            }
            else if((Integer)pair.getValue()==maxSeg) puntuadors.add((Jugador)pair.getKey());
        }
        return puntuadors;
    }

    public void fusionar(Construccio c){
        _regions.addAll(c.get_regions());
        for (Map.Entry pair : c.get_seguidors().entrySet()) {
            Jugador clau = (Jugador) pair.getKey();
            if(_seguidors.containsKey(clau)){
                _seguidors.put(clau,_seguidors.get(clau)+(Integer)pair.getValue());
            }
            else _seguidors.put(clau,(Integer)pair.getValue());
        }
        _pendents.retainAll(c.get_pendents());
    }

    public void addRegio(Regio regio){
        _regions.add(regio);
        _pendents.remove(regio.get_peça());
        regio.get_peça().get_adjacents().stream()
                .filter(adj -> adj !=null)
                .forEach(p -> _pendents.add(p.hashCode()));
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
    
    public Set<Integer> get_pendents(){
        return _pendents;
    }
    
    public boolean ocupada(){
        return _ocupada;
    }
}
