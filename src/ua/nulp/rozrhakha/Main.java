package ua.nulp.rozrhakha;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Function f = (x) -> x[0] * x[0] + x[1] * x[1] ;
        Generation.setSize(10);
        Generation.setFunction(f, 2);
        Generation.setInstancePattern(new MeansInstance());

        Generation generation = Generation.generateFirstGeneration();

        for (int i = 0; i < 1000; i++) {
            generation = generation.nextGeneration();
        }

        System.out.println(f.calculate(1, 2));
    }
}
