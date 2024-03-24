package study.deliveryhanghae.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimerAop {

    @Pointcut("@annotation(study.deliveryhanghae.global.aop.Timer)")
    private void enableTimer(){}

    @Around("enableTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed(); // 메서드가 실행되는 부분

        stopWatch.stop();
        String methodName = joinPoint.getSignature().getName(); // 메서드 이름 가져오기
        System.out.println("Method: " + methodName + ", total time: " + stopWatch.getTotalTimeSeconds());

        return result;
    }
}