package com.czltnb.weblog.admin.event.subscriber;

import com.czltnb.weblog.admin.event.ReadArticleEvent;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.domain.mapper.StatisticsArticlePVDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {

    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private StatisticsArticlePVDOMapper articlePVDOMapper;

    @Override
    @Async("threadPoolTaskExecutor")
    //@Async("threadPoolTaskExecutor") 异步注解，表示该方法将通过异步线程来执行，使用的是名为 threadPoolTaskExecutor 的线程池。
    public void onApplicationEvent(ReadArticleEvent event) {
        // 在这里处理收到的事件，可以是任何逻辑操作
        Long articleId = event.getArticleId();

        // 获取当前线程名称
        String threadName = Thread.currentThread().getName();

        log.info("==> threadName: {}", threadName);
        log.info("==> 文章阅读事件消费成功，articleId: {}", articleId);

        articleDOMapper.increaseReadNum(articleId);
        log.info("==> 文章阅读量 +1 操作成功，articleId: {}", articleId);

        // 当日文章 PV 访问量 +1
        LocalDate currDate = LocalDate.now();
        articlePVDOMapper.increasePVCount(currDate);
        log.info("==> 当日文章 PV 访问量 +1 操作成功，date: {}", currDate);
    }
}
