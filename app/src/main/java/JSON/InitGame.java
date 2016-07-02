package JSON;

import org.json.*;
import java.util.ArrayList;
import api.CoordinateConverter;

public class InitGame
{
    protected int quantityFilled, quantityUnit;
    public int ID , width , height;
    public int[]  pivotCoordinates , sourceSeeds , quantityHexOfUnit;
    public ArrayList<Integer> coordinatesOfUnit = new ArrayList<>();
    public ArrayList<Integer> filled = new ArrayList<>();
    public int sourceLength;
    CoordinateConverter converter;

    public InitGame(String strJson)
    {
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            height = Integer.parseInt(jsonRootObject.optString("height"));
            width = Integer.parseInt(jsonRootObject.optString("width"));

            JSONArray jsonArray = jsonRootObject.optJSONArray("sourceSeeds");
            this.sourceSeeds = new int[jsonArray.length()];
            for(int i = 0 ; i <= jsonArray.length() - 1; i++)
                this.sourceSeeds[i] = jsonArray.optInt(i);

            this.ID = Integer.parseInt(jsonRootObject.optString("id"));

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

                for(int z = 0; z < jsonMemberArray.length(); z++){ //все гексы в фигуре
                    coordinatesOfUnit.add(converter.convertOffsetCoordinatesToAxialX(jsonMemberArray.getJSONObject(z).getInt("x"), jsonMemberArray.getJSONObject(z).getInt("y")));
                    coordinatesOfUnit.add(jsonMemberArray.getJSONObject(z).getInt("y"));
                }
            }

            JSONArray jsonFilledArray = jsonRootObject.getJSONArray("filled");
            quantityFilled = jsonFilledArray.length()*2;

            for(int i = 0 ; i < quantityFilled; i++){
                JSONObject jsonFilledCell = jsonFilledArray.getJSONObject(i);
                filled.add(converter.convertOffsetCoordinatesToAxialX(jsonFilledCell.getInt("x") ,jsonFilledCell.getInt("y")));
                filled.add(jsonFilledCell.getInt("y"));
            }

            this.sourceLength = Integer.parseInt(jsonRootObject.optString("sourceLength"));
        } catch (JSONException e) {}

    }

}