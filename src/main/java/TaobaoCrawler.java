import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by designer01 on 5/31/17.
 */
public class TaobaoCrawler {
    public static final String URL_LIST = "https://babycook.taobao.com/i/asynSearch.htm?mid=w-14457103583-0&wid=14457103583&path=/category.htm&spm=a1z10.3-c-s.w4002-14457103583.52.JvKzRO&search=y&scene=taobao_shop&pageNo=1";
    public static final String URL_PRODUCT = "https://item.taobao.com/item.htm?spm=a1z10.3-c-s.w4002-14457103583.53.2p7IAs&id=";

    public static void main(String[] args) {
        // 24 items per page, 24 * 14 = 336 products
        for (int i = 1; i < 15; i++) {
            try {
                // list page
                Document doc = Jsoup.connect(URL_LIST).get();
                Elements items = doc.select("dl");
                for (Element item : items) {
                    Element detail = item.select("dd").get(0);
                    Elements spans = detail.select("span");

                    // get title, price and sale number
                    String title = detail.select("a").text();
                    String price = spans.get(1).text();
                    int saleNumber = Integer.parseInt(spans.get(spans.size() - 1).text());

                    // get product id
                    String id = item.attr("data-id");
                    id = id.substring(2, id.length() - 2);

                    // sleep
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // product page
                    // get brand name from product page
                    Document productPage = Jsoup.connect(URL_PRODUCT + id).get();
                    Elements descriptions = productPage.select(".attributes-list li");

                    String brand = "";
                    for (Element des : descriptions) {
                        String thisDes = des.text();
                        if (thisDes.indexOf("品牌") != -1) {
                            brand = thisDes.substring(5);
                        }
                    }

                    // create a new Product Object for each product
                    Product product = new Product();
                    product.setTitle(title);
                    product.setPrice(price);
                    product.setSaleNumber(saleNumber);
                    product.setBrand(brand);

                    // print product information
                    System.out.println(product.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
