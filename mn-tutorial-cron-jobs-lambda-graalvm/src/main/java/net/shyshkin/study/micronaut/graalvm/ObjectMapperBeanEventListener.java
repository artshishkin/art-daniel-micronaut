package net.shyshkin.study.micronaut.graalvm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ObjectMapperBeanEventListener implements BeanCreatedEventListener<ObjectMapper> {

    public static final Logger log = LoggerFactory.getLogger(ObjectMapperBeanEventListener.class);

    @Override
    public ObjectMapper onCreated(@NonNull BeanCreatedEvent<ObjectMapper> event) {
        final ObjectMapper mapper = event.getBean();
        SimpleModule module = new SimpleModule("ArtJacksonModule");
        module.addDeserializer(DateTime.class, new JodaTimeDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
