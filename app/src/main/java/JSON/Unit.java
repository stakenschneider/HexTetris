package JSON;

/**
 * Created by Masha on 21.04.16.
 */

public class Unit
{
    public Cell[] members;
    public Cell pivot;

    public int getMemberX(int index)
    {
        return this.members[index].xx;
    }
    public int getMemberY(int index) {return this.members[index].yy;}
    public int getPivotX()
    {
        return this.pivot.xx;
    }
    public int getPivotY()
    {
        return this.pivot.yy;
    }
    public int getMemberLength()
    {
        return this.members.length;
    }
}
