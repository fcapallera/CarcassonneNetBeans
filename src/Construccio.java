import java.util.*;

public class Construccio {
    protected Set<Regio> _regions = new HashSet<>();
    protected Map<String,Integer> _seguidors = new HashMap<>();
    protected Set<Integer> _pendents = new HashSet<>();

    public Construccio(Regio regio){
        _regions.add(regio);
    }

    public int puntuar(){return _regions.size();}

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
        _regions.addAll(c.get_regions());
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

    public void addRegio(Regio regio){
        _regions.add(regio);

    }

    public boolean conteRegio(Regio regio){
        return _regions.contains(regio);
    }

    public Set<Regio> get_regions() {
        return _regions;
    }

    public Map<String, Integer> get_seguidors() {
        return _seguidors;
    }
}
