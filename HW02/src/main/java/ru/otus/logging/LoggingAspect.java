package ru.otus.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.model.Question;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(ru.otus.logging.LoggingData)")
    public void logBefore(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.debug("call method: {}, {}, {}", joinPoint.getThis().getClass().getName(),joinPoint.getTarget().getClass().getName(), signature);
    }

    @AfterReturning (value = "@annotation(ru.otus.logging.LoggingData)", returning = "questions")
    public void  logAfter (JoinPoint joinPoint, List<Question> questions) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if ((questions != null) && (questions.size()>0)) {
            logger.debug("return from method {}, {}, {}", joinPoint.getThis().getClass().getName(), joinPoint.getTarget().getClass().getName(), signature);
            logger.debug("result: {}, {}",  questions.stream().map(Objects::toString).collect(Collectors.joining(";","[","]")));

        } else {
            logger.debug("Error in  {}, {}, {}", joinPoint.getThis().getClass().getName(), joinPoint.getTarget().getClass().getName(), signature);
        }
    }
}
