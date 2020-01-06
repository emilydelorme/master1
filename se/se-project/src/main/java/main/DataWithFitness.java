package main;

public class DataWithFitness {

    private Data data;
    private Double fitness;

    public DataWithFitness(final Data data, final Double fitness) {
        this.data = data;
        this.fitness = fitness;
    }

    public Data getData() {
        return this.data;
    }

    public Double getFitness() {
        return this.fitness;
    }

    public void setData(final Data data) {
        this.data = data;
    }

    public void setFitness(final Double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return "DataWithFitness{" +
                "data=" + this.data +
                ", fitness=" + this.fitness +
                '}';
    }
}

