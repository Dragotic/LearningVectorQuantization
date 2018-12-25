package lvq;

public class CompetitiveNeuron {

    private Point point;
    private Point weight;

    CompetitiveNeuron (Point point, Point weight) {
        this.point = point;
        this.weight = weight;
    }

    public Point getPoint() {
        return point;
    }

    public Point getWeight() {
        return weight;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setWeight(Point weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return point +
                "with weight= " + weight;
    }
}
