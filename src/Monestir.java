/** @file Monestir.java
    @brief Classe Monestir
*/
import java.util.HashSet;
import java.util.Set;

/** @class Monestir
    @brief Construcció de tipus Monestir
    @author Ferran Capallera
*/
public class Monestir extends Construccio {
    /** @brief Constructor de Monestir a partir d'una Regio.
	@pre cert
	@post Es crea un Monestir amb el _pendents no inicialitzat. */
    public Monestir(Regio regio) {
        super(regio);
    }
    
    /** @brief Retorna la puntuació d'aquest Monestir.
	@pre cert
	@post Retorna 9 si el Monestir està complet. Altrament 9 - les peces incompletes. */
    @Override
    public int puntuar(){
        return 9 - _pendents.size();
    }
    
    /** @brief Inicialitza les peces pendents del monestir.
	@pre El monestir s'acaba de crear.
	@post Per cada peça adjacent {N,NE,E,SE,S,SW,W,NW} s'afageix a pendents si no hi és al tauler.  */
    public void setPendents(Tauler tauler, Peça peça){
        int[] hashKeyAdj = {100,101,1,-99,-100,-101,-1,99};
        Set<Integer> pendents = new HashSet<>();
        for(int i=0;i<8;i++){
            int newHash = peça.hashCode()+hashKeyAdj[i];
            if(!tauler.contains(newHash)) pendents.add(newHash);
        }
        this.set_pendents(pendents);
    }
}
