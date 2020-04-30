package ua.nulp.rozrhakha.ga;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class MeansInstance extends Instance {


    @Override
    public Instance crossover(Instance other) {
        ArrayList<Double> values = new ArrayList<>(this.getValues().size());

        for (int i = 0; i < this.getValues().size(); i++) {
            values.add(i, (this.getValues().get(i) + other.getValues().get(i)) / 2);
        }
        Instance child = new MeansInstance();
        child.setValues(values);

        return child;
    }

    @Override
    public void mutate() {
        Random random = new Random();
        int index = random.nextInt(this.getValues().size());
        ArrayList<Double> newValues = this.getValues();
        double mean = newValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        newValues.set(index, newValues.get(index) + random.nextGaussian() * abs(newValues.get(index) - mean));
        this.setValues(newValues);
    }

}
