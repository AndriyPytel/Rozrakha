package ua.nulp.rozrhakha.ga;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class Instance implements Cloneable, Comparable<Instance> {

    private double loss;
    private double normalized;

    private ArrayList<Double> values;

    public abstract Instance crossover(Instance other);
    public abstract void mutate();

    public static Instance createByPattern(Instance instance, ArrayList<Double> values) {
        try {
            return instance.getClass().getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }



    public double getNormalized() {
        return normalized;
    }

    public void setNormalized(double normalized) {
        this.normalized = normalized;
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
        this.loss = loss;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    @Override
    public int compareTo(Instance o) {
        return Double.compare(this.getLoss(), o.getLoss());
    }
}
        