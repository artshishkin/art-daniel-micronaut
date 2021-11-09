package net.shyshkin.study.micronaut.hello;

import io.micronaut.context.annotation.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("hello.world.translation")
public interface HelloWorldTranslationConfig {

    //de
    @NotBlank
    String getDe();

    //en
    @NotBlank
    String getEn();
}
