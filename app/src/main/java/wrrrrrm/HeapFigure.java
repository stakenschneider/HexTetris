package wrrrrrm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import JSON.InitGame;
import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;

import static api.AxialCoordinate.fromCoordinates;

public class HeapFigure {

    ArrayList <ArrayList<Hexagon>> pack = new ArrayList<>(); //лист фигур
    ArrayList<BigInteger> pseudoRandSeq = new ArrayList<>();
    private AxialCoordinate ax;
    private HexagonalGrid hexagonalGrid;
    InitGame initGame;
    int step = 0;


    public HeapFigure(HexagonalGrid hexagonalGrid , int amountUnits , String str) {
        this.hexagonalGrid = hexagonalGrid;
        initGame = new InitGame(str);
        for (int i = 0; i < amountUnits; i++)
        {
            pack.add(makeFigure(initGame.quantityHexOfUnit[i] , step, i+1));
            step = step+initGame.quantityHexOfUnit[i];
        }
    }


    public ArrayList<Hexagon> makeFigure(int amountCell , int uu ,  int am) {
        ArrayList<Hexagon> figure = new ArrayList<>();
        ax = fromCoordinates(initGame.pivotCoordinates[am*2-2], initGame.pivotCoordinates[am]);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax).get());

        for (int i = uu ; i < amountCell + uu; i+=2)
        {
            ax = fromCoordinates(initGame.coordinatesOfUnit.get(i), initGame.coordinatesOfUnit.get(i+1));
            figure.add(hexagonalGrid.getByAxialCoordinate(ax).get());
        }
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


    public void getFigure(int gridW)
    {
        Random random = new Random();
        //TODO: вместо рандома lcg
        ArrayList <Hexagon> newFigure = pack.get(random.nextInt(initGame.quantityUnit));
        Iterator<Hexagon> iterator = newFigure.iterator();
        Figure figureCoordinate = new Figure(newFigure);
        hexagonalGrid.getByAxialCoordinate(figureCoordinate.convertToGrid(gridW)).get().setState();
        iterator.next();
        while (iterator.hasNext()){
            hexagonalGrid.getByAxialCoordinate(figureCoordinate.getNewCoordinate(iterator.next())).get().setState();
        }
    }

}
