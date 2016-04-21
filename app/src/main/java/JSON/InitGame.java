package JSON;

/**
 * Created by Masha on 21.04.16.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.*;

public class InitGame
{
    public int ID;
    public Unit[] incoming;
    public Cell[] filled;
    public int width;
    public int height;
    public int sourceLength;
    public int[] sourceSeeds;


    public InitGame (File jsonFILE) throws JSONException
    {
		//JSON file -> String -> JSONObj
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(jsonFILE));
        } catch (FileNotFoundException ex) {Log.d("piska" , "with Buff");}

        StringBuilder sb = new StringBuilder();
        String line = "";

        try {
            while((line = br.readLine()) != null)
            sb.append(line);
        }
        catch (IOException e) {e.printStackTrace();}

        JSONObject json = new JSONObject(sb.toString());

		//JSONObject -> initGame obj

        this.ID = json.getInt("id");
        this.width = json.getInt("width");
        this.height = json.getInt("height");
        this.sourceLength = json.getInt("sourceLength");

		//-> JSONObject

        JSONArray jsonUnitArray = json.getJSONArray("units");
        this.incoming = new Unit[jsonUnitArray.length()];

        for(int memberIter = 0; memberIter <= jsonUnitArray.length()-1; memberIter++)
        {
            JSONObject jsonUnit = jsonUnitArray.getJSONObject(memberIter);
            JSONArray jsonMemberArray = jsonUnit.getJSONArray("members");
            JSONObject jsonPivot = jsonUnit.getJSONObject("pivot");

            this.incoming[memberIter] = new Unit();
            this.incoming[memberIter].members = new Cell[jsonMemberArray.length()];

            for(int cellIter = 0; cellIter <= jsonMemberArray.length()-1; cellIter++)
            {
                this.incoming[memberIter].members[cellIter] = new Cell();
                this.incoming[memberIter].members[cellIter].xx = jsonMemberArray.getJSONObject(cellIter).getInt("x");
                this.incoming[memberIter].members[cellIter].yy = jsonMemberArray.getJSONObject(cellIter).getInt("y");
            }

            this.incoming[memberIter].pivot = new Cell();
            this.incoming[memberIter].pivot.xx = jsonPivot.getInt("x");
            this.incoming[memberIter].pivot.yy = jsonPivot.getInt("y");
        }

		//FilledArray -> JSONArr -> filled arr

        JSONArray jsonFilledArray = json.getJSONArray("filled");
        this.filled = new Cell[jsonFilledArray.length()];

        for(int fillIter = 0; fillIter <= jsonFilledArray.length()-1; fillIter++)
        {
            JSONObject jsonFilledCell = jsonFilledArray.getJSONObject(fillIter);
            this.filled[fillIter].xx = jsonFilledCell.getInt("x");
            this.filled[fillIter].yy = jsonFilledCell.getInt("y");
        }

        JSONArray jsonSourceSeeds = json.getJSONArray("sourceSeeds");
        this.sourceSeeds = new int[jsonSourceSeeds.length()];

        for(int sourceSeedsIter = 0; sourceSeedsIter <= jsonSourceSeeds.length()-1; sourceSeedsIter++) {
            this.sourceSeeds[sourceSeedsIter] = jsonSourceSeeds.optInt(sourceSeedsIter);
        }
    }
}