package eightqueens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: Eptwalabha
 * Date: 12/03/14
 * Time: 18:57
 */
public class ChessBoard implements Evolving<ChessBoard> {

    private int boardLength;
    private int[] dna;

    public ChessBoard(int boardLength) {
        this.boardLength = (boardLength > 3) ? boardLength : 8;
        dna = new int[boardLength];
        for (int i = 0; i < boardLength; i++)
            dna[i] = 0;
    }

    public ChessBoard(int[] dna, int DNASize) {
        this(DNASize);
        setDNA(dna);
    }

    public ChessBoard(int[] dna) {
        this(dna.length);
        setDNA(dna);
    }

    public void moveQueenTo(int column, int row) {
        column = normalize(column);
        row = normalize(row);
        dna[column] = row;
    }

    public int retrieveQueenPosition(int column) {
        column = normalize(column);
        return dna[column];
    }

    public int getNumberOfThreats() {
        int numberOfThreats = 0;

        for (int queen = 0; queen < boardLength; queen++)
            numberOfThreats += getNumberOfThreats(queen);

        return numberOfThreats;
    }

    public int getNumberOfThreats(int queen) {

        int numberOfThreats = 0;
        int queenPosition = dna[queen];
        int otherQueenPosition;
        for (int i = 0; i < boardLength; i++) {
            if (i == queen)
                continue;
            otherQueenPosition = dna[i];

            if (otherQueenPosition == queenPosition || Math.abs(queenPosition - otherQueenPosition) == Math.abs(queen - i))
                numberOfThreats++;

        }
        return numberOfThreats;
    }

    private int normalize(int number) {
        if (number > boardLength - 1)
            number = boardLength - 1;
        if (number < 0)
            number = 0;
        return number;
    }

    @Override
    public void configureWithRandomizeDNA() {
        int[] initialDNA = dna.clone();
        List<Integer> dna2 = new ArrayList<Integer>();

        do {
            for (int i = 0; i < boardLength; i++)
                dna2.add(i);

            Collections.shuffle(dna2);
            for (int i = 0; i < boardLength; i++)
                dna[i] = dna2.get(i);
        } while (Arrays.equals(dna, initialDNA));
    }

    @Override
    public void setDNA(int[] dna) {

        this.dna = new int[boardLength];
        int newDNASize = dna.length;

        for (int i = 0; i < boardLength; i++)
            this.dna[i] = (i < newDNASize && dna[i] > 0 && dna[i] < boardLength) ? dna[i] : 0;

    }

    @Override
    public int[] getDNA() {
        return dna;
    }

    @Override
    public String getStringDNA() {
        String stringDNA = "";
        for (int position : dna)
            stringDNA += (stringDNA.length() > 0 ? "-" : "") + position;
        return stringDNA;
    }

    @Override
    public void mutate() {
        int[] initialGenome = dna.clone();

        do {
            dna[((int) (Math.random() * boardLength))] = (int) (Math.random() * boardLength);
        } while (Arrays.equals(initialGenome, dna));
    }

    @Override
    public ChessBoard[] reproduce(ChessBoard partner, int pivot) {

        int[] configurationChildren1 = new int[boardLength];
        int[] configurationChildren2 = new int[boardLength];

        for (int i = 0; i < boardLength; i++) {
            configurationChildren1[i] = (i < pivot) ? dna[i] : partner.dna[i];
            configurationChildren2[i] = (i < pivot) ? partner.dna[i] : dna[i];
        }

        ChessBoard[] chessBoards = new ChessBoard[2];
        chessBoards[0] = new ChessBoard(configurationChildren1);
        chessBoards[1] = new ChessBoard(configurationChildren2);

        return chessBoards;
    }

    @Override
    public int getDNALength() {
        return boardLength;
    }

    @Override
    public float getFitness() {
        return 1f - getNumberOfThreats() / (float) (boardLength * (boardLength - 1));
    }

    @Override
    public int compareTo(ChessBoard chessBoard) {
        float delta = chessBoard.getFitness() - getFitness();
        return (delta < 0) ? -1 : (delta == 0) ? 0 : 1;
    }
}
