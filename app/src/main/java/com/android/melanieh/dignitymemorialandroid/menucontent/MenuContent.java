package com.android.melanieh.dignitymemorialandroid.menucontent;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Populates menu **/
public class MenuContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<MenuItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, MenuItem> ITEM_MAP = new HashMap<String, MenuItem>();

    private static final int COUNT = 5;

    static {
        // Populate menu item list
        for (int i = 1; i <= COUNT; i++) {
            addItem(new MenuItem("" + i, makeContent(i), makeDetails(i)));
        }
    }

    private static void addItem(MenuItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        switch (position) {
            case 1:
                return "Search Obituaries and Providers";
            case 2:
                return BuildConfig.TTR_CHECKLIST_URL;
            case 3:
                return "Start Planning a Service";
            case 4:
                return BuildConfig.BILL_PAY_URL;
            case 5:
                return "FAQs";
            default:
                return builder.append("Details about Item: ").append(position).toString();
        }
    }

    private static String makeContent(int position) {
        StringBuilder builder = new StringBuilder();
        // specify temp content for detail fragment
        switch (position) {
            case 1:
                return "Search Obituaries and Providers";
            case 2:
                return "\'Things to Remember\' Checklist";
            case 3:
                return "Start Planning a Service";
            case 4:
                return "Bill Pay";
            case 5:
                return "FAQs";
            default:
                return builder.append("Item: ").append(position).toString();
        }
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class MenuItem {
        public final String id;
        public final String content;
        public final String details;

        public MenuItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

}