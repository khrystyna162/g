import java.util.LinkedHashMap;
import java.util.Map;


public class Database {


    public static final Map<Integer, Map<String, Object>> articles = new LinkedHashMap<>();


    public static final Map<Integer, Map<String, Object>> comments = new LinkedHashMap<>();


    public static int articleIdSeq = 1;
    public static int commentIdSeq = 1;
}