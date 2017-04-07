package com.android.melanieh.dignitymemorialandroid.content;

import com.android.melanieh.dignitymemorialandroid.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MenuContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<MenuItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, MenuItem> ITEM_MAP = new HashMap<String, MenuItem>();

    private static final int COUNT = 6;

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

    private static MenuItem createMenuItem(int position) {
        return new MenuItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        switch (position) {
            case 1:
                return "Search Obituaries";
            case 2:
                return "Search Providers";
            case 3:
//                return "http://www.dignitymemorial.com/en-us/plan-now/funeral-checklist.page";
                return BuildConfig.TTR_CHECKLIST_URL;
            case 4:
                return "Start Planning a Service";
            case 5:
                return "Find a loved one's site";
            case 6:
//                return "https://www.paymentservicenetwork.com/Login.aspx?acc=CP19858";
                return BuildConfig.BILL_PAY_URL;

            default:
                return builder.append("Details about Item: ").append(position).toString();

//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
        }
//        return builder.toString();
    }

    private static String makeContent(int position) {
        StringBuilder builder = new StringBuilder();
        // specify temp content for detail fragment
        switch (position) {
            case 1:
                return "Search Obituaries";
            case 2:
                return "Search Providers";
            case 3:
                return "\'Things to Remember\' Checklist";
            case 4:
                return "Start Planning a Service";
            case 5:
                return "Find a loved one's site";
            case 6:
                return "Bill Pay";
            default:
                return builder.append("Item: ").append(position).toString();


//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
        }
//        return builder.toString();
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
