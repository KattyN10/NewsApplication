package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Category;
import hcmute.kltn.backend.entity.Tag;
import hcmute.kltn.backend.entity.TagArticle;
import hcmute.kltn.backend.entity.enum_entity.ArtSource;
import hcmute.kltn.backend.entity.enum_entity.Status;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.CategoryRepo;
import hcmute.kltn.backend.repository.TagArticleRepo;
import hcmute.kltn.backend.repository.TagRepo;
import hcmute.kltn.backend.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CrawlerServiceImpl implements CrawlerService {
    private final CategoryRepo categoryRepo;
    private final TagArticleRepo tagArticleRepo;
    private final TagRepo tagRepo;
    private final ArticleRepo articleRepo;

    @Override
    public void crawlVnExpress() {
        String url = "https://vnexpress.net/tin-tuc-24h";
        try {
            Document document = Jsoup.connect(url).get();
            Elements articles = document.select(".item-news.item-news-common");

            // lấy 10 bài viết mới nhất thỏa mãn
            Elements newestArts = new Elements();
            for (Element art : articles) {
                boolean check = checkValidVnExpress(art);
                if (check) {
                    newestArts.add(art);
                }
                if (newestArts.size() == 10) break;
            }

            for (int i = 0; i < newestArts.size(); i++) {
                Article article = new Article();

                String title = newestArts.get(i).select("h3.title-news > a").text();
                String abstracts = newestArts.get(i).select("p.description a").text();
                String linkArticle = newestArts.get(i).select("h3.title-news a").attr("href");

                article.setTitle(title);
                article.setAbstracts(abstracts);

                boolean existedArt = articleRepo.existsByTitle(article.getTitle());
                if (!existedArt) {
                    Article mainArticle = mainContentVnExpress(linkArticle);
                    mainArticle.setTitle(article.getTitle());
                    mainArticle.setAbstracts(article.getAbstracts());

                    // save article, get and save tag
                    if (mainArticle.getCategory() != null) {
                        articleRepo.save(mainArticle);

                        String[] listTags = getTagsVnExpress(linkArticle);
                        for (String tagValue : listTags) {
                            TagArticle tagArticle = new TagArticle();
                            tagArticle.setArticle(mainArticle);
                            Tag tag = tagRepo.findByValue(tagValue);
                            if (tag == null) {
                                Tag newTag = new Tag();
                                newTag.setValue(tagValue);
                                tagRepo.save(newTag);
                                tagArticle.setTag(newTag);
                            } else {
                                tagArticle.setTag(tag);
                            }
                            tagArticleRepo.save(tagArticle);
                        }
                    }
                }
            }
        } catch (Exception e) {
            new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void crawlDanTri() {
        String url = "https://dantri.com.vn/tin-moi-nhat.htm";
        try {
            Document document = Jsoup.connect(url).get();
            Elements articles = document.select(".article-list.article-item");

            Elements newestArts = new Elements();
            for (Element art : articles) {
                boolean check = checkValidDanTri(art);
                if (check) {
                    newestArts.add(art);
                }
                if (newestArts.size() == 10) break;
            }

            for (int i = 1; i < newestArts.size(); i++) {
                Article article = new Article();

                String title = newestArts.get(i).select("h3.article-title > a").text();
                String abstracts = newestArts.get(i).select("div.article-excerpt a").text();
                String linkArticle = "https://dantri.com.vn/" + newestArts.get(i).select("div.article-excerpt a").attr("href");

                article.setTitle(title);
                article.setAbstracts(abstracts);

                boolean existedArt = articleRepo.existsByTitle(article.getTitle());
                if (!existedArt) {
                    Article articleDT = mainContentDanTri(linkArticle);
                    articleDT.setTitle(article.getTitle());
                    articleDT.setAbstracts(article.getAbstracts());
                    if (articleDT.getCategory() != null) {
                        articleRepo.save(articleDT);
                        List<String> listTags = getTagsDanTri(linkArticle);
                        if (!listTags.isEmpty()) {
                            for (int j = 0; j < listTags.size(); j++) {
                                TagArticle tagArticle = new TagArticle();
                                tagArticle.setArticle(articleDT);
                                Tag tag = tagRepo.findByValue(listTags.get(j));
                                if (tag == null) {
                                    Tag newTag = new Tag();
                                    newTag.setValue(listTags.get(j));
                                    tagRepo.save(newTag);
                                    tagArticle.setTag(newTag);
                                } else {
                                    tagArticle.setTag(tag);
                                }
                                tagArticleRepo.save(tagArticle);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean checkValidVnExpress(Element elementArt) {
        // Check quảng cáo dựa theo thẻ của title
        String title = elementArt.select("h3.title-news > a").text();
        String html = elementArt.html();
        Document document = Jsoup.parse(html);
        // Check bài media
        Element mediaElement = document.selectFirst("span.icon_thumb_videophoto");
        return !title.isEmpty() && mediaElement == null;
    }

    private float readingTime(String content) {
        int count = content.split("\\s+").length;
        int avgReadingSpeed = 200;
        return (float) (count / avgReadingSpeed);
    }

    private Article mainContentVnExpress(String url) {
        Article article = new Article();

        try {
            Document document = Jsoup.connect(url).get();

            // get create_date
            String date = document.select(".date").text();
            int commaIndex = date.indexOf(",");
            String trimmedString = date.substring(commaIndex + 1).trim();
            trimmedString = trimmedString.replaceAll("\\(.*\\)", "").trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy, HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(trimmedString, formatter);
            article.setCreate_date(dateTime);

            // get category
            Elements elementsCat = document.select("ul.breadcrumb li");
            if (elementsCat.size() == 1) {
                Category category;
                category = categoryRepo.findParentCatByName(elementsCat.get(0).text());
                if (category != null) {
                    Random random = new Random();
                    List<Category> childCat = categoryRepo.findChildCategories(category.getId());
                    article.setCategory(childCat.get(random.nextInt(childCat.size())));
                }
            } else {
                Category category, categoryParent;
                categoryParent = categoryRepo.findParentCatByName(elementsCat.get(0).text());
                if (categoryParent != null) {
                    category = categoryRepo.findByNameAndParent(elementsCat.get(1).text(), categoryParent);
                    if (category != null) {
                        article.setCategory(category);
                    }
                }
            }

            // get image avatar
            Element imgElement = document.selectFirst("img[itemprop=contentUrl]");
            if (imgElement != null) {
                String src = imgElement.attr("data-src");
                article.setAvatar(src);
            }

            // get content
            Element contentElement = document.selectFirst(".fck_detail");
            if (contentElement != null) {
                String content = contentElement.outerHtml();
                content = content.replace("amp;", "");
                content = content.replace("src=\"data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==\"", "");
                content = content.replace("data-src=", "src=");
                article.setContent(content);
            }

            article.setReading_time(readingTime(article.getContent()));
            article.setStatus(Status.PUBLIC);
            article.setArtSource(ArtSource.VN_EXPRESS);

            return article;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String[] getTagsVnExpress(String url) {
        try {
            String headContent = Jsoup.connect(url).get().head().html();

            Document documentTag = Jsoup.parse(headContent);
            Element tagElement = documentTag.selectFirst("meta[name=keywords]");
            if (tagElement != null) {
                String tagContent = tagElement.attr("content");
                String[] tags = tagContent.split(",");
                return tags;
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkValidDanTri(Element elementArt) {
        // Check bài photo
        Element photoElement = elementArt.selectFirst("div.photostory.article-category");
        // Check DNews
        Element dNewsElement = elementArt.selectFirst("div.dnews.article-category");
        //  Check Tâm điểm
        Element tamDiemElement = elementArt.selectFirst("div.blog article-category");
        return dNewsElement == null && photoElement == null &&tamDiemElement == null;
    }

    private Article mainContentDanTri(String url) {
        Article article = new Article();

        try {
            Document document = Jsoup.connect(url).get();

            // get create_date
            String date = document.select("time.author-time").text();
            int commaIndex = date.indexOf(",");
            String trimmedString = date.substring(commaIndex + 1).trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(trimmedString, formatter);
            article.setCreate_date(localDateTime);

            // get category
            Elements elementsCat = document.select("ul.dt-text-c808080.dt-text-base.dt-leading-5.dt-p-0.dt-list-none > li");
            String cat = null;
            Category category;
            if (elementsCat.size() == 1) {
                category = categoryRepo.findParentCatByName(elementsCat.get(0).text());
                if (category != null) {
                    Random random = new Random();
                    List<Category> childCat = categoryRepo.findChildCategories(category.getId());
                    article.setCategory(childCat.get(random.nextInt(childCat.size())));
                }
            } else if (elementsCat.size() != 0){
                category = categoryRepo.findBySecondOrName(elementsCat.get(1).text());
                if (category != null) {
                    article.setCategory(category);
                }
            }

            // get image avatar
            Element imgElement = document.selectFirst("img[data-content-name=article-content-image]");
            if (imgElement != null) {
                String src = imgElement.attr("data-original");
                article.setAvatar(src);
            }

            // get content
            Element authorElement = document.selectFirst("div.author-name");
            Element contentElement = document.selectFirst("div.singular-content");
            if (contentElement != null) {
                String content = contentElement.outerHtml();
                content = content.replace("amp;", "");
                content = content.replace("src=\"data:image/svg+xml", "s=\"data:image/svg+xml");
                content = content.replace("data-src=", "src=");
                if (authorElement != null) {
                    content = content + authorElement.outerHtml();
                }
                article.setContent(content);
            }

            article.setReading_time(readingTime(article.getContent()));
            article.setStatus(Status.PUBLIC);
            article.setArtSource(ArtSource.DAN_TRI);

            return article;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<String> getTagsDanTri(String url) {
        try {
            List<String> stringList = new ArrayList<>();
            Document document = Jsoup.connect(url).get();

            Elements listTags = document.select("ul.tags-wrap.mt-30 > li");
            if (listTags.size() > 1) {
                for (int i = 1; i < listTags.size(); i++) {
                    stringList.add(listTags.get(i).text());
                }

            }
            return stringList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
