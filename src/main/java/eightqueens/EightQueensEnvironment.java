package eightqueens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Eptwalabha
 * Date: 12/03/14
 * Time: 21:54
 */
public class EightQueensEnvironment {

    private int generationNumber;
    private List<ChessBoard> currentPopulation;

    public EightQueensEnvironment(int boardSize) {
        this(boardSize, boardSize * boardSize);
    }

    public EightQueensEnvironment(int boardSize, int populationSize) {
        if (boardSize < 4)
            boardSize = 4;

        currentPopulation = new ArrayList<ChessBoard>(populationSize);
        initialize(populationSize, boardSize);
        generationNumber = -1;
        processToNextGeneration();
    }

    public void initialize(int populationSize, int sizeOfTheBoard) {

        populationSize += (populationSize % 4 != 0) ? 4 - populationSize % 4 : 0;

        for (int i = 0; i < populationSize; i++) {
            ChessBoard chessBoard = new ChessBoard(sizeOfTheBoard);
            chessBoard.configureWithRandomizeDNA();
            currentPopulation.add(chessBoard);
        }
    }

    public int getCurrentPopulationSize() {
        return currentPopulation.size();
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public List<ChessBoard> getCurrentPopulation() {
        return currentPopulation;
    }

    public void processToNextGeneration() {

        // notation
        if (generationNumber == -1)
            Collections.sort(currentPopulation);

        List<ChessBoard> bestEntities = currentPopulation.subList(0, currentPopulation.size() / 2);

        // couplage
        List<ChessBoard> newPopulation = new ArrayList<ChessBoard>();

        double mutation = 0.1;

        for (int i = 0, size = bestEntities.size() / 2; i < size; i++) {

            ChessBoard entityA = bestEntities.remove(0);
            newPopulation.add(entityA);

            if (bestEntities.isEmpty())
                break;

            int indexPartner = (int) (Math.random() * bestEntities.size());
            ChessBoard entityB = bestEntities.remove(indexPartner);
            newPopulation.add(entityB);

            ChessBoard[] children = entityA.reproduce(entityB, (int) (Math.random() * entityB.getDNALength()));

            if (Math.random() < mutation)
                children[0].mutate();

            if (Math.random() < mutation)
                children[1].mutate();

            newPopulation.add(children[0]);
            newPopulation.add(children[1]);
        }

        currentPopulation = newPopulation;

        Collections.sort(currentPopulation);
        generationNumber++;
    }

    public void processNGeneration(int numberOfGenerationToProcess) {

        int counter = 0;
        while (currentPopulation.get(0).getFitness() != 1 && counter < numberOfGenerationToProcess) {
            processToNextGeneration();
            counter++;
        }

        System.out.println("best fitness for generation " + generationNumber + " : " + currentPopulation.get(0).getFitness() + " disposition = " + currentPopulation.get(0).getStringDNA());

    }
}
