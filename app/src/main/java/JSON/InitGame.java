package JSON;

import android.util.Log;
import org.json.*;

public class InitGame
{
    public int ID;
    public Unit[] incoming;
    public int width;
    public int height;
    public Cell[] filled;
    public int sourceLength;
    public int[] sourceSeeds;


    public InitGame(String strJson)
    {
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            this.height = Integer.parseInt(jsonRootObject.optString("height"));
            this.width = Integer.parseInt(jsonRootObject.optString("width"));

            JSONArray jsonArray = jsonRootObject.optJSONArray("sourceSeeds");
            this.sourceSeeds = new int[jsonArray.length()];
            for(int i = 0 ; i <= jsonArray.length() - 1; i++)
                this.sourceSeeds[i] = jsonArray.optInt(i);

            JSONArray jsonUnitArray = jsonRootObject.getJSONArray("units");
            this.incoming = new Unit[jsonUnitArray.length()];

            for(int j = 0; j < jsonUnitArray.length(); j++) {
                JSONObject jsonUnit = jsonUnitArray.getJSONObject(j);
                JSONArray jsonMemberArray = jsonUnit.getJSONArray("members");
                JSONObject jsonPivot = jsonUnit.getJSONObject("pivot");

                this.incoming[j] = new Unit();
                this.incoming[j].members = new Cell[jsonMemberArray.length()];

                for(int z = 0; z < jsonMemberArray.length(); z++)
                {
                    this.incoming[j].members[z] = new Cell();
                    this.incoming[j].members[z].xx = jsonMemberArray.getJSONObject(z).getInt("x");
                    this.incoming[j].members[z].yy = jsonMemberArray.getJSONObject(z).getInt("y");
                }

                this.incoming[j].pivot = new Cell();
                this.incoming[j].pivot.xx = jsonPivot.getInt("x");
                this.incoming[j].pivot.yy = jsonPivot.getInt("y");
            }

            this.ID = Integer.parseInt(jsonRootObject.optString("id"));
            Log.d("ID ", Integer.toString(ID));

            JSONArray jsonFilledArray = jsonRootObject.getJSONArray("filled");
            this.filled = new Cell[jsonFilledArray.length()];

            for(int fillIter = 0; fillIter < jsonFilledArray.length() ; fillIter++)
            {
                JSONObject jsonFilledCell = jsonFilledArray.getJSONObject(fillIter);
                this.filled[fillIter] = new Cell();
                this.filled[fillIter].xx = jsonFilledCell.getInt("x");
                this.filled[fillIter].yy = jsonFilledCell.getInt("y");
            }

            this.sourceLength = Integer.parseInt(jsonRootObject.optString("sourceLength"));
        } catch (JSONException e) {}

    }
}