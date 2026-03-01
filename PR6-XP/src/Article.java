import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Article {

    private int    id;
    private String title;
    private String content;
    private String author;


    public Article(String title, String content, String author) {
        this.title   = title;
        this.content = content;
        this.author  = author;
    }


    private Article(int id, String title, String content, String author) {
        this.id      = id;
        this.title   = title;
        this.content = content;
        this.author  = author;
    }


    public void save() {
        if (id == 0) {

            id = Database.articleIdSeq++;
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id",      id);
            row.put("title",   title);
            row.put("content", content);
            row.put("author",  author);
            Database.articles.put(id, row);
            System.out.println("✅ Статтю збережено (ID=" + id + ")");
        } else {

            Map<String, Object> row = Database.articles.get(id);
            if (row != null) {
                row.put("title",   title);
                row.put("content", content);
                row.put("author",  author);
                System.out.println("✅ Статтю оновлено (ID=" + id + ")");
            }
        }
    }


    public void delete() {
        Database.articles.remove(id);

        Database.comments.entrySet()
                .removeIf(e -> (int) e.getValue().get("articleId") == id);
        System.out.println("🗑️  Статтю видалено разом із коментарями (ID=" + id + ")");
    }


    public static Article findById(int id) {
        Map<String, Object> row = Database.articles.get(id);
        if (row == null) return null;
        return fromRow(row);
    }


    public static List<Article> findAll() {
        return Database.articles.values().stream()
                .map(Article::fromRow)
                .collect(Collectors.toList());
    }


    private static Article fromRow(Map<String, Object> row) {
        return new Article(
                (int)    row.get("id"),
                (String) row.get("title"),
                (String) row.get("content"),
                (String) row.get("author")
        );
    }


    public Comment addComment(String text, String commenter) {
        Comment c = new Comment(this.id, text, commenter);
        c.save();
        return c;
    }


    public List<Comment> getComments() {
        return Comment.findByArticleId(this.id);
    }


    public int    getId()                     { return id; }
    public String getTitle()                  { return title; }
    public String getContent()                { return content; }
    public String getAuthor()                 { return author; }
    public void   setTitle(String title)      { this.title = title; }
    public void   setContent(String content)  { this.content = content; }

    @Override
    public String toString() {
        return String.format("📄 [ID=%d] \"%s\" | Автор: %s", id, title, author);
    }
}