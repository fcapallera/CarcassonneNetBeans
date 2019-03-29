import java.util.*;

public class Tauler {
    private HashMap<Integer,Peça> _tauler = new HashMap<>();
    private HashMap<String, ArrayList<Construccio>> _connexions = new HashMap<>();
    private HashSet<Integer> _disponibles = new HashSet<>();
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
            List<Integer> indexFusio = new ArrayList<>();
            Regio vila = regions.get(indexs.get("V").get(0));
            Construccio actual = new Vila(vila);
            _connexions.get("vila").add(actual);
            for(int i : indexs.get("V")){
                if(adjacents.get(i)!=null){
                    int aux = indexOfConstruccio("vila",adjacents.get(i).getRegio((i+2)%4));
                    indexFusio.add(aux);
                    actual.fusionar(_connexions.get("vila").get(aux));
                }
            }
            
            for(int i : indexFusio){
                actual.fusionar(_connexions.get("vila").get(i));
            }
            for(int i : indexFusio){
                _connexions.get("vila").remove(i);
            }
        }
        else{
            for(int i : indexs.get("V")){
                if(adjacents.get(i)==null) _connexions.get("vila").add(new Construccio(peça.getRegio(i)));
                else _connexions.get("vila").get(indexOfConstruccio("vila",adjacents.get(i).getRegio((i+2)%4)));
            }
        }
        if(peça.centre()=='X'){
            for(int i : indexs.get("C")){
                if(adjacents.get(i)==null) _connexions.get("cami").add(new Construccio(peça.getRegio(i)));
                else _connexions.get("cami").get(indexOfConstruccio("cami",adjacents.get(i).getRegio((i+2)%4)));
            }
        }
    }

    public void afegirConnexioVila(Peça peça){
        //Pot connectar dues viles
        
    }

    public int indexOfConstruccio(String constr, Regio regio){
        int i = 0;
        while(i<_connexions.get(constr).size()){
            if(_connexions.get(constr).get(i).conteRegio(regio)) return i;
            i++;
        }
        return -1;
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
