package lvq;

public class CompetitiveNeuron {

    private Point point;
    private Point weight;
    private int team;

    CompetitiveNeuron (Point point, Point weight, int team) {
        this.point = point;
        this.weight = weight;
        this.team = team;
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
                "with weight= " + weight +
                " and team= " + team;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
