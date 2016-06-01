package AI;

import java.util.LinkedList;
import api.AxialCoordinate;

/**
 * Created by Timus and Masha on 01.06.16.
 */

public class Mephistopheles {

    private class ComplexFigure{
        LinkedList<AxialCoordinate> hexs;
        LinkedList<LinkedList<AxialCoordinate>> states; //6 положений

        public ComplexFigure(LinkedList<AxialCoordinate> hexs) {
            this.hexs = hexs;
        }

        private void makeStates() {
            for (int i = 0; i<5; i++){

            }
        }


        private LinkedList<AxialCoordinate> clockwise(LinkedList<AxialCoordinate> state){

            return new LinkedList<AxialCoordinate>();
        }
    }

    public Mephistopheles(LinkedList<AxialCoordinate> hexs) {
        ComplexFigure figure = new ComplexFigure(hexs);

    }





}
