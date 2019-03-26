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
                adj.setPeçaAdjacent(peça,0);
            } else adjacents.add(null);
        }
        peça.set_adjacents(adjacents);
        afegirConnexioVila(peça);
    }

    public void afegirConnexioVila(Peça peça){
        //Pot connectar dues viles
        if(peça.centre()=='V' || peça.centre()=='E'){
            List<Peça> connexes = peça.adjacenciesConnexes('V');
            //No esta connectada amb cap Vila
            if(connexes.size()==0) _connexions.get("vila").add(new Vila(peça));
            else{
                List<Integer> vilesFusionar = new ArrayList<>();
                for(Peça p : connexes) vilesFusionar.add(indexOfConstruccio("vila",p));
                for(int i=1;i<vilesFusionar.size();i++){
                    _connexions.get("vila").get(0).fusionar(_connexions.get("vila").get(i));
                }
                for(int i=1;i<vilesFusionar.size();i++){
                    _connexions.get("vila").remove(i);
                }
                _connexions.get("vila").get(0).addPeça(peça);
            }
        }
        List<Peça> connexes = peça.adjacenciesConnexes('V');
        if(connexes.size()==0) _connexions.get("vila").add(new Vila(peça));
        else{
            List<Integer> indexConnexio = new ArrayList<>();
            for(Peça p : connexes) indexConnexio.add(indexOfConstruccio("vila",p));
        }
    }

    public int indexOfConstruccio(String constr, Peça peça){
        int i = 0;
        while(i<_connexions.get(constr).size()){
            if(_connexions.get(constr).get(i).contePeça(peça)) return i;
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
