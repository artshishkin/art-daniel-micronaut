package net.shyshkin.study.micronaut.graalvm;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;
import org.joda.time.DateTime;

import java.util.Optional;

@Singleton
public class JodaTimeConverter implements TypeConverter<String, DateTime> {

    @Override
    public Optional<DateTime> convert(String object, Class<DateTime> targetType, ConversionContext context) {
        return Optional.of(DateTime.now());
    }
}
