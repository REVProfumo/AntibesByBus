package com.mycompany.antibes;

import android.provider.BaseColumns;


public final class FeedReaderContract {
    public FeedReaderContract() {
    }
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String TABLE_NAME2 = "entry_sat";
        public static final String TABLE_NAME3 = "entry_sun";

        public static final String STOP = "entryid";
        public static final String LINE = "title";
        public static final String DIRECTION = "direction";
        public static final String SCHEDULE = "schedule";
    }
}



