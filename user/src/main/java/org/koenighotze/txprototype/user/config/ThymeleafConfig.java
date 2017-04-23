package org.koenighotze.txprototype.user.config;

import nz.net.ultraq.thymeleaf.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.thymeleaf.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.*;
import org.thymeleaf.*;
import org.thymeleaf.extras.java8time.dialect.*;
import org.thymeleaf.extras.springsecurity4.dialect.*;
import org.thymeleaf.spring4.*;
import org.thymeleaf.spring4.templateresolver.*;
import org.thymeleaf.spring4.view.*;
import org.thymeleaf.templateresolver.*;

@Configuration
//@ConditionalOnClass({SpringTemplateEngine.class})
@EnableConfigurationProperties({ThymeleafProperties.class})
//@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class ThymeleafConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ThymeleafProperties properties;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setOrder(2147483642);
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    //made this @Bean (vs private in Thymeleaf migration docs ), otherwise MessageSource wasn't autowired.
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.addDialect(new Java8TimeDialect());
        engine.addDialect(new LayoutDialect());
        engine.addDialect(new SpringSecurityDialect());
//        engine.addDialect(new SpringStandardDialect());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(properties.getPrefix());
        resolver.setSuffix(properties.getSuffix());
        resolver.setTemplateMode(properties.getMode());
        resolver.setCacheable(properties.isCache());
        return resolver;
    }

}
