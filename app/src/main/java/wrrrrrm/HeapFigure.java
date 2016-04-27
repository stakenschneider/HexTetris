package wrrrrrm;

/**
 * Created by Masha on 27.04.16.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import api.Hexagon;

public class HeapFigure {

    ArrayList<BigInteger> pseudoRandSeq = new ArrayList<>();


    public HeapFigure(int amountUnits) {
        //TODO: здесь запихиваем все фигуры в "ПАК"
    }

    public ArrayList<Hexagon> makeFigure(int memberLength) {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        //TODO: тут будет создание фигуры
        return figure;
    }


    public void getFigure(int sourceLength , int sourceSeeds , int amountUnits) {
        Lcg randSlow = new Lcg(BigInteger.valueOf(sourceSeeds));
        //TODO: sequence starting mod amountUnits
        for (int i = 0; i < sourceLength; i++){ //sequence starting
            pseudoRandSeq.add(randSlow.getState());
            randSlow.next();
        }
    }
}
