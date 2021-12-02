fun calculateEmptyPlaces(numLines: Int, numColumns: Int) = numLines * numColumns - 2

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? {
    val emptyPlaces = calculateEmptyPlaces(numLines, numColumns)

    if (emptyPlaces <= 0) return null

    val numMines = when (emptyPlaces) {
        1 -> 1
        in 2..5 -> 2
        in 6..10 -> 3
        in 11..20 -> 6
        in 21..50 -> 10
        else -> 15
    }
    return numMines
}

fun drawTerrainWithLegend(numLines: Int, numColumns: Int, numMines: Int, withColor: Boolean = false): String {
    var terrain = ""

    val esc: String = "\u001B"
    var legendColor = "$esc[97;44m"
    var endLegendColor = "$esc[0m"

    if (!withColor) {
        legendColor = ""
        endLegendColor = ""
    }

    // Draw Legend
    terrain += "$legendColor    ${createLegend(numColumns)}    $endLegendColor\n"

    var lineCounter = 1
    while(lineCounter <= numLines) {
        var columnCounter = numColumns
        var mineCounter = numMines

        // Draw game artifacts by column for each line
        while (columnCounter > 0) {
            // If its 1st column, draw player
            if (columnCounter == numColumns) {
                terrain += "$legendColor $lineCounter $endLegendColor P "
                columnCounter--

            // If its last column, draw finish
            } else if (columnCounter == 1) {
                terrain += "| f $legendColor   $endLegendColor\n"

                // Add spaces below terrain
                var spacesBelow = "$legendColor "
                var spacesCounter = 0
                while (spacesCounter < (numColumns + 1) * 4) {
                    spacesBelow += " "
                    spacesCounter++
                }
                spacesBelow += "$endLegendColor"
                terrain += spacesBelow

                columnCounter--
            } else {
                // Draw mines
                if (mineCounter > 0) {
                    terrain += "| * "
                    mineCounter--
                    columnCounter--

                // Draw empty places
                } else {
                    terrain += "|   "
                    columnCounter--
                }
            }
            lineCounter++
        }
    }

    return terrain
}

fun drawTerrainWithNoLegend(numLines: Int, numColumns: Int, numMines: Int): String {
    var terrain = ""

    var lineCounter = 1
    while (lineCounter <= numLines) {
        var columnCounter = numColumns
        var mineCounter = numMines

        // Draw game artifacts by column for each line
        while (columnCounter > 0) {
            // If its 1st column, draw player
            if (columnCounter == numColumns) {
                terrain += " P "
                columnCounter--

            // If its last column, draw finish
            } else if (columnCounter == 1) {
                terrain += "| f "
                columnCounter--
            } else {
                // Draw mines
                if (mineCounter > 0) {
                    terrain += "| * "
                    mineCounter--
                    columnCounter--

                // Draw empty places
                } else {
                    terrain += "|   "
                    columnCounter--
                }
            }
        }
        lineCounter++
    }

    return terrain
}

fun makeTerrain(numLines: Int, numColumns: Int, numMines: Int, showLegend: Boolean = true, withColor: Boolean = false): String {
    // Add legend or not
    if (showLegend && withColor) {
        return drawTerrainWithLegend(numLines, numColumns, numMines, withColor)
    } else if (showLegend) {
        return drawTerrainWithLegend(numLines, numColumns, numMines)
    } else {
        return drawTerrainWithNoLegend(numLines, numColumns, numMines)
    }
}

fun createLegend(numColumns: Int): String {
    var letter = 'A'
    var count = 0
    var legend = ""
    while (count < numColumns) {
        if (count == numColumns - 1) {
            legend += letter
        } else {
            legend += "$letter   "
        }
        letter++
        count++
    }
    return legend
}

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean {
    val emptyPlaces = calculateEmptyPlaces(numLines, numColumns)
    return !(numMines <= 0 || emptyPlaces < numMines)
}