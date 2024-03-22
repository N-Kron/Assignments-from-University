package cookbook_app.model;

public class Content {
    private Ingredient ingr;
    private double value;

    public Content(Ingredient ingr, double value) {
        this.ingr = ingr;
        this.value = value; 
    }

    public Ingredient getIngr() {
        return ingr;
    }
    public double getValue() {
        return value;
    }
    public String getIngrPrimaryKey() {
        return ingr.getName();
    }
}
