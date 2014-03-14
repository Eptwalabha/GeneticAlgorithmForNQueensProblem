import eightqueens.ChessBoard;
import eightqueens.EightQueensEnvironment;
import org.junit.Before;
import org.junit.Test;

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
        eightQueensEnvironment = new EightQueensEnvironment(8);
    }

    @Test
    public void canCorrectTheInitialPopulationAmountToBeMultipleOfFour() {
        eightQueensEnvironment = new EightQueensEnvironment(8, 13);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(16);
    }

    @Test
    public void canCreateABasicEnvironment() {
        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(0);
    }

    @Test
    public void canPopulateAnEnvironmentBasedOnTheBoardSize() {
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(64);
    }

    @Test
    public void cannotCreateABoardShorterThan4() {
        eightQueensEnvironment = new EightQueensEnvironment(1);
        assertThat(eightQueensEnvironment.getCurrentPopulation().get(0).getDNALength()).isEqualTo(4);
    }

    @Test
    public void canPopulateAGeneration() {
        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(0);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(64);
    }

    @Test
    public void canReturnPopulationOfCurrentGeneration() {
        eightQueensEnvironment = new EightQueensEnvironment(12, 200);
        List<ChessBoard> chessBoards = eightQueensEnvironment.getCurrentPopulation();
        assertThat(chessBoards.size()).isEqualTo(200);
        assertThat(chessBoards.get(0)).isNotNull();
        assertThat(chessBoards.get(0).getDNA().length).isEqualTo(12);
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

        eightQueensEnvironment = new EightQueensEnvironment(8, 200);
        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(0);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(200);

        eightQueensEnvironment.processToNextGeneration();

        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(1);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(200);

    }

    @Test
    public void canProcessNGeneration() {

        eightQueensEnvironment = new EightQueensEnvironment(100, 12);

        eightQueensEnvironment.processNGeneration(10);

        assertThat(eightQueensEnvironment.getGenerationNumber()).isEqualTo(10);
        assertThat(eightQueensEnvironment.getCurrentPopulationSize()).isEqualTo(12);
    }

    @Test
    public void canStopWhenAPerfectBoardIsFound() {
        eightQueensEnvironment = new EightQueensEnvironment(6, 52);

        eightQueensEnvironment.processNGeneration(10000);
        assertThat(eightQueensEnvironment.getGenerationNumber()).isLessThan(10000);

    }

}
