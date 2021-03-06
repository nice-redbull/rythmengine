package org.rythmengine.extension;

import org.rythmengine.template.ITemplate;
import org.rythmengine.utils.I18N;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface IDateFormatFactory {
    DateFormat createDateFormat(ITemplate template, String pattern, Locale locale, String timezone);

    class DateFormatKey {
        private Locale locale;
        private String timezone;
        private String pattern;

        public DateFormatKey(Locale locale, String timezone, String pattern) {
            this.locale = locale;
            this.timezone = timezone;
            this.pattern = pattern;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DateFormatKey that = (DateFormatKey) o;
            return Objects.equals(locale, that.locale) &&
                    Objects.equals(timezone, that.timezone) &&
                    Objects.equals(pattern, that.pattern);
        }

        @Override
        public int hashCode() {

            return Objects.hash(locale, timezone, pattern);
        }
    }

    class DefaultDateFormatFactory implements IDateFormatFactory {

        public static final IDateFormatFactory INSTANCE = new DefaultDateFormatFactory();

        private ConcurrentMap<DateFormatKey, DateFormat> cache = new ConcurrentHashMap<>();

        @Override
        public DateFormat createDateFormat(ITemplate template, String pattern, Locale locale, String timezone) {
            if (null == locale) {
                locale = I18N.locale(template);
            }
            DateFormat df;
            DateFormatKey key = new DateFormatKey(locale, timezone, pattern);
            df = cache.get(key);
            if (null == df) {
                if (null != pattern) {
                    df = new SimpleDateFormat(pattern, locale);
                } else {
                    df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
                }
                if (null != timezone) df.setTimeZone(TimeZone.getTimeZone(timezone));
                cache.putIfAbsent(key, df);
            }
            return df;
        }
    }
}
