package cz.vsb.fei.project.data;

import jdk.jfr.DataAmount;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;



import java.util.Random;

// DTO - Data transfer Object: jednoducha datova trida ma jen atributy
// zadne logicke metody
//slouzi k prenosu dat mezi vrstvami (napr. databaze <-> UI)

@Entity
@Table(name = "scores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nick;
    private int points;

    @Transient
    private static final Random RANDOM = new Random();

    public Score(String nick, int points){
        this.nick = nick;
        this.points = points;
    }


    public Score(int points) {
        this.points = points;
        this.nick = getRandomNick();
    }

    @Override
    public String toString() {
        return nick + ": " + points;
    }


    public static Score randomScore() {
        return new Score(getRandomNick(), RANDOM.nextInt(1000));
    }

    public static String getRandomNick() {
        return nicks[RANDOM.nextInt(nicks.length)];
    }

    @Transient
    public static final String[] nicks = { "CyberSurfer", "PixelPioneer", "SocialSavvy", "DigitalDynamo", "ByteBuddy", "InstaGuru",
            "TikTokTornado", "SnapMaster", "TweetTrendsetter", "ChatChampion", "HashtagHero", "EmojiEnthusiast",
            "StoryStylist", "SelfieStar", "FilterFanatic", "VlogVirtuoso", "Memelord", "InfluencerInsider",
            "StreamSupreme", "GeekyGizmo", "CodeCommander", "JavaJuggernaut", "ByteNinja", "SyntaxSamurai",
            "ClassyCoder", "ObjectOmnipotent", "LoopLegend", "VariableVirtuoso", "DebugDemon", "CompilerCrusader",
            "PixelProdigy", "VirtualVoyager", "AlgorithmAce", "DataDynamo", "ExceptionExpert", "BugBuster",
            "SyntaxSorcerer", "CodeCrusader", "JavaJester", "NerdyNavigator", "CryptoCaptain", "SocialButterfly",
            "AppArchitect", "WebWizard", "FunctionFreak", "PixelArtist", "CyberPhantom", "HackHero", "CacheChampion",
            "ScreenSage", "WebWeaver", "LogicLover", "BitBlazer", "NetworkNomad", "ProtocolPioneer", "BinaryBoss",
            "StackSultan", "SocialScribe", "RenderRuler", "ScriptSorcerer", "HTMLHero", "PixelProwler", "FrameFreak",
            "DataDreamer", "BotBuilder", "ByteBishop", "KeyboardKnight", "DesignDaredevil", "JavaJuggler",
            "SyntaxStrategist", "TechTactician", "ProgramProdigy", "BinaryBard", "PixelPoet", "GigabyteGuru",
            "TechTrekker", "NetworkNinja", "DataDetective", "MatrixMaster", "CodeConductor", "AppAlchemist",
            "ServerSage", "ClusterChampion", "ScriptSensei", "KeyboardKicker", "CacheCrafter", "SocialSpark",
            "BinaryBeast", "CodeConnoisseur", "BitBrain", "VirtualVanguard", "SystemSculptor", "RenderRogue",
            "CryptoConqueror", "MachineMonarch", "PixelPal", "CompilerCaptain", "BitBuilder", "TechTitan",
            "CloudConqueror", "EchoExplorer", "FunctionFanatic", "RobotRanger" };
}


