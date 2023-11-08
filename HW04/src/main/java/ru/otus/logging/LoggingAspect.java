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
        try {
            List<Question> questionList = (List<Question>) questions;
            if (!questionList.isEmpty()) {
                logger.debug("result: {}",
                        questionList.stream()
                                .map(Question::toString)
                                .collect(Collectors.joining(";", "[", "]")));

            } else {
                logger.debug("Data return is empty in {}, {}, {}",
                        joinPoint.getThis().getClass().getName(),
                        joinPoint.getTarget().getClass().getName(), signature);
            }
        } catch (ClassCastException | NullPointerException exception) {
            logger.debug("Exception in loggerData: {}", exception.getMessage());
        }
    }
}
