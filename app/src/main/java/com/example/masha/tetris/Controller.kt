package com.example.masha.tetris

import android.util.SparseArray
import java.util.ArrayList

import api.AxialCoordinate
import api.HexagonalGrid
import backport.Optional

import api.AxialCoordinate.fromCoordinates


class Controller(private val dataMap: ArrayList<AxialCoordinate>   //фигуры в движении
                 , var lockedHexagons: SparseArray<ArrayList<Any>> // заблокированные шестиугольники
                 , private var point: Int                          //очки
) {


    private fun check(i: Int, hexagonalGrid: HexagonalGrid, n: Int): Boolean {
        if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap[i].gridX - n,
                dataMap[i].gridZ + 1)).isPresent || lockedHexagons.get(dataMap[i].gridZ + 1).contains(dataMap[i].gridX - n))
            return false
        return true
    }


    fun moveDownRight(hexagonalGrid: HexagonalGrid): Int {
        for (i in dataMap.indices) {
            if (!check(i, hexagonalGrid, 0)) {
                for (j in 0..i - 1) {
                    dataMap[j].gridZ = dataMap[j].gridZ - 1
                    lockedHexagons.get(dataMap[j].gridZ).add(dataMap[j].gridX)
                }

                for (j in i..dataMap.size - 1)
                    lockedHexagons.get(dataMap[j].gridZ).add(dataMap[j].gridX)

                checkRow(hexagonalGrid)
                dataMap.clear()
                break
            }
            dataMap[i].gridZ = dataMap[i].gridZ + 1
        }
        return point
    }


    fun moveDownLeft(hexagonalGrid: HexagonalGrid): Int {
        for (i in dataMap.indices) {
            if (!check(i, hexagonalGrid, 1)) {
                for (j in 0..i - 1) {
                    dataMap[j].gridZ = dataMap[j].gridZ - 1
                    dataMap[j].gridX = dataMap[j].gridX + 1
                    lockedHexagons.get(dataMap[j].gridZ).add(dataMap[j].gridX)     //а вот и он! С просонья наверн не увидел)
                }

                for (j in i..dataMap.size - 1)
                    lockedHexagons.get(dataMap[j].gridZ).add(dataMap[j].gridX)

                checkRow(hexagonalGrid)
                dataMap.clear()
                break
            }

            dataMap[i].gridZ = dataMap[i].gridZ + 1
            dataMap[i].gridX = dataMap[i].gridX - 1
        }
        return point
    }


    fun moveRight(hexagonalGrid: HexagonalGrid) {
        for (i in dataMap.indices)
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap[i].gridX + 1, dataMap[i].gridZ)).isPresent and !lockedHexagons.get(dataMap[i].gridZ).contains(dataMap[i].gridX + 1))
                dataMap[i].gridX = dataMap[i].gridX + 1
            else {
                for (j in 0..i - 1)
                    dataMap[j].gridX = dataMap[j].gridX - 1
                break
            }
    }


    fun moveLeft(hexagonalGrid: HexagonalGrid) {
        for (i in dataMap.indices) {
            if (hexagonalGrid.getByAxialCoordinate(fromCoordinates(dataMap[i].gridX - 1, dataMap[i].gridZ)).isPresent and !lockedHexagons.get(dataMap[i].gridZ).contains(dataMap[i].gridX - 1))
                dataMap[i].gridX = dataMap[i].gridX - 1
            else {
                for (j in 0..i - 1)
                    dataMap[j].gridX = dataMap[j].gridX + 1
                break
            }
        }
    }


    fun rotationClockwise(hexagonalGrid: HexagonalGrid) {
        val x = dataMap[0].gridX
        val z = dataMap[0].gridZ
        val y = -x - z
        var b = true

        for (i in 1..dataMap.size - 1)
            if (!//NULL EXCEPTION
            hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(dataMap[i].gridZ - z) + x,
                    -(-dataMap[i].gridX - dataMap[i].gridZ - y) + z)).isPresent || Optional.ofNullable(lockedHexagons.get(-(-dataMap[i].gridX - dataMap[i].gridZ - y) + z)).get().contains(-(dataMap[i].gridZ - z) + x)) {
                b = false
                break
            }

        if (b)
            for (i in 1..dataMap.size - 1)
                dataMap[i].setCoordinate(-(dataMap[i].gridZ - z) + x, -(-dataMap[i].gridX - dataMap[i].gridZ - y) + z)

    }

    fun rotationCounterClockwise(hexagonalGrid: HexagonalGrid) {
        val x = dataMap[0].gridX
        val z = dataMap[0].gridZ
        val y = -x - z
        var b = true

        for (i in 1..dataMap.size - 1)
            if (!hexagonalGrid.getByAxialCoordinate(fromCoordinates(-(-dataMap[i].gridX - dataMap[i].gridZ - y) + x,
                    -(dataMap[i].gridX - x) + z)).isPresent || Optional.ofNullable(lockedHexagons.get(-(dataMap[i].gridX - x) + z)).get().contains(-(-dataMap[i].gridX - dataMap[i].gridZ - y) + x)) {
                b = false
                break
            }

        if (b)
            for (i in 1..dataMap.size - 1)
                dataMap[i].setCoordinate(-(-dataMap[i].gridX - dataMap[i].gridZ - y) + x, -(dataMap[i].gridX - x) + z)
    }


    private fun checkRow(hexagonalGrid: HexagonalGrid) {
        for (data in dataMap)
            if (lockedHexagons.get(data.gridZ).size == hexagonalGrid.width) {
                point = hexagonalGrid.width + point
                lockedHexagons.get(data.gridZ).clear()
                lockedHexagons.get(data.gridZ).trimToSize()

                for (i in data.gridZ downTo 1)
                    if ((i - 1) % 2 == 0) {
                        val coordinate = ArrayList<Any>(lockedHexagons.get(i - 1).size)
                        for (x in lockedHexagons.get(i - 1) as ArrayList<Int>) coordinate.add(x)
                        lockedHexagons.put(i, coordinate)
                    } else {
                        val coordinate = ArrayList<Any>(lockedHexagons.get(i - 1).size)
                        for (x in lockedHexagons.get(i - 1) as ArrayList<Int>) coordinate.add(x - 1)
                        lockedHexagons.put(i, coordinate)
                    }
            }
    }
}