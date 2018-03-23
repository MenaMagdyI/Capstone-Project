package trendy.mina.com.trendy.database;

/**
 * Created by Mena on 3/21/2018.
 */

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(authority = TrendyContentProvider.AUTHORITY, database = NewsContract.NewsDatabase.class)
public class TrendyContentProvider {
    public static final String AUTHORITY = "trendy.mina.com.trendy";

    @TableEndpoint(table = NewsContract.NewsDatabase.SOURCES)
    public static class Sources {
        @ContentUri(path = NewsContract.NewsDatabase.SOURCES, type = "vnd.android.cursor.dir/list")
        public static final Uri SOURCES = Uri.parse("content://" + AUTHORITY)
                .buildUpon()
                .appendPath(NewsContract.NewsDatabase.SOURCES)
                .build();
    }

    @TableEndpoint(table = NewsContract.NewsDatabase.ARTICLES)
    public static class Articles {
        @ContentUri(path = NewsContract.NewsDatabase.ARTICLES, type = "vnd.android.cursor.dir/list")
        public static final Uri ARTICLES = Uri.parse("content://" + AUTHORITY)
                .buildUpon()
                .appendPath(NewsContract.NewsDatabase.ARTICLES)
                .build();
    }
}
