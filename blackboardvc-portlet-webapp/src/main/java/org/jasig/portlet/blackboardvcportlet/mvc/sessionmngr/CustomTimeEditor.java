package org.jasig.portlet.blackboardvcportlet.mvc.sessionmngr;

import java.beans.PropertyEditorSupport;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

public class CustomTimeEditor extends PropertyEditorSupport {

    private final DateTimeFormatter dateFormat;

    private final boolean allowEmpty;

    private final int exactDateLength;

    /**
     * Create a new CustomDateEditor instance, using the given DateFormat for
     * parsing and rendering.
     * <p>
     * The "allowEmpty" parameter states if an empty String should be allowed
     * for parsing, i.e. get interpreted as null value. Otherwise, an
     * IllegalArgumentException gets thrown in that case.
     * 
     * @param dateFormat
     *            DateFormat to use for parsing and rendering
     * @param allowEmpty
     *            if empty strings should be allowed
     */
    public CustomTimeEditor(DateTimeFormatter dateFormat,
            boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    /**
     * Create a new CustomDateEditor instance, using the given DateFormat for
     * parsing and rendering.
     * <p>
     * The "allowEmpty" parameter states if an empty String should be allowed
     * for parsing, i.e. get interpreted as null value. Otherwise, an
     * IllegalArgumentException gets thrown in that case.
     * <p>
     * The "exactDateLength" parameter states that IllegalArgumentException gets
     * thrown if the String does not exactly match the length specified. This is
     * useful because SimpleDateFormat does not enforce strict parsing of the
     * year part, not even with <code>setLenient(false)</code>. Without an
     * "exactDateLength" specified, the "01/01/05" would get parsed to
     * "01/01/0005".
     * 
     * @param dateFormat
     *            DateFormat to use for parsing and rendering
     * @param allowEmpty
     *            if empty strings should be allowed
     * @param exactDateLength
     *            the exact expected length of the date String
     */
    public CustomTimeEditor(DateTimeFormatter dateFormat,
            boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0
                && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly"
                            + this.exactDateLength + "characters long");
        } else {
            setValue(this.dateFormat.parseDateTime(text).toLocalTime());
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        LocalTime value = (LocalTime) getValue();
        return (value != null ? this.dateFormat.print(value) : "");
    }

}
