package ru.otus.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.model.Question;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("@annotation(ru.otus.logging.LoggingData)")
    public void logBefore(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.debug("call method: {}, {}, {}", joinPoint.getThis().getClass().getName(),
                joinPoint.getTarget().getClass().getName(), signature);
    }

    @AfterReturning(value = "@annotation(ru.otus.logging.LoggingData)", returning = "questions")
    public void logAfter(JoinPoint joinPoint, Object questions) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.debug("return from method {}, {}, {}", joinPoint.getThis().getClass().getName(),
                joinPoint.getTarget().getClass().getName(), signature);
        if (questions == null) {
            logger.debug("Data return is Null in {}, {}, {}", joinPoint.getThis().getClass().getName(),
                    joinPoint.getTarget().getClass().getName(), signature);
            return;
        }

        if (questions instanceof List<?> listOfQuestions) {
            if (listOfQuestions.isEmpty()) {
                logger.debug("Data return is Empty in {}, {}, {}", joinPoint.getThis().getClass().getName(),
                        joinPoint.getTarget().getClass().getName(), signature);
                return;
            }
            var msg = listOfQuestions.stream()
                    .filter(Question.class::isInstance)
                    .map(Question.class::cast)
                    .map(Object::toString)
                    .collect(Collectors.joining(";", "[", "]"));
            logger.debug("result: {}", msg);
        }
    }
}
