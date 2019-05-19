/** @file Jugador.java
    @brief Classe Jugador
*/
/** @class Jugador
    @brief 
    @author 
*/
public class Jugador {
    private int _id;///< Id del Jugador
    private String _nom;///< Nom del Jugador
    private boolean _cpu = false;///< Cert si el jugador no és humà
    private int _punts = 0;///< Punts del Jugador
    private String _color;///< Color dels seguidors d'un Jugador
    private int _seguidors = 7;///< Nombre de seguidors restants d'un Jugador
    
    /** @brief Constructor de Jugador
	@pre --
	@post Set _id = _id */
    public Jugador(int _id) {
        this._id = _id;
    }
    
    /** @brief Posa l'atribut CPU a cert
	@pre --
	@post Set _cpu = true */
    public void setCpu() {
        this._cpu = true;
    }
    
    /** @brief Retorna cert si el jugador és CPU
	@pre --
	@post Retorna _cpu */
    public boolean get_cpu(){
        return _cpu;
    }
    
    /** @brief Retorna la Id del jugador
	@pre --
	@post Retorna _id */
    public int getId(){
        return _id;
    }
    
    /** @brief 
	@pre 
	@post  */
    public void jugarCpu(Tauler tauler){
        for(int i=0;i<4;i++){
            
        }
    }
    
    /** @brief Representa la utilització d'un seguidor
	@pre 
	@post  */
    public void utilitzarSeguidor(){
        _seguidors -= 1;
    }
    
    /** @brief 
	@pre 
	@post  */
    public void tornarSeguidor(int s){
        _seguidors += s;
    }
    
    /** @brief 
	@pre 
	@post  */
    public void sumarPunts(int punts){
        _punts += punts;
    }
    
    /** @brief Retorna els punts del Jugador
	@pre --
	@post Retorna _punts */
    public int getPunts(){
        return _punts;
    }
    
    /** @brief Retorna els seguidors del Jugador
	@pre --
	@post Retorna _seguidors */
    public int getSeguidors(){
        return _seguidors;
    }
}
