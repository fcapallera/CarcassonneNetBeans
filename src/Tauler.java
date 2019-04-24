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
        _connexions.put("V",new ArrayList<>());
        _connexions.put("C",new ArrayList<>());
        _connexions.put("M", new ArrayList<>());
        if(camperols) _connexions.put("F", new ArrayList<>());
    }
    
    public Map<Integer,Peça> getTauler(){
        return _tauler;
    }
    
    public int getMinX(){
        return _minX;
    }
    
    public int getMaxY(){
        return _maxY;
    }
    
    public void afegirPeça(Peça peça, int x, int y){
        System.out.println("Peça afegida ("+x+","+y+")");
        actualitzarCotes(x,y);
        peça.set_x(x);
        peça.set_y(y);
        _disponibles.remove(peça.hashCode());
        _tauler.put(peça.hashCode(),peça);
        
        List<Peça> adjacents = new ArrayList<>();
        int[] hashKeyAdj = {1,100,-1,-100};

        for(int i=0;i<4;i++){
            if(_tauler.containsKey(peça.hashCode()+hashKeyAdj[i])){
                Peça adj = _tauler.get(peça.hashCode()+hashKeyAdj[i]);
                adjacents.add(adj);
                adj.setPeçaAdjacent(peça,(i+2)%4);
                //Comprovar monestirs adjacents
                if(adj.centre()=='M'){
                    for(Construccio m : _connexions.get("M")) m.removePendent(peça.hashCode());
                }
            } else adjacents.add(null);
        }
        peça.set_adjacents(adjacents);
        System.out.println("arriba");
        
        for(int i=0;i<4;i++){
            Regio r = peça.getRegio(i);
            System.out.println(i+": "+r.hashCode());
            if(adjacents.get(i)==null){
                _disponibles.add(peça.hashCode()+hashKeyAdj[i]);
                if(r.get_pertany()==null){
                    Construccio c;
                    if(r.get_codi()=='V'){
                        System.out.println("Nova ciutat");
                        c = new Vila(r);
                        _connexions.get(""+r.get_codi()).add(c);
                        r.set_pertany(c);
                    }
                    else if(r.get_codi()=='C'){
                        System.out.println("Nou cami");
                        c = new Cami(r);
                        _connexions.get(""+r.get_codi()).add(c);
                        r.set_pertany(c);
                    }
                } else System.out.println("putae");
            }
            else{
                if(r.get_pertany()==null){
                    Construccio c = adjacents.get(i).getRegio((i+2)%4).get_pertany();
                    c.addRegio(r);
                    r.set_pertany(c);
                    System.out.println("Expansio");
                }
                else{
                    System.out.println("Fusio");
                    Construccio a = r.get_pertany();
                    Construccio b = adjacents.get(i).getRegio((i+2)%4).get_pertany();
                    a.fusionar(b);
                    _connexions.get(""+r.get_codi()).remove(b);
                }
            }
        }
        
        /*Map<String,ArrayList<Integer>> indexs = peça.get_indexs();
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
                else actual.addPendent(peça.hashCode()+hashKeyAdj[i]);
            }
            _connexions.get("vila").add(actual);
        }
        else if(peça.centre()=='M'){
            Monestir monestir = new Monestir(null);
            monestir.set_peça(peça);
            _connexions.get("monestir").add(monestir);
        }
        else{
            for(int i : indexs.get("V")){
                if(adjacents.get(i)==null){
                    Construccio vila = new Vila(peça.getRegio(i));
                    vila.addPendent(peça.hashCode()+hashKeyAdj[i]);
                    _connexions.get("vila").add(vila);
                }
                else buscarConstruccio("vila",adjacents.get(i).getRegio((i+2)%4)).addRegio(regions.get(i));
            }
        }
        
        if(peça.centre()=='X'){
            for(int i : indexs.get("C")){
                if(adjacents.get(i)==null){
                    Construccio cami = new Cami(peça.getRegio(i));
                    cami.addPendent(peça.hashCode()+hashKeyAdj[i]);
                    _connexions.get("cami").add(cami);
                }
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
                    else actual.addPendent(peça.hashCode()+hashKeyAdj[i]);
                }
                _connexions.get("cami").add(actual);
            }
        }*/ 
        
    }


    public Construccio buscarConstruccio(Regio regio){
        int i = 0;
        while(i<_connexions.get(regio.get_codi()+"").size()){
            Construccio actual = _connexions.get(regio.get_codi()+"").get(i);
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
    
    public boolean jugadaValida(Peça peça, int x, int y){
        if(!_disponibles.contains(100*x+y)) return false;
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

}
