package jpolanco.springbootapp.event.domain.model.categories;

public class Music implements Category{
    private static final String MUSIC = "MUSIC";

    @Override
    public String getValue() {
        return MUSIC;
    }
}
