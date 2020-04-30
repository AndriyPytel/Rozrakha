package ua.nulp.rozrhakha.ga;

import ua.nulp.rozrhakha.utils.Function;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.IntStream;

public class Generation  {

    static final int DEFAULT_SIZE = 100;
    static final int DEFAULT_MULTIPLY_NUMBER = (int) (DEFAULT_SIZE * 0.5);
    static final double DEFAULT_MUTATION_PROP = 0.01;
    static final double DEFAULT_LIMIT = 1000;

    private static int size = DEFAULT_SIZE;
    private static int multiplyNumber = DEFAULT_MULTIPLY_NUMBER;
    private static double mutationProp = DEFAULT_MUTATION_PROP;

    private static Instance instancePattern;
    private static Function function;
    private static int params;
    private static boolean isParallel;

    private ArrayList<Instance> population;
    private HashMap<Double, Instance> multiplyFrequencies;



    public static void setFunction(Function function, int params){
        Generation.params = params;
        Generation.function = function;
    }

    public static void setInstancePattern(Instance instancePattern) {
        Generation.instancePattern = instancePattern;
    }

    public static void setSize(int size) {
        Generation.size = size;
    }

    public static void setMultiplyNumber(int multiplyNumber) {
        Generation.multiplyNumber = multiplyNumber;
    }

    public static void setMutationProp(double mutationProp) {
        Generation.mutationProp = mutationProp;
    }

    public static void setIsParallel(boolean isParallel) {
        Generation.isParallel = isParallel;
    }

    public static void setGenerationParams(int size, int multiplyNumber, double mutationProp) {
        setSize(size);
        setMultiplyNumber(multiplyNumber);
        setMutationProp(mutationProp);
    }
    private Generation() {
        population = new ArrayList<>();
        multiplyFrequencies = new HashMap<>();
    }

    static Generation generateFirstGeneration(ArrayList<Entry<Double, Double>> limits) {
        Generation first = new Generation();
        Random random = new Random();
        IntStream range = IntStream.range(0, multiplyNumber);

        if (isParallel) {
            range = range.parallel();
        }
        range.forEach(i -> {
            ArrayList<Double> values = new ArrayList<>(params);

            for (int j = 0; j < params; j++) {
                double left = limits.get(j).getKey();
                double right = limits.get(j).getValue();
                values.add(left + random.nextDouble()* (right - left));
            }
            Instance instance = Instance.createByPattern(instancePattern, values);

            assert instance != null;
            instance.setValues(values);

            first.add(instance);
        });

        return first;
    }

    public static Generation generateFirstGeneration() {
        ArrayList<Entry<Double, Double>> limits =
                new ArrayList<>(Collections.nCopies(params,
                        new SimpleEntry<Double, Double>(-DEFAULT_LIMIT, DEFAULT_LIMIT)));
        return generateFirstGeneration(limits);
    }

    public static Generation evolution(Generation generation, int generationCount) {
        for (int i = 0; i < generationCount; i++) {
            generation = generation.nextGeneration();
        }
        return generation;
    }

    public Generation nextGeneration() {
        this.multiply();

        Generation nextGen = new Generation();
        nextGen.add(population.subList(0, size));

        return nextGen;
    }


    public void add(Instance instance) {
        double[] values = instance.getValues().stream().mapToDouble(Double::doubleValue).toArray();
        instance.setLoss(function.calculate(values));

        this.population.add(instance);
    }

    private void add(List<Instance> population) {
        this.population.addAll(population);
    }

    private void normalize() {
        double sum = population.stream().mapToDouble(Instance::getLoss).sum();
        double pointer = 0;

        for(Instance i: population) {
            pointer += i.getLoss() / sum;
            multiplyFrequencies.put(pointer, i);
        }
    }
    private Instance findByProp(double prop) {
        ArrayList<Double> props = new ArrayList<Double>(this.multiplyFrequencies.keySet());
        props.sort(Double::compareTo);

        int l = 0;
        int r = props.size() - 1;

        while (l < r) {
            int mid = (l + r) / 2;
            if (props.get(mid) < prop) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        return multiplyFrequencies.get(props.get(l));
    }

    private void multiply() {
        this.normalize();

        IntStream range = IntStream.range(0, multiplyNumber);

        if (isParallel) {
            range = range.parallel();
        }

        range.forEach(i -> {
            Random random = new Random();
            Instance parent1 = findByProp(random.nextDouble());
            Instance parent2 = findByProp(random.nextDouble());
            Instance child = parent1.crossover(parent2);

            if (random.nextDouble() <= mutationProp) {
                child.mutate();
            }
            this.add(child);
        });

        Collections.sort(population);
    }
}
