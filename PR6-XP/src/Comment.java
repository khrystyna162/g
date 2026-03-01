import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Comment {

    private int    id;
    private int    articleId;
    private String text;
    private String commenter;


    public Comment(int articleId, String text, String commenter) {
        this.articleId = articleId;
        this.text      = text;
        this.commenter = commenter;
    }


    private Comment(int id, int articleId, String text, String commenter) {
        this.id        = id;
        this.articleId = articleId;
        this.text      = text;
        this.commenter = commenter;
    }


    public void save() {
        if (id == 0) {

            id = Database.commentIdSeq++;
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id",        id);
            row.put("articleId", articleId);
            row.put("text",      text);
            row.put("commenter", commenter);
            Database.comments.put(id, row);
            System.out.println("  ✅ Коментар збережено (ID=" + id + ")");
        } else {

            Map<String, Object> row = Database.comments.get(id);
            if (row != null) {
                row.put("text",      text);
                row.put("commenter", commenter);
                System.out.println("  ✅ Коментар оновлено (ID=" + id + ")");
            }
        }
    }


    public void delete() {
        Database.comments.remove(id);
        System.out.println("  🗑️  Коментар видалено (ID=" + id + ")");
    }


    public static Comment findById(int id) {
        Map<String, Object> row = Database.comments.get(id);
        if (row == null) return null;
        return fromRow(row);
    }


    public static List<Comment> findByArticleId(int articleId) {
        return Database.comments.values().stream()
                .filter(row -> (int) row.get("articleId") == articleId)
                .map(Comment::fromRow)
                .collect(Collectors.toList());
    }


    private static Comment fromRow(Map<String, Object> row) {
        return new Comment(
                (int)    row.get("id"),
                (int)    row.get("articleId"),
                (String) row.get("text"),
                (String) row.get("commenter")
        );
    }



    public int    getId()            { return id; }
    public int    getArticleId()     { return articleId; }
    public String getText()          { return text; }
    public String getCommenter()     { return commenter; }
    public void   setText(String t)  { this.text = t; }

    @Override
    public String toString() {
        return String.format("  💬 [ID=%d] %s: \"%s\"", id, commenter, text);
    }
}