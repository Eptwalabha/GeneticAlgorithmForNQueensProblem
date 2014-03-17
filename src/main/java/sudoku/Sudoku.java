package sudoku;

import eightqueens.ChessBoard;
import eightqueens.Evolving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: eptwalabha
 * Date: 17/03/14
 * Time: 12:13
 */
public class Sudoku implements Evolving<Sudoku> {

    private final int[] dna;
    private int gridPower;
    private int gridLength;

    public Sudoku() {
        this(3);
    }

    public Sudoku(int power) {
        gridPower = (power < 1) ? 3 : power;
        int dnaSize = (int) Math.pow(power, 4);
        dna = new int[dnaSize];
        gridLength = gridPower * gridPower;
    }

    @Override
    public void setDNA(int[] dna) {
    }

    @Override
    public void configureWithRandomizeDNA() {
    }

    @Override
    public void mutate() {
    }

    @Override
    public Sudoku[] reproduce(Sudoku partner, int pivot) {
        return new Sudoku[2];
    }

    @Override
    public float getFitness() {
        return 0;
    }

    @Override
    public int[] getDNA() {
        return dna;
    }

    @Override
    public String getStringDNA() {
        return Arrays.toString(dna);
    }

    @Override
    public int getDNALength() {
        return (int) Math.pow(gridPower, 4);
    }

    @Override
    public float determineFitness() {
        return 0;
    }

    @Override
    public int compareTo(ChessBoard o) {
        return 0;
    }

    public int getSudokuSize() {
        return this.gridPower;
    }

    public int getGridLength() {
        return gridLength;
    }

    public void setCellValue(int indexX, int indexY, int value) {
        int index = getDNAIndexFromGridCoordinates(indexX, indexY);
        dna[index] = value;
    }

    public int getCellValue(int indexX, int indexY) {
        return dna[getDNAIndexFromGridCoordinates(indexX, indexY)];
    }

    public void randomizeRow(int rowIndex) {
        int[] copyOfDNA = dna.clone();

        int startIndex = rowIndex * gridLength;
        List<Integer> sequence = getSequence();

        do {
            Collections.shuffle(sequence);

            for (int index = 0; index < gridLength; index++)
                dna[index + startIndex] = sequence.get(index);
        } while (copyOfDNA == dna);
    }

    public void randomizeColumn(int columnIndex) {
        int[] copyOfDNA = dna.clone();
        List<Integer> sequence = getSequence();

        do {
            Collections.shuffle(sequence);

            for (int index = 0; index < gridLength; index++)
                dna[index * gridLength + columnIndex] = sequence.get(index);
        } while (copyOfDNA == dna);
    }

    public void randomizeCell(int cellIndexX, int cellIndexY) {
        int[] copyOfDNA = dna.clone();
        List<Integer> sequence = getSequence();

        do {
            Collections.shuffle(sequence);

            for (int indexY = 0; indexY < gridPower; indexY++)
                for (int indexX = 0; indexX < gridPower; indexX++)
                    dna[getDNAIndexFromCellPositionAndLocalCoordinates(cellIndexX, cellIndexY, indexX, indexY)] = sequence.get(indexY * gridPower + indexX);
        } while (copyOfDNA == dna);
    }

    public int[] getGridRow(int rowIndex) {
        return Arrays.copyOfRange(dna, rowIndex * gridLength, rowIndex * gridLength + gridLength);
    }

    public int[] getGridColumn(int columnIndex) {

        columnIndex %= gridLength;

        int[] column = new int[gridLength];

        for (int i = 0; i < gridLength; i++)
            column[i] = dna[i * gridLength + columnIndex];

        return column;
    }

    public int[] getGridCell(int cellIndexX, int cellIndexY) {

        int[] cell = new int[gridLength];

        for (int y = 0; y < gridPower; y++)
            for (int x = 0; x < gridPower; x++)
                cell[y * gridPower + x] = dna[getDNAIndexFromCellPositionAndLocalCoordinates(cellIndexX, cellIndexY, x, y)];

        return cell;
    }

    public boolean isRowValid(int rowIndex) {
        int[] row = getGridRow(rowIndex);
        return isArrayValid(row);
    }

    public Boolean isColumnValid(int columnIndex) {
        int[] column = getGridColumn(columnIndex);
        return isArrayValid(column);
    }

    public boolean isCellValid(int cellIndexX, int cellIndexY) {
        int[] cell = getGridCell(cellIndexX, cellIndexY);
        return isArrayValid(cell);
    }

    private boolean isArrayValid(int[] elements) {
        Arrays.sort(elements);
        for (int i = 0; i < gridLength; i++)
            if (elements[i] != i)
                return false;
        return true;
    }

    public float determineRowFitness() {
        float validCounter = 0;
        for (int rowIndex = 0; rowIndex < gridLength; rowIndex++)
            validCounter += (isRowValid(rowIndex)) ? 1 : 0;
        return validCounter / gridLength;
    }

    public float determineColumnFitness() {
        float validCounter = 0;
        for (int columnIndex = 0; columnIndex < gridLength; columnIndex++)
            validCounter += (isColumnValid(columnIndex)) ? 1 : 0;
        return validCounter / gridLength;
    }

    public float determineCellFitness() {
        float validCounter = 0;
        for (int cellIndexY = 0; cellIndexY < gridPower; cellIndexY++)
            for (int cellIndexX = 0; cellIndexX < gridPower; cellIndexX++)
                validCounter += (isCellValid(cellIndexX, cellIndexY)) ? 1 : 0;
        return validCounter / gridLength;
    }

    private List<Integer> getSequence() {
        List<Integer> listNumber = new ArrayList<Integer>();
        for (int index = 0; index < gridLength; index++)
            listNumber.add(index);
        return listNumber;
    }

    private int getDNAIndexFromGridCoordinates(int indexX, int indexY) {
        int index = indexX + indexY * gridLength;
        index = (index < 0) ? 0 : index;
        index = (index >= dna.length) ? dna.length - 1 : index;
        return index;
    }

    private int getDNAIndexFromCellPositionAndLocalCoordinates(int cellIndexX, int cellIndexY, int x, int y) {
        return ((cellIndexY * gridPower + y) * gridLength + cellIndexX * gridPower + x);
    }
}