package JSON;

import android.util.Log;
import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import wrrrrrm.Controller;
import api.CoordinateConverter;

public class InitGame
{
    public int ID , width ,  height,
            quantityFilled, //количество нач-зап клеток
            quantityUnit; //количество фигур

    public int[]  pivotCoordinates ,  //массив с коорднатами точек поворота
            sourceSeeds,
            filled,  //массив с начально-заполненными фигурами
            quantityHexOfUnit; //массив с количеством гексов в фигуре
    public ArrayList<Integer> coordinatesOfUnit = new ArrayList<>(); //список со всеми координатами фигур
    public int sourceLength;

    public InitGame(String strJson)
    {
        //TODO: разобраться с демонами в путях
//        try {
//            textFromFile = read(strJson);
//            Log.d("piska ",textFromFile);
//        }catch (FileNotFoundException ex){          Log.d("piska ","ne rabochia");}

        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            height = Integer.parseInt(jsonRootObject.optString("height"));
            width = Integer.parseInt(jsonRootObject.optString("width"));

            JSONArray jsonArray = jsonRootObject.optJSONArray("sourceSeeds");
            this.sourceSeeds = new int[jsonArray.length()];
            for(int i = 0 ; i <= jsonArray.length() - 1; i++)
                this.sourceSeeds[i] = jsonArray.optInt(i);

            this.ID = Integer.parseInt(jsonRootObject.optString("id"));

            JSONArray jsonFilledArray = jsonRootObject.getJSONArray("filled");
            quantityFilled = jsonFilledArray.length();
            filled = new int[quantityFilled];

            JSONArray jsonUnitArray = jsonRootObject.getJSONArray("units");
            quantityUnit = jsonUnitArray.length(); //количество фигур
            quantityHexOfUnit = new int[quantityUnit]; //количество гексов в фигуре
            pivotCoordinates = new int[quantityUnit*2];

            for(int j = 0; j < quantityUnit; j++) {       //проходимся по всем фигурам
                JSONObject jsonUnit = jsonUnitArray.getJSONObject(j);
                JSONArray jsonMemberArray = jsonUnit.getJSONArray("members");
                JSONObject jsonPivot = jsonUnit.getJSONObject("pivot");

                pivotCoordinates[j] = jsonPivot.getInt("x");
                pivotCoordinates[j+1] = jsonPivot.getInt("y");

                quantityHexOfUnit[j] = jsonMemberArray.length()*2;

                for(int z = 0; z < jsonMemberArray.length(); z++) //все гексы в фигуре
                {
                    coordinatesOfUnit.add(jsonMemberArray.getJSONObject(z).getInt("x"));
                    coordinatesOfUnit.add(jsonMemberArray.getJSONObject(z).getInt("y"));
                }
            }

            for(int fillIter = 0; fillIter < quantityFilled; fillIter+=2)
            {
                JSONObject jsonFilledCell = jsonFilledArray.getJSONObject(fillIter);
                filled[fillIter] = jsonFilledCell.getInt("x");
                filled[fillIter+1] = jsonFilledCell.getInt("y");
            }

            this.sourceLength = Integer.parseInt(jsonRootObject.optString("sourceLength"));
        } catch (JSONException e) {}

    }


    public  static String read(String fileName) throws FileNotFoundException {

        StringBuilder sb = new StringBuilder();
        File file = new File(fileName+"/problem_0.txt");
        exists(fileName);

        try {

            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {

                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName+"/problem_0.txt");
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

}