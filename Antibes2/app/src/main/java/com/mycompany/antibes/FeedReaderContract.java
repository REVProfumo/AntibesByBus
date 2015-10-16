package com.mycompany.antibes;

import android.provider.BaseColumns;


public final class FeedReaderContract {
    public FeedReaderContract() {
    }
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String STOP = "entryid";
        public static final String LINE = "title";
        public static final String SCHEDULE = "schedule";
    }
}



