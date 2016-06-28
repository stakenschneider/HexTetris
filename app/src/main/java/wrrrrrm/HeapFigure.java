package wrrrrrm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import JSON.InitGame;
import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;

import static api.AxialCoordinate.fromCoordinates;

public class HeapFigure {

    private ArrayList <ArrayList<Hexagon>> pack = new ArrayList<>(); //лист фигур
    private final ArrayList<BigInteger> pseudoRandSeq = new ArrayList<>();
    protected AxialCoordinate ax;
    private final HexagonalGrid hexagonalGrid;
    private final InitGame initGame;
    int step = 0;
    private int ii = 0;


    public HeapFigure(HexagonalGrid hexagonalGrid , int amountUnits , String str) {
        this.hexagonalGrid = hexagonalGrid;
        initGame = new InitGame(str);
        for (int i = 0; i < amountUnits; i++) {
            pack.add(makeFigure(initGame.quantityHexOfUnit[i] , step, i+1));
            step = step+initGame.quantityHexOfUnit[i];
        }
    }


    public ArrayList<Hexagon> makeFigure(int amountCell , int uu ,  int am) {
        ArrayList<Hexagon> figure = new ArrayList<>();
        ax = fromCoordinates(initGame.pivotCoordinates[am*2-2], initGame.pivotCoordinates[am]);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax).get());

        for (int i = uu ; i < amountCell + uu; i+=2) {
            ax = fromCoordinates(initGame.coordinatesOfUnit.get(i), initGame.coordinatesOfUnit.get(i+1));
            figure.add(hexagonalGrid.getByAxialCoordinate(ax).get());
        }
        return figure;
    }


    public ArrayList<BigInteger> makePRS(int sourceLength , int sourceSeeds , BigInteger amountUnits) {
        Lcg randSlow = new Lcg(BigInteger.valueOf(sourceSeeds));
        for (int i = 0; i < sourceLength; i++){
            pseudoRandSeq.add(randSlow.getState().mod(amountUnits));
            randSlow.next();
        }
        return pseudoRandSeq;
    }


    public void getFigure(int gridW , int sourceQ) {
        ii++;
        makePRS(initGame.sourceLength , initGame.sourceSeeds[sourceQ] , BigInteger.valueOf(initGame.quantityHexOfUnit.length));
        //TODO:  исправлять java.lang.IndexOutOfBoundsException: Invalid index 3, size is 1
        ArrayList <Hexagon> newFigure = pack.get(pseudoRandSeq.get(ii).intValue());
        Iterator<Hexagon> iterator = newFigure.iterator();
        Figure figureCoordinate = new Figure(newFigure);
        hexagonalGrid.getByAxialCoordinate(figureCoordinate.convertToGrid(gridW)).get().setState();
        iterator.next();
        while (iterator.hasNext()){
            hexagonalGrid.getByAxialCoordinate(figureCoordinate.getNewCoordinate(iterator.next())).get().setState();
        }
    }

}
