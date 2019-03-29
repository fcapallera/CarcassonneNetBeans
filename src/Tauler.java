import java.util.*;

public class Tauler {
    private Map<Integer,Peça> _tauler = new HashMap<>();
    private Map<String, ArrayList<Construccio>> _connexions = new HashMap<>();
    private Set<Integer> _disponibles = new HashSet<>();
    private int _maxX = 0;
    private int _minX = 0;
    private int _maxY = 0;
    private int _minY = 0;

    public Tauler(boolean camperols){
        _connexions.put("vila",new ArrayList<>());
        _connexions.put("cami",new ArrayList<>());
        _connexions.put("monestir", new ArrayList<>());
        if(camperols) _connexions.put("camp", new ArrayList<>());
    }

    public void afegirPeça(Peça peça, int x, int y){
        actualitzarCotes(x,y);
        peça.set_x(x);
        peça.set_y(y);
        _tauler.put(peça.hashCode(),peça);

        List<Peça> adjacents = new ArrayList<>();
        int[] hashKeyAdj = {100,1,-100,-1};

        for(int i=0;i<4;i++){
            if(_tauler.containsKey(peça.hashCode()+hashKeyAdj[i])){
                Peça adj = _tauler.get(peça.hashCode()+hashKeyAdj[i]);
                adjacents.add(adj);
                adj.setPeçaAdjacent(peça,(i+2)%4);
            } else adjacents.add(null);
        }
        peça.set_adjacents(adjacents);
        
        Map<String,ArrayList<Integer>> indexs = peça.get_indexs();
        List<Regio> regions = peça.get_regions();
        if(peça.centre()=='V' || peça.centre()=='E'){
            Regio vila = regions.get(indexs.get("V").get(0));
            Construccio actual = new Vila(vila);
            for(int i : indexs.get("V")){
                if(adjacents.get(i)!=null){
                    Construccio aux = buscarConstruccio("vila",adjacents.get(i).getRegio((i+2)%4));
                    actual.fusionar(aux);
                    _connexions.get("vila").remove(aux);
                }
            }
            _connexions.get("vila").add(actual);
        }
        else{
            for(int i : indexs.get("V")){
                if(adjacents.get(i)==null) _connexions.get("vila").add(new Vila(peça.getRegio(i)));
                else buscarConstruccio("vila",adjacents.get(i).getRegio((i+2)%4)).addRegio(regions.get(i));
            }
        }
        if(peça.centre()=='X'){
            for(int i : indexs.get("C")){
                if(adjacents.get(i)==null) _connexions.get("cami").add(new Cami(peça.getRegio(i)));
                else buscarConstruccio("cami",adjacents.get(i).getRegio((i+2)%4)).addRegio(regions.get(i));
            }
        }
        else{
            if(indexs.get("C").size()>0){
                Regio cami = regions.get(indexs.get("C").get(0));
                Construccio actual = new Cami(cami);
                for(int i : indexs.get("C")){
                    if(adjacents.get(i)!=null){
                        Construccio aux = buscarConstruccio("cami",adjacents.get(i).getRegio((i+2)%4));
                        actual.fusionar(aux);
                        _connexions.get("cami").remove(aux);
                    }
                }
                _connexions.get("cami").add(actual);
            }
        }
    }


    public Construccio buscarConstruccio(String constr, Regio regio){
        int i = 0;
        while(i<_connexions.get(constr).size()){
            Construccio actual = _connexions.get(constr).get(i);
            if(actual.conteRegio(regio)) return actual;
            i++;
        }
        return null;
    }

    public Peça getPeça(int x, int y){
        return _tauler.get(100*x+y);
    }

    private void actualitzarCotes(int x, int y){
        if(x < _minX) _minX = x;
        else if(x > _maxX) _maxX = x;
        if(y < _minY) _minY = y;
        else if(y > _maxY) _maxY = y;
    }

}
