package draw;

import com.example.masha.tetris.Figure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import api.AxialCoordinate;
import api.Hexagon;
import api.HexagonalGrid;
import static api.AxialCoordinate.fromCoordinates;

public class NormalPack {

    private ArrayList<Hexagon> figure1;
    private ArrayList<Hexagon> figure2;
    private ArrayList<Hexagon> figure3;
    private ArrayList<Hexagon> figure4;

    ArrayList <ArrayList<Hexagon>> pack = new ArrayList<ArrayList<Hexagon>>();
    private AxialCoordinate ax1;
    private AxialCoordinate ax2;
    private AxialCoordinate ax3;
    private AxialCoordinate ax4;


    private HexagonalGrid hexagonalGrid;

    public NormalPack(HexagonalGrid hexagonalGrid)
    {
        this.hexagonalGrid = hexagonalGrid;
        figure1 = makeFirst();
        figure2 = makeSecond();
        figure3 = makeThird();
        figure4 = makeFourth();
        pack.add(figure1);
        pack.add(figure2);
        pack.add(figure3);
        pack.add(figure4);
    }
    public ArrayList<Hexagon> makeFirst()
    {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,1);
        ax2 = fromCoordinates(1,2);
        ax3 = fromCoordinates(2,1);
        ax4 = fromCoordinates(2,2);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax4).get());
        return figure;
    }

    public ArrayList<Hexagon> makeSecond()
    {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,1);
        ax2 = fromCoordinates(1,2);
        ax3 = fromCoordinates(1,3);
        ax4 = fromCoordinates(1,4);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax4).get());
        return figure;
    }
    public ArrayList<Hexagon> makeThird()
    {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,1);
        ax2 = fromCoordinates(1,2);
        ax3 = fromCoordinates(2,2);
        ax4 = fromCoordinates(2,3);
        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax3).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax4).get());

        return figure;
    }
    public ArrayList<Hexagon> makeFourth()
    {
        ArrayList<Hexagon> figure = new ArrayList<Hexagon>();
        ax1 = fromCoordinates(1,1);
        ax2 = fromCoordinates(2,1);

        figure.add(hexagonalGrid.getByAxialCoordinate(ax1).get());
        figure.add(hexagonalGrid.getByAxialCoordinate(ax2).get());

        return figure;
    }

    public void getFigure(int gridWidth)
    {
        Random random = new Random();
        ArrayList <Hexagon> newFigure = pack.get(random.nextInt(4));
        Iterator<Hexagon> iterator = newFigure.iterator();
        Figure figureCoordinate = new Figure(newFigure);
        hexagonalGrid.getByAxialCoordinate(figureCoordinate.convertToGrid(gridWidth)).get().setState();
        iterator.next();
        while (iterator.hasNext()){
            hexagonalGrid.getByAxialCoordinate(figureCoordinate.getNewCoordinate(iterator.next())).get().setState();
        };
    }
}

