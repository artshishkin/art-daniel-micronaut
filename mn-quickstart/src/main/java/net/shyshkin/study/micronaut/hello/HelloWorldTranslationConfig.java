package net.shyshkin.study.micronaut.hello;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("hello.world.translation")
public interface HelloWorldTranslationConfig {

    //de
    String getDe();

    //en
    String getEn();
}
