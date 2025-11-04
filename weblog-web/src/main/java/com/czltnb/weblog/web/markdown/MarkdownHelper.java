package com.czltnb.weblog.web.markdown;

import com.czltnb.weblog.web.markdown.provider.NofollowLinkAttributeProvider;
import com.czltnb.weblog.web.markdown.renderer.ImageNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

public class MarkdownHelper {

    /**
     * Markdown 解析器
     */
    private final static Parser PARSER;
    /**
     * HTML 渲染器
     */
    private final static HtmlRenderer HTML_RENDERER;

    /**
     * 初始化
     */
//    static {
//        PARSER = Parser.builder().build();
//        HTML_RENDERER = HtmlRenderer.builder().build();
//    }

    /**
     * 初始化
     */
    static {
        // Markdown 拓展
        // Markdown 拓展
        List<Extension> extensions = Arrays.asList(
                TablesExtension.create(), // 表格拓展
                HeadingAnchorExtension.create(), // 标题锚定项
                ImageAttributesExtension.create(), // 图片宽高
                TaskListItemsExtension.create() // 任务列表
        );

        PARSER = Parser.builder().extensions(extensions).build();
        HTML_RENDERER = HtmlRenderer.builder()
                .extensions(extensions)
                .attributeProviderFactory(context -> new NofollowLinkAttributeProvider())
                .nodeRendererFactory(context -> new ImageNodeRenderer(context))
                .build();
    }



    /**
     * 将 Markdown 转换成 HTML
     * @param markdown
     * @return
     */
    public static String convertMarkdown2Html(String markdown) {
        Node document = PARSER.parse(markdown);
        return HTML_RENDERER.render(document);
    }

//    public static void main(String[] args) {
//        String markdown = "This is *Sparta*";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
//    }
    public static void main(String[] args) {
//        String markdown = "| First Header  | Second Header |\n" +
//                "| ------------- | ------------- |\n" +
//                "| Content Cell  | Content Cell  |\n" +
//                "| Content Cell  | Content Cell  |";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
        String markdown = "# 一级标题\n" +
                "## 二级标题\n";
        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
//        String markdown = "![text](/url.png){width=640 height=480}";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
//        String markdown = "[个人网站域名](http://www.czltnb.com \"个人网站域名\")\n" +
//                "\n" +
//                "[第三方网站域名](http://www.baidu.com \"第三方网站域名\")";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
//        String markdown = "![图 1-1 技术栈](https://img.czltnb.com/czltnb/169560181378937 \"图 1-1 技术栈\")";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));
//        String markdown = "![图 1-1 技术栈](https://img.czltnb.com/czltnb/169560181378937 \"图 1-1 技术栈\"){width=100 height=100}";
//        System.out.println(MarkdownHelper.convertMarkdown2Html(markdown));

    }

}
