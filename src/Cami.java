/** @file Cami.java
    @brief Classe Cami
*/
import java.util.Set;
import java.util.TreeSet;

/** @class Cami
    @brief 
    @author 
*/
public class Cami extends Construccio {
    /** @brief Constructor de Cami
	@pre 
	@post  */
    public Cami(Regio regio){ super(regio);}
    
    /** @brief 
	@pre 
	@post */
    @Override
    public int puntuar(){
        Set<Peça> peces = new TreeSet<>();
        _regions.forEach((r) -> {
            peces.add(r.get_peça());
        });
        return peces.size();
    }
}
