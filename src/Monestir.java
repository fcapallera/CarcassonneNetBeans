/** @file Monestir.java
    @brief Classe Monestir
*/
import java.util.HashSet;
import java.util.Set;

/** @class Monestir
    @brief 
    @author 
*/
public class Monestir extends Construccio {
    /** @brief 
	@pre 
	@post  */
    public Monestir(Regio regio) {
        super(regio);
        Peça peça = regio.get_peça();
        int[] hashKeyAdj = {100,101,1,-99,-100,-101,-1,99};
        for(int i : hashKeyAdj){
            _pendents.add(peça.hashCode()+i);
        }
    }
    
    /** @brief 
	@pre 
	@post  */
    @Override
    public int puntuar(){
        return 9 - _pendents.size();
    }
    
    /** @brief 
	@pre 
	@post  */
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
