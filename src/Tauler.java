import java.util.*;

public class Tauler {
    private Map<Integer,Peça> _tauler = new HashMap<>();
    private Map<String, List<Construccio>> _connexions = new HashMap<>();
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
    
    public boolean contains(Peça peça){
        return _tauler.containsKey(peça.hashCode());
    }
    
    public boolean contains(Integer hash){
        return _tauler.containsKey(hash);
    }
    
    public void afegirPeça(Peça peça, int x, int y){
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
            } else adjacents.add(null);
        }
        peça.set_adjacents(adjacents);
        
        //Comprovació de peça amb Monestir
        if(peça.centre()=='M'){
            Regio m = peça.getRegio(-1);
            Construccio c = new Monestir(m);
            ((Monestir)c).setPendents(this, peça);
            m.set_pertany(c);
            _connexions.get("M").add(c);
        }
        
        //Actualitzem les peces pendents dels possibles monestirs adjacents
        actualitzarMonestirs(peça);
        
        for(int i=0;i<4;i++){
            Regio r = peça.getRegio(i);
            if(adjacents.get(i)==null) _disponibles.add(peça.hashCode()+hashKeyAdj[i]);
            if(r.get_codi()!='F'){
                if(adjacents.get(i)==null){
                    if(r.get_pertany()==null){
                        Construccio c;
                        if(r.get_codi()=='V'){
                            c = new Vila(r);
                            _connexions.get(""+r.get_codi()).add(c);
                            r.set_pertany(c);
                        }
                        else if(r.get_codi()=='C'){
                            c = new Cami(r);
                            _connexions.get(""+r.get_codi()).add(c);
                            r.set_pertany(c);
                        }
                    }
                }
                else{
                    if(r.get_pertany()==null){
                        Construccio c = adjacents.get(i).getRegio((i+2)%4).get_pertany();
                        c.addRegio(r);
                        r.set_pertany(c);
                    }
                    else{
                        Construccio a = r.get_pertany();
                        Construccio b = adjacents.get(i).getRegio((i+2)%4).get_pertany();
                        a.fusionar(b);
                        _connexions.get(""+r.get_codi()).remove(b);
                    }
                }
            }
        }
    }
    
    public void afegirSeguidor(int x, int y, int pos, Jugador actual){
        actual.utilitzarSeguidor();
        Peça peça = getPeça(x,y);
        Regio regio = peça.getRegio(pos-1);
        regio.setSeguidor(actual);
    }
    
    public List<Integer> seguidorsValids(int x, int y, Jugador actual){
        List<Integer> valids = new ArrayList<>();
        Peça peça = getPeça(x,y);
        for(int i=0;i<5;i++){
            Regio regio = peça.getRegio(i-1);
            if(regio==null || regio.get_codi()=='F') valids.add(0);
            else{
                if(!regio.get_pertany().ocupada()) valids.add(1);
                else{
                    List<Jugador> puntuadors = regio.get_pertany().quiPuntua();
                    if(puntuadors.contains(actual)) valids.add(1);
                    else valids.add(0);
                }
            }
        }
        return valids;
    }


    /*public Construccio buscarConstruccio(Regio regio){
        int i = 0;
        while(i<_connexions.get(regio.get_codi()+"").size()){
            Construccio actual = _connexions.get(regio.get_codi()+"").get(i);
            if(actual.conteRegio(regio)) return actual;
            i++;
        }
        return null;
    }*/

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
    
    public void actualitzarPuntuacions(){
        Iterator it = _connexions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            List<Construccio> llista = (List<Construccio>)pair.getValue();
            Iterator<Construccio> iter = llista.iterator();
            while(iter.hasNext()){
                Construccio c = iter.next();
                if(c.completada()){
                    List<Jugador> puntuadors = c.quiPuntua();
                    for(Jugador j : puntuadors) j.sumarPunts(c.puntuar());
                    c.tornarSeguidors();
                    iter.remove();
                }
            }
        }
    }
    
    private void actualitzarMonestirs(Peça peça){
        List<Construccio> monestirs = _connexions.get("M");
        for(Construccio m : monestirs) m.removePendent(peça.hashCode());
    }
    
    public static class Posicio {
        public int _x;
        public int _y;
        
        public Posicio(Integer hash){
            double xAppr = hash/100;
            _x = (int) Math.round(xAppr);
            _y = hash - _x;
        }
    }

}
