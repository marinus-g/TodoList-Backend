package academy.mischok.todoapp.converter.impl;

import academy.mischok.todoapp.converter.EntityConverter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class DateConverter implements EntityConverter<Date, String> {

    private static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public Date convertToEntity(String s) {
        if (Objects.isNull(s)) {
            return null;
        }
        try {
            return df.parse(s);
        } catch (ParseException ignored) {
            return null;
        }
    }
}
