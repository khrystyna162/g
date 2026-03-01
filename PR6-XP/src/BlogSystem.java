import java.util.List;
import java.util.Scanner;


public class BlogSystem {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║   🗞️  СИСТЕМА УПРАВЛІННЯ БЛОГОМ  ║");
        System.out.println("║       Патерн: Active Record       ║");
        System.out.println("╚══════════════════════════════════╝");

        seedData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            System.out.println();
            switch (choice) {
                case 1  -> showAllArticles();
                case 2  -> showArticleWithComments();
                case 3  -> createArticle();
                case 4  -> editArticle();
                case 5  -> deleteArticle();
                case 6  -> addComment();
                case 7  -> deleteComment();
                case 0  -> { running = false; System.out.println("👋 До побачення!"); }
                default -> System.out.println("❌ Невірний вибір.\n");
            }
        }
    }


    static void printMenu() {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│            МЕНЮ                 │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Показати всі статті         │");
        System.out.println("│  2. Переглянути статтю          │");
        System.out.println("│  3. Додати статтю               │");
        System.out.println("│  4. Редагувати статтю           │");
        System.out.println("│  5. Видалити статтю             │");
        System.out.println("│  6. Додати коментар до статті   │");
        System.out.println("│  7. Видалити коментар           │");
        System.out.println("│  0. Вийти                       │");
        System.out.println("└─────────────────────────────────┘");
    }


    static void showAllArticles() {
        List<Article> articles = Article.findAll();
        if (articles.isEmpty()) {
            System.out.println("📭 Статей немає.\n");
            return;
        }
        System.out.println("=== СПИСОК СТАТЕЙ ===");
        for (Article a : articles) {
            System.out.println(a + " | Коментарів: " + a.getComments().size());
        }
        System.out.println();
    }


    static void showArticleWithComments() {
        int id = readInt("Введіть ID статті: ");
        Article article = Article.findById(id);
        if (article == null) {
            System.out.println("❌ Статтю не знайдено.\n");
            return;
        }

        System.out.println("\n=== СТАТТЯ ===");
        System.out.println("ID:     " + article.getId());
        System.out.println("Назва:  " + article.getTitle());
        System.out.println("Автор:  " + article.getAuthor());
        System.out.println("Текст:  " + article.getContent());

        List<Comment> comments = article.getComments();
        System.out.println("\n--- Коментарі (" + comments.size() + ") ---");
        if (comments.isEmpty()) {
            System.out.println("  (немає коментарів)");
        } else {
            comments.forEach(System.out::println);
        }
        System.out.println();
    }


    static void createArticle() {
        System.out.println("=== НОВА СТАТТЯ ===");
        String title   = readString("Назва:  ");
        String content = readString("Текст:  ");
        String author  = readString("Автор:  ");
        Article article = new Article(title, content, author);
        article.save();
        System.out.println();
    }


    static void editArticle() {
        int id = readInt("Введіть ID статті для редагування: ");
        Article article = Article.findById(id);
        if (article == null) {
            System.out.println("❌ Статтю не знайдено.\n");
            return;
        }

        System.out.println("Поточна назва: " + article.getTitle());
        String newTitle = readString("Нова назва    (Enter — без змін): ");
        if (!newTitle.isBlank()) article.setTitle(newTitle);

        System.out.println("Поточний текст: " + article.getContent());
        String newContent = readString("Новий текст   (Enter — без змін): ");
        if (!newContent.isBlank()) article.setContent(newContent);

        article.save();
        System.out.println();
    }


    static void deleteArticle() {
        int id = readInt("Введіть ID статті для видалення: ");
        Article article = Article.findById(id);
        if (article == null) {
            System.out.println("❌ Статтю не знайдено.\n");
            return;
        }
        article.delete();
        System.out.println();
    }


    static void addComment() {
        int articleId = readInt("Введіть ID статті: ");
        Article article = Article.findById(articleId);
        if (article == null) {
            System.out.println("❌ Статтю не знайдено.\n");
            return;
        }
        String commenter = readString("Ваше ім'я:  ");
        String text      = readString("Коментар:   ");
        article.addComment(text, commenter);
        System.out.println();
    }


    static void deleteComment() {
        int id = readInt("Введіть ID коментаря для видалення: ");
        Comment comment = Comment.findById(id);
        if (comment == null) {
            System.out.println("❌ Коментар не знайдено.\n");
            return;
        }
        comment.delete();
        System.out.println();
    }


    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Введіть ціле число.");
            }
        }
    }


    static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }



    static void seedData() {

        Article a1 = new Article(
                "Вступ до Java",
                "Java — об'єктно-орієнтована мова програмування з сильною типізацією.",
                "Іван Петренко"
        );
        a1.save();

        Article a2 = new Article(
                "Патерни проектування",
                "Патерни — це типові рішення поширених задач у проектуванні ПЗ.",
                "Оксана Коваль"
        );
        a2.save();

        Article a3 = new Article(
                "Active Record у деталях",
                "Active Record поєднує дані та логіку доступу до БД в одному класі.",
                "Михайло Бондар"
        );
        a3.save();


        a1.addComment("Дуже корисна стаття!", "Аліна");
        a1.addComment("Дякую, зрозуміло пояснено.", "Сергій");
        a2.addComment("Хочу більше прикладів коду.", "Юлія");
        a3.addComment("Нарешті знайшов зрозумілий опис AR!", "Денис");

        System.out.println("\n📦 Початкові дані завантажено.\n");
    }
}