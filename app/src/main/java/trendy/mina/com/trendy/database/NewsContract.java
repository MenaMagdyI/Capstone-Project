package trendy.mina.com.trendy.database;

/**
 * Created by Mena on 3/21/2018.
 */
import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;

public class NewsContract {

    public interface SourceEntry {
        @PrimaryKey @DataType(DataType.Type.TEXT) String _ID = "_id";
        @DataType(DataType.Type.TEXT) String NAME = "name";
    }

    public interface ArticleEntry {
        @AutoIncrement @PrimaryKey @DataType(DataType.Type.INTEGER) String  _ID = "_id";
        @DataType(DataType.Type.TEXT) String SOURCE_ID = "source_id";
        @DataType(DataType.Type.TEXT) String SORTED_BY = "sorted_by";
        @DataType(DataType.Type.TEXT) String AUTHOR = "author";
        @DataType(DataType.Type.TEXT) String TITLE = "title";
        @DataType(DataType.Type.TEXT) String DESCRIPTION = "description";
        @DataType(DataType.Type.TEXT) String URL = "url";
        @DataType(DataType.Type.TEXT) String IMAGE_URL = "image_url";
        @DataType(DataType.Type.TEXT) String DATE = "date";
    }

    @Database(version = NewsDatabase.VERSION)
    public final class NewsDatabase {
        public static final int VERSION = 1;
        @Table(SourceEntry.class) public static final String SOURCES = "sources";
        @Table(ArticleEntry.class) public static final String ARTICLES = "articles";
    }
}
