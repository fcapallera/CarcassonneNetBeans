import java.util.Set;
import java.util.TreeSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class Cami extends Construccio {
    public Cami(Regio regio){ super(regio);}
    
    @Override
    public int puntuar(){
        Set<Peça> peces = new TreeSet<>();
        _regions.forEach((r) -> {
            peces.add(r.get_peça());
        });
        return peces.size();
    }
}
