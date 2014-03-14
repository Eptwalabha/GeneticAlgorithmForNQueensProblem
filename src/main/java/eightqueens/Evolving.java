package eightqueens;

/**
 * User: Eptwalabha
 * Date: 12/03/14
 * Time: 21:35
 */
public interface Evolving <T> extends Comparable<ChessBoard> {

    void setDNA(int[] dna);
    void configureWithRandomizeDNA();

    void mutate();
    T[] reproduce(T partner, int pivot);

    float getFitness();

    int[] getDNA();
    String getStringDNA();
    int getDNALength();

    float determineFitness();
}
