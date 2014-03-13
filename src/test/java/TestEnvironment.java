import eightqueens.ChessBoard;
import eightqueens.EightQueensEnvironment;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * User: Eptwalabha
 * Date: 12/03/14
 * Time: 21:26
 */
public class TestEnvironment {

    private EightQueensEnvironment eightQueensEnvironment;

    @Before
    public void setUp() {
        eightQueensEnvironment = new EightQueensEnvironment(20);
    }

    @Test
    public void canCorrectTheInitialPopulationAmountToBeMultipleOfFour() {
        eightQueensEnvironment = new EightQueensEnvironment(13);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(16);
    }

    @Test
    public void canCreateAnEmptyEnvironment() {
        eightQueensEnvironment = new EightQueensEnvironment();
        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(0);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(12);
    }

    @Test
    public void canPopulateAGeneration() {
        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(0);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(20);
    }

    @Test
    public void canReturnPopulationOfCurrentGeneration() {
        eightQueensEnvironment = new EightQueensEnvironment(200);
        List<ChessBoard> chessBoards = eightQueensEnvironment.getCurrentPopulation();
        assertThat(chessBoards.size()).isEqualTo(200);
        assertThat(chessBoards.get(0)).isNotNull();
        assertThat(chessBoards.get(0).getStringDNA()).isNotNull();
        assertThat(chessBoards.get(0).getStringDNA()).isNotEqualTo(chessBoards.get(1).getStringDNA());
    }

    @Test
    public void canSortEntitiesOfAGenerationFromHigherToLowerFitness() {
        List<ChessBoard> chessBoards = eightQueensEnvironment.getCurrentPopulation();

        float fitnessOfLastEntity = -1, fitnessOfCurrentEntity;

        boolean first = true;

        for (ChessBoard chessBoard : chessBoards) {
            if (first) {
                fitnessOfLastEntity = chessBoard.getFitness();
                first = false;
                continue;
            }
            fitnessOfCurrentEntity = chessBoard.getFitness();
            assertThat(fitnessOfCurrentEntity).isLessThanOrEqualTo(fitnessOfLastEntity);
            fitnessOfLastEntity = fitnessOfCurrentEntity;
        }
    }

    @Test
    public void canProcessToNextGeneration() {

        int numberOfEntities = 252;
        eightQueensEnvironment = new EightQueensEnvironment(numberOfEntities);
        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(0);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(numberOfEntities);

        eightQueensEnvironment.processToNextGeneration();

        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(1);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(numberOfEntities);

        int numberOfGeneration = 30;
        for (int i = 1; i < numberOfGeneration; i++) {
            eightQueensEnvironment.processNGeneration(10);
            assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(i * 10 + 1);
            assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(numberOfEntities);
        }
    }


}
