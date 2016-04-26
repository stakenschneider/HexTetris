package wrrrrrm;

/**
 * Created by Masha on 27.04.16.
 */

import wrrrrrm.Figure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;

import static api.AxialCoordinate.fromCoordinates;

public class HeapFigure {
    private ArrayList<Hexagon> figure1 , figure2 , figure3 , figure4 , figure5;
    ArrayList <ArrayList<Hexagon>> pack = new ArrayList<ArrayList<Hexagon>>();
    private AxialCoordinate ax1 , ax2 , ax3 , ax4 , ax5;
    private HexagonalGrid hexagonalGrid;


    public HeapFigure(HexagonalGrid hexagonalGrid) {
        this.hexagonalGrid=hexagonalGrid;
        figure1 = makeFirst();

        pack.add(figure1);
        pack.add(figure2);
        pack.add(figure3);
        pack.add(figure4);
        pack.add(figure5);
    }

    public ArrayList<Hexagon> makeFirst() {  //трапеция
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1, 1);
        ax2 = fromCoordinates(1, 2);
        ax3 = fromCoordinates(2, 1);
        ax4 = fromCoordinates(2, 2);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax4).get());
        return figure;
    }


    public void getFigure(int gridWidth) {
        Random random = new Random();
        ArrayList <Hexagon> newFigure = pack.get(random.nextInt(5));
        Iterator<Hexagon> iterator = newFigure.iterator();
        Figure figureCoordinate = new Figure(newFigure);
        hexagonalGrid.getByAxialCoordinate(figureCoordinate.convertToGrid(gridWidth)).get().setState();
        iterator.next();
        while (iterator.hasNext()){
            hexagonalGrid.getByAxialCoordinate(figureCoordinate.getNewCoordinate(iterator.next())).get().setState();
        }
    }


}
