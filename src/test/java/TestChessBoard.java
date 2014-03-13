import eightqueens.ChessBoard;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

/**
 * User: Eptwalabha
 * Date: 12/03/14
 * Time: 18:54
 */
public class TestChessBoard {

    private ChessBoard chessBoard;

    @Before
    public void setUp() {
        chessBoard = new ChessBoard(8);
    }

    @Test
    public void canCreateABasicBoard() {
        assertThat(chessBoard.getDNALength()).isEqualTo(8);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-0-0-0-0-0-0-0");
    }

    @Test
    public void canCreateABoardFromAnEmptyString() {
        chessBoard = new ChessBoard(new int[0], 8);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-0-0-0-0-0-0-0");
    }

    @Test
    public void canCreateABoardFromAGivenSequence() {

        chessBoard = new ChessBoard(getDisposition(0, 0, 0, 0, 1, 0, 0, 0));
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-0-0-0-1-0-0-0");

        chessBoard = new ChessBoard(getDisposition(0, 0, 2, 0, 1, 0, 0, 0), 4);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-0-2-0");
    }

    private int[] getDisposition(int... genomes) {
        return genomes;
    }

    @Test
    public void canCreateABoardFromAStringWithLessThanEightCharacters() {
        chessBoard = new ChessBoard(getDisposition(1, 1), 8);
        assertThat(chessBoard.getStringDNA()).isEqualTo("1-1-0-0-0-0-0-0");
    }

    @Test
    public void canCreateABoardFromAStringWithMoreCharactersThanTheBoardLength() {
        chessBoard = new ChessBoard(getDisposition(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), 8);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-1-2-3-4-5-6-7");

        chessBoard = new ChessBoard(getDisposition(0, 1, 2, 3, 4, 5, 6, 7), 6);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-1-2-3-4-5");
    }

    @Test
    public void canReplaceANonValidCharacterByZero() {
        chessBoard = new ChessBoard(getDisposition(0, -1, -2, -3, 2, -5, 1, 9), 8);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-0-0-0-2-0-1-0");
    }

    @Test
    public void canSetTheBoard() {
        chessBoard.moveQueenTo(1, 1);
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-1-0-0-0-0-0-0");

        chessBoard.moveQueenTo(-1, 2);
        assertThat(chessBoard.getStringDNA()).isEqualTo("2-1-0-0-0-0-0-0");

        chessBoard.moveQueenTo(10, 3);
        assertThat(chessBoard.getDNA()).isEqualTo(getDisposition(2, 1, 0, 0, 0, 0, 0, 3));
    }

    @Test
    public void canSetTheBoardWithAString() {
        chessBoard.setDNA(getDisposition(0, 1, 2, 3, 4, 5, 6, 7));
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-1-2-3-4-5-6-7");
    }

    @Test
    public void canSetupARandomConfiguration() {
        chessBoard.setDNA(getDisposition(0, 1, 2, 3, 4, 5, 6, 7));
        assertThat(chessBoard.getStringDNA()).isEqualTo("0-1-2-3-4-5-6-7");

        chessBoard.configureWithRandomizeDNA();
        assertThat(chessBoard.getStringDNA()).isNotEqualTo("0-1-2-3-4-5-6-7");
    }

    @Test
    public void canRetrieveQueenPosition() {
        String configuration = "";
        for (int i = 0; i < 8; i++) {
            int position = (int) (Math.random() * 8);
            configuration += (configuration.length() > 0 ? "-" : "") + position;
            chessBoard.moveQueenTo(i, position);
            assertThat(chessBoard.retrieveQueenPosition(i)).isEqualTo(position);
        }
        assertThat(chessBoard.getStringDNA()).isEqualTo(configuration);
    }

    @Test
    public void canChessBoardMutate() {
        String oldConfiguration = chessBoard.getStringDNA();
        chessBoard.mutate();
        String newConfiguration = chessBoard.getStringDNA();
        assertThat(newConfiguration).isNotEqualTo(oldConfiguration);
    }

    @Test
    public void canTwoChessBoardsCanGenerateTwoOtherChessBoardsBasedOnThemGeneticConfigurationAndAPivot() {
        ChessBoard[] chessBoards = chessBoard.reproduce(new ChessBoard(getDisposition(2, 2, 2, 2, 2, 2, 2, 2)), 4);
        assertThat(chessBoards.length).isEqualTo(2);
        assertThat(chessBoards[0].getStringDNA()).isEqualTo("0-0-0-0-2-2-2-2");
        assertThat(chessBoards[1].getStringDNA()).isEqualTo("2-2-2-2-0-0-0-0");

        chessBoards = chessBoard.reproduce(new ChessBoard(getDisposition(0, 1, 2, 3, 4, 5, 6, 7)), 2);
        assertThat(chessBoards[0].getStringDNA()).isEqualTo("0-0-2-3-4-5-6-7");
        assertThat(chessBoards[1].getStringDNA()).isEqualTo("0-1-0-0-0-0-0-0");
    }

    @Test
    public void canDetermineHowManyQueensAQueenIsThreatening() {
        chessBoard.setDNA(getDisposition(3, 0, 0, 0, 0, 0, 0, 3));
        assertThat(chessBoard.getNumberOfThreats(0)).isEqualTo(2);

        chessBoard.setDNA(getDisposition(3, 5, 2, 3, 2, 5, 3, 3));
        assertThat(chessBoard.getNumberOfThreats(3)).isEqualTo(7);

        chessBoard.setDNA(getDisposition(3, 5, 2, 3, 2, 5, 4, 4));
        assertThat(chessBoard.getNumberOfThreats(3)).isEqualTo(5);
    }

    @Test
    public void cannotFindAnyThreatForAPerfectChessBoard() {
        chessBoard.setDNA(getDisposition(3, 5, 7, 1, 6, 0, 2, 4));
        assertThat(chessBoard.getNumberOfThreats()).isEqualTo(0);
        assertThat(chessBoard.getFitness()).isEqualTo(1f);
    }

    @Test
    public void canDetermineTheTotalNumberOfThreatFromAChessBoard() {
        chessBoard.setDNA(getDisposition(0, 0, 0, 0, 0, 0, 0, 0));
        assertThat(chessBoard.getNumberOfThreats()).isEqualTo(8 * 7);
        assertThat(chessBoard.getFitness()).isEqualTo(0f);

        chessBoard.setDNA(getDisposition(1, 0, 0, 0, 0, 0, 0, 0));
        int numberOfThread = 1 + 7 + 6 + 6 + 6 + 6 + 6 + 6;
        assertThat(chessBoard.getNumberOfThreats()).isEqualTo(numberOfThread);
        assertThat(chessBoard.getFitness()).isEqualTo(1f - numberOfThread / (8f * 7f));
    }

}