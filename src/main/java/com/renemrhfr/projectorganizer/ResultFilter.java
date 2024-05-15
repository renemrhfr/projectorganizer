package com.renemrhfr.projectorganizer;

public class ResultFilter {

    static boolean includeDone = false;
    static String textFilter = "";
    static boolean tag1 = false;
    static boolean tag2 = false;
    static boolean tag3 = false;
    static boolean tag4 = false;
    static boolean tag5 = false;

    public static boolean isIncludeDone() {
        return includeDone;
    }

    public static void setIncludeDone(boolean includeDone) {
        ResultFilter.includeDone = includeDone;
    }

    public static String getTextFilter() {
        return textFilter;
    }

    public static void setTextFilter(String textFilter) {
        ResultFilter.textFilter = textFilter;
    }


    public static boolean isTag1() {
        return tag1;
    }

    public static void setTag1(boolean tag1) {
        ResultFilter.tag1 = tag1;
    }

    public static boolean isTag2() {
        return tag2;
    }

    public static void setTag2(boolean tag2) {
        ResultFilter.tag2 = tag2;
    }

    public static boolean isTag3() {
        return tag3;
    }

    public static void setTag3(boolean tag3) {
        ResultFilter.tag3 = tag3;
    }

    public static boolean isTag4() {
        return tag4;
    }

    public static void setTag4(boolean tag4) {
        ResultFilter.tag4 = tag4;
    }

    public static boolean isTag5() {
        return tag5;
    }

    public static void setTag5(boolean tag5) {
        ResultFilter.tag5 = tag5;
    }

    private ResultFilter() {
    }

}
