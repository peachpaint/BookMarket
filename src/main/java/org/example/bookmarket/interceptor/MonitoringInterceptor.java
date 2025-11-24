package org.example.bookmarket.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class MonitoringInterceptor implements HandlerInterceptor {
  ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<StopWatch>();

  @Override
  public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
    StopWatch stopWatch = new StopWatch(handler.toString());
    stopWatch.start(handler.toString());
    stopWatchLocal.set(stopWatch);
    log.info("접근한 URL 경로 : " + getURLPath(request));//로그 메세지 출력
    log.info("요청 처리 시작 시각 : " + getCurrentTime());//로그 메세지 출력
    return true;
  }

  @Override
  public void postHandle(@NonNull HttpServletRequest arg0, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    log.info("요청 처리 종료 시각 : " + getCurrentTime());
  }
  
  @Override
  public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
    StopWatch stopWatch = stopWatchLocal.get();
    stopWatch.stop();
    log.info("요청 처리 소요 시간 : " + stopWatch.getTotalTimeMillis()+ "ms");
    stopWatchLocal.set(null);
    log.info("-----------------------------");
  }

  private String getURLPath(HttpServletRequest request) {
    String currentPath = request.getRequestURI();
    String queryString = request.getQueryString();
    queryString = queryString == null ? "" : "?"+ queryString;
    return currentPath + queryString;
  }

  private String getCurrentTime() {
    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    return formatter.format(calendar.getTime());
  }
}
