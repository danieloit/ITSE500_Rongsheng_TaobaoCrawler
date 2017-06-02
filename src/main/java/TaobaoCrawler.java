import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by designer01 on 5/31/17.
 */
public class TaobaoCrawler {
    public static final String URL_LIST = "https://babycook.taobao.com/i/asynSearch.htm?mid=w-14457103583-0&wid=14457103583&path=/category.htm&spm=a1z10.3-c-s.w4002-14457103583.52.JvKzRO&search=y&scene=taobao_shop&pageNo=";
    public static final String URL_PRODUCT = "https://item.taobao.com/item.htm?spm=a1z10.3-c-s.w4002-14457103583.53.2p7IAs&id=";

    public static void main(String[] args) {
        String data = "卡米母婴\nhttps://babycook.taobao.com/category.htm?spm=a1z10.3-c-s.w5001-14457103492.5.YeeQ78&search=y&scene=taobao_shop\n\n";
        int productCount = 0;

        WriteFile wf = new WriteFile("document/data.txt", data);
        wf.run();

        // 24 items per page, 24 * 14 = 336 products
        for (int i = 1; i < 15; i++) {
            try {
                // list page
                Document doc = Jsoup.connect(URL_LIST + i).get();
                Elements items = doc.select("dl");
                for (Element item : items) {
                    Element detail = item.select("dd").get(0);
                    Elements spans = detail.select("span");

                    // get title, price and sale number
                    String title = detail.select("a").text();
                    String price = spans.get(1).text();
                    int saleNumber = Integer.parseInt(spans.get(spans.size() - 1).text());
                    String imageUrl = item.select("dt img").attr("src");
                    if (imageUrl.length() > 16) {
                        imageUrl = imageUrl.substring(2, imageUrl.length() - 14);
                        imageUrl = "http:" + imageUrl;
                    }

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
                    product.setId(id);
                    product.setBrand(brand);
                    product.setPrice(price);
                    product.setSaleNumber(saleNumber);
                    product.setImage(imageUrl);

                    // print product information
//                    System.out.println(product.toString());
                    productCount ++;
                    data = "\n商品" + productCount +
                            "\n名称： " + product.getTitle() +
                            "\n商品图片： " + product.getImage() +
                            "\nID： " + product.getId() +
                            "\n品牌： " + product.getBrand() +
                            "\n价格： ￥" + product.getPrice() +
                            "\n已售出： " + product.getSaleNumber() +
                            "\n";

                    // Write data to file
                    wf.setData(data);
                    wf.run();

                    // fetch images
                    FetchImage fi = new FetchImage("document/images/image" + productCount + ".jpg", product.getImage());
                    fi.run();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
