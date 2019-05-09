public class Jugador {
    private int _id;
    private String _nom;
    private boolean _cpu = false;
    private int _punts = 0;
    private String _color;
    private int _seguidors = 7;

    public Jugador(int _id) {
        this._id = _id;
    }

    public void setCpu() {
        this._cpu = true;
    }
    
    public boolean get_cpu(){
        return _cpu;
    }
    
    public int getId(){
        return _id;
    }
    
    public void jugarCpu(Tauler tauler){
        for(int i=0;i<4;i++){
            
        }
    }
    
    public void utilitzarSeguidor(){
        _seguidors -= 1;
    }
    
    public void tornarSeguidor(){
        _seguidors += 1;
    }
    
    public int getPunts(){
        return _punts;
    }
    
    public int getSeguidors(){
        return _seguidors;
    }
}
