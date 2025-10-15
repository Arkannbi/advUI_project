package settings;
import java.awt.*;

public class Settings {
    public Color baseColor = Color.DARK_GRAY;
    public Color secondaryColor = Color.GRAY;
    public Color tertiaryColor = Color.BLACK;
    
    public Color portColorInput = Color.BLUE;
    public Color portColorOutput = Color.RED;

    public Color portColorInActivation = Color.CYAN;
    public Color portColorOutActivation = Color.MAGENTA;

    public Color textColor = Color.WHITE;
    
    public Color buttonColor = new Color(30,30,80);


    private static final Settings instance = new Settings();

    private Settings() {}

    public static Settings getInstance() {
        return instance;
    }
}
