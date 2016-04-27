package wrrrrrm;

import java.math.BigInteger;
import java.util.ArrayList;
import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;

import static api.AxialCoordinate.fromCoordinates;

public class HeapFigure {

    private ArrayList<Hexagon> figure;                                         //фигура
    ArrayList <ArrayList<Hexagon>> pack = new ArrayList<ArrayList<Hexagon>>(); //лист фигур
    ArrayList<BigInteger> pseudoRandSeq = new ArrayList<>();
    private AxialCoordinate ax;
    private HexagonalGrid hexagonalGrid;

    //это создает пак
    public HeapFigure(HexagonalGrid hexagonalGrid) {
        this.hexagonalGrid = hexagonalGrid;
        int amountUnits = 0;
        for (int i = 0; i < amountUnits; i++)
        {
//            figure = makeFigure();
            //TODO: здесь запихиваем все фигуры в "ПАК"
        }
    }

    public ArrayList<Hexagon> makeFigure(int memberLength) {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();

        for (int i = 0; i < memberLength; i++)
        {
            ax = fromCoordinates(1, 1);
            figure.add(hexagonalGrid.getByAxialCoordinate(ax).get());
        }

        //TODO: тут будет создание фигуры
        return figure;
    }


    //тут создается лист длинной sourceLength который будет хранить очередность фигур
    public ArrayList<BigInteger> makePRS(int sourceLength , int sourceSeeds , int amountUnits) {
        Lcg randSlow = new Lcg(BigInteger.valueOf(sourceSeeds));
        //TODO: sequence starting mod amountUnits

        for (int i = 0; i < sourceLength; i++){ //sequence starting
            pseudoRandSeq.add(randSlow.getState());
            randSlow.next();
        }
        return pseudoRandSeq;
    }

    public ArrayList <ArrayList<Hexagon>> getFigureSeq(int sourceLength) {
        //TODO: создать лист с запиханными в него фигурами по последовательности makePRS
        return pack;
    }

    //int num - аргумент pseudoRandSeq
    public ArrayList<Hexagon> getFigure(int num)
    {
        //TODO: метод должен возвращать по одной фигуре из pack

        return figure;
    }

}
