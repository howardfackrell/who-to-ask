package com.hlf;

import java.util.*;


public class App 
{

    public static final String OMIT_AFTER = "2017-12-01";

    public static void main( String[] args ) throws Exception
    {
        List<String> names = CsvUtils.readFromCsv(
                CsvUtils.readLines("Prayers - Adults.csv"),
                (a) -> a[0]);

        List<Prayer> prayers = CsvUtils.readFromCsv(
                CsvUtils.readLines("Prayers - Prayers.csv"),
                (a) -> new Prayer(a[0], a[1]));

        List<Info> infos = CsvUtils.readFromCsv(
                CsvUtils.readLines("Prayers - Info.csv"),
                (a) -> new Info(a[0], a[1], a[2]));

        final Map<String, Info> infoMap = new HashMap<>();
        for (Info info : infos) {
            infoMap.put(info.name, info);
        }

        final Map<String, String> mostRecentPrayers = new HashMap<>();
        for (String name : names) {
            mostRecentPrayers.put(name, "2000-01-01");
        }
        for (Prayer prayer : prayers) {
            if (mostRecentPrayers.containsKey(prayer.name)) {
                if (mostRecentPrayers.get(prayer.name).compareTo(prayer.date) < 0) {
                    mostRecentPrayers.put(prayer.name, prayer.date);
                }
            }
        }

        List<Prayer> allAsks = new ArrayList<>();
        for (Map.Entry<String, String> entry : mostRecentPrayers.entrySet()) {
            allAsks.add(new Prayer(entry.getValue(), entry.getKey()));
        }

        allAsks.stream()
                .filter(p -> p.date.compareTo(OMIT_AFTER) < 0)
                .filter(p -> infoMap.containsKey(p.name) ? infoMap.get(p.name).possible : true)
                .sorted(new PrayerComparator())
                .forEach(p -> System.out.println(p.toString() + " " + infoMap.get(p.name)));
    }


    static class PrayerComparator implements Comparator<Prayer> {
        @Override
        public int compare(Prayer o1, Prayer o2) {
            int res = o1.date.compareTo(o2.date);
            if (res == 0) {
                res = o1.name.compareTo(o2.name);
            }
            return res;
        }
    }

    static class Prayer {
        final String date;
        final String name;
        public Prayer(final String date, final String name) {
            this.date = date;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Prayer{" +
                    "date='" + date + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        public String getDate() {
            return date;
        }

        public String getName() {
            return name;
        }
    }

    static class Info {
        final String name;
        final boolean possible;
        final String note;

        Info(String name, String possible, String note) {
            this.name = name;
            this.possible = Boolean.parseBoolean(possible);
            this.note = note;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "name='" + name + '\'' +
                    ", possible=" + possible +
                    ", note='" + note + '\'' +
                    '}';
        }
    }
}
