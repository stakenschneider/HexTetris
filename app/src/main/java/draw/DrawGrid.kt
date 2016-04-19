package draw

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path

import com.example.masha.tetris.Controller

import api.HexagonalGrid
import api.HexagonalGridBuilder
import api.HexagonalGridCalculator
import api.Point
import api.exception.HexagonalGridCreationException
import backport.Optional

import api.HexagonOrientation.POINTY_TOP
import api.HexagonalGridLayout.RECTANGULAR
import com.example.masha.tetris.Main.scrh
import com.example.masha.tetris.Main.scrw
import com.example.masha.tetris.Settings.height
import com.example.masha.tetris.Settings.strpack
import com.example.masha.tetris.Settings.width


class DrawGrid {
    protected lateinit var  hexagonalGrid: HexagonalGrid
    private var hexagonalGridCalculator: HexagonalGridCalculator? = null
    private var controller: Controller? = null
    private val orientation = DEFAULT_ORIENTATION
    private val hexagonGridLayout = DEFAULT_GRID_LAYOUT
    internal var radius: Double = 0.0
    internal var pack: Pack

    constructor() //собственно котлин тут нужен ради этого, в дальнейшим надо все таки почитать документации и
    {
        height = 15
        width = 8
        strpack = "normal"
    }


    init {
        point = 0
        radius = radGame()

        try {
            val builder = HexagonalGridBuilder().setGridWidth(width).setGridHeight(height).setRadius(radius).setOrientation(orientation).setGridLayout(hexagonGridLayout)
            hexagonalGrid = builder.build()
            hexagonalGridCalculator = builder.buildCalculatorFor(hexagonalGrid)
            controller = Controller(builder.customStorage, hexagonalGrid.lockedHexagons, point)
        } catch (e: HexagonalGridCreationException) {}


        when (strpack) {
            "pack 1" -> pack = Pack_1(hexagonalGrid)

            "pack 2" -> pack = Pack_2(hexagonalGrid)

            "pack 3" -> pack = Pack_3(hexagonalGrid)

            else -> pack = Pack_1(hexagonalGrid)
        }
    }


    fun useBuilder(canvas: Canvas, movement: String): Boolean {
        val array = IntArray(12)

        when (movement) {

            "COUNTER_CLCK" -> controller!!.rotationCounterClockwise(hexagonalGrid)

            "CLCK" -> controller!!.rotationClockwise(hexagonalGrid)

            "DOWN_RIGHT" -> point = controller!!.moveDownRight(hexagonalGrid)

            "DOWN_LEFT" -> point = controller!!.moveDownLeft(hexagonalGrid)

            "RIGHT" -> controller!!.moveRight(hexagonalGrid)

            "LEFT" -> controller!!.moveLeft(hexagonalGrid)

            "START" -> {
                for (hexagon in hexagonalGrid.hexagons)
                    drawPoly(canvas, convertToPointsArr(hexagon.points, array), "#FF5346", Style.STROKE)
                return false
            }
        }

        if (hexagonalGrid.hexagonStorage.isEmpty()) {
            hexagonalGrid.hexagonStorage.trimToSize()
            pack.getFigure(width)

            for (axialCoordinate in hexagonalGrid.hexagonStorage)
                if (hexagonalGrid.lockedHexagons.get(axialCoordinate.gridZ).contains(axialCoordinate.gridX))
                //условие выхода из игр
                    return true
        }


        canvas.drawColor(Color.parseColor("#001B2024")) // полностью прозрачный канвас

        for (axialCoordinate in hexagonalGrid.hexagonStorage)
            drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(axialCoordinate).get().points, array), "#81AA21", Style.FILL)


        for (z in 0..hexagonalGrid.lockedHexagons.size() - 1) {
            val coordinate = hexagonalGrid.lockedHexagons.get(z)
//            if (coordinate.size != 0)
//                for (x in coordinate)
//                    drawPoly(canvas, convertToPointsArr(hexagonalGrid.getByAxialCoordinate(fromCoordinates( x , z)).get().points, array), "#FF5346", Style.FILL)
        }

        val p = Paint()
        p.color = Color.parseColor("#81AA21")
        p.strokeWidth = 1f
        p.style = Style.FILL_AND_STROKE
        p.textSize = 40f
        canvas.drawText("score: " + point, 30f, scrh.toFloat() - 15, p)

        return false
    }


    private fun drawPoly(canvas: Canvas, array: IntArray, color: String, style: Style) {

        if (array.size < 12) return

        val p = Paint()

        p.color = Color.parseColor(color)
        p.style = style

        if (width > 15)
            p.strokeWidth = 2f
        else if (width > 30)
            p.strokeWidth = 1f
        else
            p.strokeWidth = 5f

        val polyPath = Path()
        polyPath.moveTo(array[0].toFloat(), array[1].toFloat())

        var i = 0
        while (i < 12) {
            polyPath.lineTo(array[i].toFloat(), array[i + 1].toFloat())
            i += 2
        }

        polyPath.lineTo(array[0].toFloat(), array[1].toFloat())
        canvas.drawPath(polyPath, p)
    }


    private fun convertToPointsArr(points: List<Point>, array: IntArray): IntArray {
        var idx = 0
        for (point in points) {
            array[idx] = Math.round(point.coordinateX).toInt()
            array[idx + 1] = Math.round(point.coordinateY).toInt()
            idx += 2
        }
        return array
    }


    fun radGame(): Double {
        radius = 2 * scrw / (Math.sqrt(3.0) * (2 * width + 1))
        val dfkj = 50
        if (radius * ((height / 2).toDouble() + height.toDouble() + Math.sqrt(3.0) / 2.0 / 2.0) > scrh - dfkj && height % 2 == 0)
            radius = (scrh - dfkj) / ((height / 2).toDouble() + height.toDouble() + Math.sqrt(3.0) / 2.0 / 2.0)
        else if (radius * (height + (height + 1) / 2) > scrh - dfkj && height % 2 != 0)
            radius = (scrh - dfkj) / (height + (height + 1) / 2)

        return radius
    }

    companion object {

        private val DEFAULT_ORIENTATION = POINTY_TOP
        private val DEFAULT_GRID_LAYOUT = RECTANGULAR
        var point: Int = 0
    }


}