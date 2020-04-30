package ua.nulp.rozrhakha;


import ua.nulp.rozrhakha.ga.Generation;
import ua.nulp.rozrhakha.ga.MeansInstance;
import ua.nulp.rozrhakha.utils.Function;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Function f = (x) -> x[0] * x[0] + x[1] * x[1] ;
        Generation.setSize(10);
        Generation.setFunction(f, 2);
        Generation.setInstancePattern(new MeansInstance());
        Generation lastGen =  Generation.evolution(Generation.generateFirstGeneration(), 100);

    }
}
