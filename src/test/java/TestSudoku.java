import eightqueens.Evolving;
import org.junit.Before;
import org.junit.Test;import sudoku.Sudoku;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * User: eptwalabha
 * Date: 17/03/14
 * Time: 12:09
 */
public class TestSudoku {

    private Sudoku sudoku;

    @Before
    public void setUp() {
        sudoku = new Sudoku();
    }
    @Test
    public void canCreateAnBasicSudokuGrid() {
        assertThat(sudoku.getSudokuSize()).isEqualTo(3);
    }

    @Test
    public void canCreateA4By4SudokuGrid() {
        sudoku = new Sudoku(4);
        assertThat(sudoku.getSudokuSize()).isEqualTo(4);
    }

    @Test
    public void cannotCreateASudokuGridShorterThan1() {
        sudoku = new Sudoku(0);
        assertThat(sudoku.getSudokuSize()).isEqualTo(3);
    }

    @Test
    public void canActAsAnEvolvingEntity() {
        boolean implementsEvolving = false;
        for (Class anInterface : sudoku.getClass().getInterfaces())
            if (anInterface == Evolving.class)
                implementsEvolving = true;

        assertThat(implementsEvolving).isTrue();
    }

    @Test
    public void canDetermineDNALength() {
        for (int i = 1; i < 10; i++) {
            sudoku = new Sudoku(i);
            assertThat(sudoku.getDNALength()).isEqualTo((int) Math.pow(i, 4));
        }
    }

    @Test
    public void canGetDNAFromSudokuGrid() {
        for (int i = 1; i < 10; i++) {
            sudoku = new Sudoku(i);
            assertThat(sudoku.getDNA().length).isEqualTo((int) Math.pow(i, 4));
        }
    }

    @Test
    public void canDetermineTheLengthSize() {
        for (int i = 1; i < 10; i++) {
            sudoku = new Sudoku(i);
            assertThat(sudoku.getGridLength()).isEqualTo(i * i);
        }
    }

    @Test
    public void canSetTheValueOfACell() {
        for (int x = 0, size = sudoku.getGridLength(); x < size; x++) {
            for (int y = 0; y < size; y++) {
                int value = (int) (Math.random() * size);
                sudoku.setCellValue(x, y, value);
                assertThat(sudoku.getCellValue(x, y)).isEqualTo(value);
            }
        }
    }

    @Test
    public void canFillARowInTheGridRandomly() {

        int[] rowCopy, currentRow;

        for (int i = 0, size = sudoku.getGridLength(); i < size; i++) {

            rowCopy = sudoku.getGridRow(i);
            assertThat(rowCopy.length).isEqualTo(sudoku.getGridLength());

            sudoku.randomizeRow(i);
            currentRow = sudoku.getGridRow(i);
            assertThat(currentRow.length).isEqualTo(sudoku.getGridLength());
            assertThat(currentRow).isNotEqualTo(rowCopy);
        }
    }

    @Test
    public void canDetermineIfARowIsNotValid() {
        for (int i = 0, size = sudoku.getGridLength(); i < size; i++)
            assertThat(sudoku.isRowValid(i)).isFalse();
    }

    @Test
    public void canDetermineIfARowIsValid() {
        for (int i = 0, size = sudoku.getGridLength(); i < size; i++) {
            sudoku.randomizeRow(i);
            assertThat(sudoku.isRowValid(i)).isTrue();
        }
    }

    @Test
    public void canRetrieveAColumnFromGrid() {

        for (int x = 0, size = sudoku.getGridLength(); x < size; x++)
            for (int y = 0; y < size; y++)
                sudoku.setCellValue(x, y, x);

        int[] column;
        for (int i = 0, size = sudoku.getGridLength(); i < size; i++) {
            column = sudoku.getGridColumn(i);
            assertThat(column.length).isEqualTo(sudoku.getGridLength());
            for (int j = 0; j < size; j++)
                assertThat(column[j]).isEqualTo(i);
        }
    }

    @Test
    public void canDetermineIfAColumnIsNotValid() {
        for (int i = 0, size = sudoku.getGridLength(); i < size; i++)
            assertThat(sudoku.isColumnValid(i)).isFalse();
    }

    @Test
    public void canDetermineIfAColumnIsValid() {
        for (int i = 0, size = sudoku.getGridLength(); i < size; i++) {
            sudoku.randomizeColumn(i);
            assertThat(sudoku.isColumnValid(i)).isTrue();
        }
    }

    @Test
    public void canRetrieveCellFromCoordinates() {

        for (int x = 0, size = sudoku.getGridLength(); x < size; x++)
            for (int y = 0; y < size; y++)
                sudoku.setCellValue(x, y, x * 100 + y);

        for (int i = 0, sudokuSize = sudoku.getSudokuSize(); i < sudokuSize; i++) {
            for (int j = 0; j < sudokuSize; j++) {
                int[] cell = sudoku.getGridCell(i, j);
                assertThat(cell.length).isEqualTo(sudoku.getGridLength());
                for (int k = 0; k < sudoku.getGridLength(); k++)
                    assertThat(cell[k]).isEqualTo(sudoku.getCellValue(i * sudokuSize + k % sudokuSize, j * sudokuSize + (int) Math.floor(k / sudokuSize)));
            }
        }
    }

    @Test
    public void canDetermineIfACellIsNotValid() {
        for (int i = 0, size = sudoku.getSudokuSize(); i < size; i++)
            for (int j = 0; j < size; j++)
                assertThat(sudoku.isCellValid(j, i)).isFalse();
    }

    @Test
    public void canDetermineIfACellIsValid() {
        for (int i = 0, size = sudoku.getSudokuSize(); i < size; i++)
            for (int j = 0; j < size; j++) {
                sudoku.randomizeCell(i, j);
                assertThat(sudoku.isCellValid(i, j)).isTrue();
            }
    }

    @Test
    public void canDetermineTheRowFitnessOfAGrid() {

        for (int sudokuPower = 1; sudokuPower < 10; sudokuPower++) {
            // all rows are valid but the first one
            for (int i = 0, size = sudoku.getGridLength(); i < size; i++) {
                if (i == 0)
                    continue;
                sudoku.randomizeRow(i);
            }
            assertThat(sudoku.determineRowFitness()).isEqualTo((sudoku.getGridLength() - 1f) / sudoku.getGridLength());
        }
    }

    @Test
    public void canDetermineTheColumnFitnessOfAGrid() {

        for (int sudokuPower = 1; sudokuPower < 10; sudokuPower++) {
            // all columns are valid but the first one
            for (int i = 0, size = sudoku.getGridLength(); i < size; i++) {
                if (i == 0)
                    continue;
                sudoku.randomizeColumn(i);
            }
            assertThat(sudoku.determineColumnFitness()).isEqualTo((sudoku.getGridLength() - 1f) / sudoku.getGridLength());
        }
    }

    @Test
    public void canDetermineTheCellFitnessOfAGrid() {

        for (int sudokuPower = 1; sudokuPower < 10; sudokuPower++) {
            // all cells are valid but the first one
            for (int cellIndexX = 0, size = sudoku.getSudokuSize(); cellIndexX < size; cellIndexX++) {
                for (int cellIndexY = 0; cellIndexY < size; cellIndexY++) {
                    if (cellIndexX == 0 && cellIndexY == 0)
                        continue;
                    sudoku.randomizeCell(cellIndexX, cellIndexY);
                }
            }
            assertThat(sudoku.determineCellFitness()).isEqualTo((sudoku.getGridLength() - 1f) / sudoku.getGridLength());
        }
    }
}
