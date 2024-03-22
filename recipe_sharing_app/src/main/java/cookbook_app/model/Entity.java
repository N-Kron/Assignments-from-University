package cookbook_app.model;

public interface Entity {
    public boolean commit();
    public void update();
    public void delete();
}
