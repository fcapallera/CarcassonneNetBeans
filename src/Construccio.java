import java.util.*;

public class Construccio {
    protected Set<Peça> _peces = new HashSet<>();
    protected HashMap<String,Integer> _seguidors = new HashMap<>();
    protected HashSet<Integer> _pendents = new HashSet<>();

    public Construccio(Peça peça){
        _peces.add(peça);
    }

    public int puntuar(){return _peces.size();}

    public List<String> quiPuntua(){
        Iterator it = _seguidors.entrySet().iterator();
        int maxSeg = 0;
        ArrayList<String> puntuadors = new ArrayList<>();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if((Integer)pair.getValue()>maxSeg){
                maxSeg = (Integer)pair.getValue();
                puntuadors = new ArrayList<>();
                puntuadors.add((String)pair.getKey());
            }
            else if((Integer)pair.getValue()==maxSeg) puntuadors.add((String)pair.getKey());
        }
        return puntuadors;
    }

    public void fusionar(Construccio c){
        _peces.addAll(c.get_peces());
        Iterator it = c.get_seguidors().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            String clau = (String) pair.getKey();
            if(_seguidors.containsKey(clau)){
                _seguidors.put(clau,_seguidors.get(clau)+(Integer)pair.getValue());
            }
            else _seguidors.put(clau,(Integer)pair.getValue());
        }
    }

    public void addPeça(Peça peça){
        _peces.add(peça);

    }

    public boolean contePeça(Peça peça){
        return _peces.contains(peça);
    }

    public Set<Peça> get_peces() {
        return _peces;
    }

    public HashMap<String, Integer> get_seguidors() {
        return _seguidors;
    }
}
