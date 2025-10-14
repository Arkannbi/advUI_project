package settings;
import java.awt.*;

public class Settings {
    public Color baseColor = Color.DARK_GRAY;
    public Color secondaryColor = Color.GRAY;
    public Color tertiaryColor = Color.BLACK;
    
    public Color textColor = Color.WHITE;


    private static final Settings instance = new Settings();

    private Settings() {}

    public static Settings getInstance() {
        return instance;
    }
}
