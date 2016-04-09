package draw;

import com.example.masha.tetris.Figure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;

import static api.AxialCoordinate.fromCoordinates;

public class Pack_3 extends Pack {

    private ArrayList<Hexagon> figure1 , figure2 , figure3 , figure4 , figure5;
    ArrayList <ArrayList<Hexagon>> pack = new ArrayList<ArrayList<Hexagon>>();
    private AxialCoordinate ax1 , ax2 , ax3 , ax4 , ax5;
    private HexagonalGrid hexagonalGrid;

    public Pack_3(HexagonalGrid hexagonalGrid) {
        super(hexagonalGrid);
        this.hexagonalGrid=hexagonalGrid;
        figure1 = makeFirst();
        figure2 = makeSecond();
        figure3 = makeThird();
        figure4 = makeFourth();
        figure5 = makeFifth();

        pack.add(figure1);
        pack.add(figure2);
        pack.add(figure3);
        pack.add(figure4);
        pack.add(figure5);
    }

    public ArrayList<Hexagon> makeFirst() { //разорванный треугольник
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax2 = fromCoordinates(2, 0);
        ax3 = fromCoordinates(3, 1);
        ax1 = fromCoordinates(1,2);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        return figure;
    }

    public ArrayList<Hexagon> makeSecond() { //разорванная трапеция
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,1);
        ax2 = fromCoordinates(1,3);
        ax3 = fromCoordinates(-1,3);
        ax4 = fromCoordinates(-1,5);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax4).get());
        return figure;
    }

    public ArrayList<Hexagon> makeThird() { //разорванная палка
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(0,0);
        ax2 = fromCoordinates(2,0);
        ax3 = fromCoordinates(4,0);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        return figure;
    }

    public ArrayList<Hexagon> makeFourth() {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,1);
        ax2 = fromCoordinates(3,1);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        return figure;
    }

    public ArrayList<Hexagon> makeFifth() { //угол
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,0);
        ax2 = fromCoordinates(1,1);
        ax3 = fromCoordinates(1,2);
        ax4 = fromCoordinates(0,2);
        ax5 = fromCoordinates(-1,2);

        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax4).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax5).get());
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