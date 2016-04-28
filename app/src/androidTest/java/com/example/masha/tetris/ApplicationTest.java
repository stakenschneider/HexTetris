package com.example.masha.tetris;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

import java.io.FileNotFoundException;


import JSON.InitGame;
import JSON.Unit;


public class ApplicationTest
{
    public static void main (String[] args) throws FileNotFoundException
    {
        String jsonString = "{\"units\": [{\"members\":[{\"x\":0,\"y\":0},{\"x\":2,\"y\":0}], \"pivot\":{\"x\":1,\"y\":0}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":0,\"y\":1},{\"x\":1,\"y\":2}],\"pivot\":{\"x\":1,\"y\":1}},{\"members\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":1,\"y\":1},{\"x\":0,\"y\":1}],\"pivot\":{\"x\":1,\"y\":1}}]}";
        InitGame game = new InitGame(jsonString);

        System.out.println("Game ID: " + game.ID);
        System.out.println("Game Width: " + game.width);
        System.out.println("Game Height: " + game.height);
        System.out.println("Game sourceLength: " + game.sourceLength);

        for(int unitIter = 0; unitIter <= game.incoming.length-1; unitIter++)
        {
            Unit currentUnit = game.incoming[unitIter];
            int memberLength = game.incoming[unitIter].getMemberLength();
            System.out.println("Unit Pivot: (" + currentUnit.getPivotX() + "," + currentUnit.getPivotY() + ")");
            for(int memberIter = 0; memberIter <= memberLength-1; memberIter++)
            {
                System.out.println("Member " + memberIter + ": (" + currentUnit.getMemberX(memberIter) + "," + currentUnit.getMemberY(memberIter) + ")");
            }
        }
        for(int fillIter = 0; fillIter <= game.filled.length-1; fillIter++)
        {
            int x = game.filled[fillIter].xx;
            int y = game.filled[fillIter].yy;
            System.out.println("Filled Cell: (" + x + "," + y + ")");
        }

        System.out.println("sourceSeed: ");
        for(int seedIter = 0; seedIter <= game.sourceSeeds.length-1; seedIter++)
        {
            System.out.print(game.sourceSeeds[seedIter] + ", ");
        }
    }

}